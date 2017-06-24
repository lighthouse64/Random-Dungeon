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
package com.lh64.randomdungeon.items.rings;

import java.util.ArrayList;

import com.lh64.randomdungeon.Badges;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.actors.hero.HeroClass;
import com.lh64.randomdungeon.items.EquipableItem;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.ItemStatusHandler;
import com.lh64.randomdungeon.sprites.ItemSpriteSheet;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.randomdungeon.utils.Utils;
import com.lh64.randomdungeon.windows.WndOptions;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class Ring extends EquipableItem {

	private static final int TICKS_TO_KNOW	= 200;
	
	private static final float TIME_TO_EQUIP = 1f;
	
	private static final String TXT_IDENTIFY = 
		"you are now familiar enough with your %s to identify it. It is %s.";
	
	private static final String TXT_UNEQUIP_TITLE = "Unequip one ring";
	private static final String TXT_UNEQUIP_MESSAGE = 
		"You can only wear three rings at a time. " +
		"Unequip one of your equipped rings.";
	
	protected Buff buff;
	
	private static final Class<?>[] rings = { 
		RingOfMending.class, 
		RingOfDetection.class, 
		RingOfShadows.class,
		RingOfPower.class,
		RingOfHerbalism.class,
		RingOfAccuracy.class,
		RingOfEvasion.class,
		RingOfSatiety.class,
		RingOfHaste.class,
		RingOfHaggler.class,
		RingOfElements.class,
		RingOfThorns.class
	};
	private static final String[] gems = 
		{"diamond", "opal", "garnet", "ruby", "amethyst", "topaz", "onyx", "tourmaline", "emerald", "sapphire", "quartz", "agate"};
	private static final Integer[] images = {
		ItemSpriteSheet.RING_DIAMOND, 
		ItemSpriteSheet.RING_OPAL, 
		ItemSpriteSheet.RING_GARNET, 
		ItemSpriteSheet.RING_RUBY, 
		ItemSpriteSheet.RING_AMETHYST, 
		ItemSpriteSheet.RING_TOPAZ, 
		ItemSpriteSheet.RING_ONYX, 
		ItemSpriteSheet.RING_TOURMALINE, 
		ItemSpriteSheet.RING_EMERALD, 
		ItemSpriteSheet.RING_SAPPHIRE, 
		ItemSpriteSheet.RING_QUARTZ, 
		ItemSpriteSheet.RING_AGATE};
	
	private static ItemStatusHandler<Ring> handler;
	
	private String gem;
	
	private int ticksToKnow = TICKS_TO_KNOW;
	
	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images, bundle );
	}
	
	public Ring() {
		super();
		syncGem();
	}
	
	public void syncGem() {
		image	= handler.image( this );
		gem		= handler.label( this );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if(Dungeon.ShopkeeperBag == false && Dungeon.storage == false){
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		}
		return actions;
	}
	
	@Override
	public boolean doEquip( final Hero hero ) {
		
		if (hero.belongings.ring1 != null && hero.belongings.ring2 != null && hero.belongings.ring3 != null) {
			
			final Ring r1 = hero.belongings.ring1;
			final Ring r2 = hero.belongings.ring2;
			final Ring r3 = hero.belongings.ring3;
			
			PixelDungeon.scene().add( 
				new WndOptions( TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE, 
					Utils.capitalize( r1.toString() ), 
					Utils.capitalize( r2.toString() ),
					Utils.capitalize( r3.toString())) {
					
					@Override
					protected void onSelect( int index ) {
						
						detach( hero.belongings.backpack );
						Ring equipped;
						if( index == 0){
							equipped = ( r1 );
							
						}
						else if ( index == 1){
							equipped = ( r2 );
							
						}
						else {
							equipped  = ( r3 );
						}
						
						if (equipped.doUnequip( hero, true, false )) {
							doEquip( hero );
						} else {
							collect( hero.belongings.backpack );
						}
					}
				} );
			
			return false;
			
		} else {
			
			if (hero.belongings.ring1 == null) {
				hero.belongings.ring1 = this;
			} else if (hero.belongings.ring2 == null) {
				hero.belongings.ring2 = this;
			} else{
				hero.belongings.ring3 = this;
			}
			
			detach( hero.belongings.backpack );
			
			activate( hero );
			
			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( "your " + this + " tightens around your finger painfully" );
			}
			
			hero.spendAndNext( TIME_TO_EQUIP );
			return true;
			
		}

	}
	
	public void activate( Char ch ) {
		buff = buff();
		buff.attachTo( ch );
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			
			if (hero.belongings.ring1 == this) {
				hero.belongings.ring1 = null;
			} else if (hero.belongings.ring2 == this) {
				hero.belongings.ring2 = null;
			} else{
				hero.belongings.ring3 = null;
			}
			
			hero.remove( buff );
			buff = null;
			
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.ring1 == this || hero.belongings.ring2 == this || hero.belongings.ring3 == this;
	}
	
	@Override
	public int effectiveLevel() {
		return level();
	}
	
	
	
	
	
	public boolean isKnown() {
		return handler.isKnown( this );
	}
	
	protected void setKnown() {
		if (!isKnown()) {
			handler.know( this );
		}
		
		Badges.validateAllRingsIdentified();
	}
	
	@Override
	public String toString() {
		return 
			levelKnown ? 
				 super.toString() : 
				super.toString();
	}
	
	@Override
	public String name() {
		return isKnown() ? name : gem + " ring";
	}
	
	@Override
	public String desc() {
		return 
			"This metal band is adorned with a large " + gem + " gem " +
			"that glitters in the darkness. Who knows what effect it has when worn?";
	}
	
	@Override
	public String info() {
		if (isEquipped( Dungeon.hero )) {
			
			return desc() + "\n\n" + "The " + name() + " is on your finger" + 
				(cursed ? ", and because it is cursed, you are powerless to remove it." : "." );
			
		} else if (cursed && cursedKnown) {
			
			return desc() + "\n\nYou can feel a malevolent magic lurking within the " + name() + ".";
			
		} else {
			
			return desc();
			
		}
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown();
	}
	
	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}
	
	@Override
	public Item random() {
		int lvl = Random.Int( 0, 3 );
		if (Random.Float() < 0.4f) {
			degrade( lvl );
			cursed = true;
		} 
		return this;
	}
	
	public static boolean allKnown() {
		return handler.known().size() == rings.length - 2;
	}
	
	@Override
	public int price() {
		return considerState( 80 );
	}
	
	protected RingBuff buff() {
		return null;
	}
	
	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, ticksToKnow );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((ticksToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			ticksToKnow = TICKS_TO_KNOW;
		}
	}
	
	public class RingBuff extends Buff {
		
		private static final String TXT_KNOWN = "This is a %s"; 
		
		public int level;
		public RingBuff() {
			level = Ring.this.effectiveLevel();
		}
		
		@Override
		public boolean attachTo( Char target ) {

			if (target instanceof Hero && ((Hero)target).heroClass == HeroClass.ROGUE && !isKnown()) {
				setKnown();
				GLog.i( TXT_KNOWN, name() );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			return super.attachTo(target);
		}
		
		@Override
		public boolean act() {
			
			if (!isIdentified() && --ticksToKnow <= 0) {
				String gemName = name();
				identify();
				GLog.w( TXT_IDENTIFY, gemName, Ring.this.toString() );
				Badges.validateItemLevelAquired( Ring.this );
			}
			
			use();
			
			spend( TICK );
			
			return true;
		}
	}
}
