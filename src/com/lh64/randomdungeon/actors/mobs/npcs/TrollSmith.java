/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.lh64.randomdungeon.actors.mobs.npcs;

import java.util.Collection;

import com.lh64.noosa.audio.Sample;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Badges;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.Journal;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.items.EquipableItem;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.scrolls.ScrollOfUpgrade;
import com.lh64.randomdungeon.levels.Room;
import com.lh64.randomdungeon.levels.Room.Type;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.BlacksmithSprite;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.randomdungeon.windows.WndForge;
import com.lh64.randomdungeon.windows.WndOptions;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class TrollSmith extends NPC {

	private static final String TXT_LOOKS_BETTER	= "your %s certainly looks better now";
	
	{
		name = "troll blacksmith";
		spriteClass = BlacksmithSprite.class;
	}
	public static int itemprice;
	public static boolean troll = true;
	@Override
	protected boolean act() {
		throwItem();		
		return super.act();
	}
	
	@Override
	public void interact() {
		Journal.add(Journal.Feature.TROLLSMITH);
		sprite.turnTo( pos, Dungeon.hero.pos );
		
			GameScene.show( new WndForge( this, Dungeon.hero ) );

	}
	

	
	public static String verify( Item item1, Item item2 ) {
		
		if (item1 == item2) {
			return "Select 2 different items, not the same item twice!";
		}
		
		if (item1.getClass() != item2.getClass()) {
			return "Select 2 items of the same type!";
		}
		
		
		if (!item1.isUpgradable() || !item2.isUpgradable()) {
			return "I can't reforge these items!";
		}
		
		return null;
	}
	
	public static void upgrade( final Item item1, final Item item2 ) {
		troll = true;
		if(item1.price() > item2.price()){
			if(item1.level() <= 0){
			itemprice = item1.price()*15;
			} else{
				itemprice = (item1.price()/item1.level()) *15;
			}
		} else{
			if(item2.level() <= 0){
				itemprice = item2.price()*15;
				} else{
					itemprice = (item2.price()/item2.level()) *15;
				}
		}
		
		GameScene.show(
				new WndOptions("Reforge Confirmation","Do you really want to reforge these items for " + itemprice ,"Yes","No"){
					@Override
					protected void onSelect( int index ) {
						if(index == 0){
							
							if(Dungeon.gold >= itemprice){
							Item first, second;
							if (item2.level() > item1.level()) {
								first = item2;
								second = item1;
							} else {
								first = item1;
								second = item2;
							}

							Sample.INSTANCE.play( Assets.SND_EVOKE );
							ScrollOfUpgrade.upgrade( Dungeon.hero );
							Item.evoke( Dungeon.hero );

							if (first.isEquipped( Dungeon.hero )) {
								((EquipableItem)first).doUnequip( Dungeon.hero, true );
							}
							first.upgrade();
							GLog.p( TXT_LOOKS_BETTER, first.name() );
							Dungeon.hero.spendAndNext( 2f );
							Badges.validateItemLevelAquired( first );

							if (second.isEquipped( Dungeon.hero )) {
								((EquipableItem)second).doUnequip( Dungeon.hero, false );
							}
							
							second.detachAll( Dungeon.hero.belongings.backpack );
							Dungeon.gold -= itemprice;
							} else{
								GLog.n("I don't serve the poor.");
							}
							
							
						} else  {
							GLog.b("How sad...");
						}
					}
				}
				);
		

	
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return 
			"This troll blacksmith uses its skill to earn large sums of money from those who want their items upgraded. \n However, his services are very overpriced, compared to the prices of the city blacksmith." +
				"\n He may be less picky about upgrading items, but that comes at a large price.";
	}

	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;
		
		public static void reset() {
			spawned		= false;
			given		= false;
			completed	= false;
			reforged	= false;
		}
		
		private static final String NODE	= "blacksmith";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REFORGED	= "reforged";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REFORGED, reforged );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	=  node.getBoolean( ALTERNATIVE );
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reforged = node.getBoolean( REFORGED );
			} else {
				reset();
			}
		}
		
		public static void spawn( Collection<Room> rooms ) {
			if (!spawned && Dungeon.depth > 11 && Random.Int( 15 - Dungeon.depth ) == 0) {
				
				Room blacksmith = null;
				for (Room r : rooms) {
					if (r.type == Type.STANDARD && r.width() > 4 && r.height() > 4) {
						blacksmith = r;
						blacksmith.type = Type.BLACKSMITH;
						
						spawned = true;
						alternative = Random.Int( 2 ) == 0;
						
						given = false;
						
						break;
					}
				}
			}
		}
	}
}
