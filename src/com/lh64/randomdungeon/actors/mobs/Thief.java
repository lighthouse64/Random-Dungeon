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
package com.lh64.randomdungeon.actors.mobs;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Terror;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.items.Gold;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.rings.RingOfHaggler;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.sprites.ThiefSprite;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.randomdungeon.utils.Utils;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class Thief extends Mob {

	protected static final String TXT_STOLE	= "%s stole %s from you!";
	protected static final String TXT_CARRIES	= "\n\n%s is carrying a _%s_. Stolen obviously.";
	
	public Item item;
	
	{
		name = "crazy thief";
		spriteClass = ThiefSprite.class;
		discovered = Dungeon.thiefdiscovered;
		
		if(Dungeon.hero.lvl <= 6){
			HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*5 +4,(Dungeon.hero.lvl /3 +1)*6 +5);
			} else {
				HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*8 +4,(Dungeon.hero.lvl /3 +1)*10 +5);
			}
		defenseSkill = 3;
		
		EXP = expStats();
		maxLvl = Dungeon.hero.lvl + 5;
		
		loot = RingOfHaggler.class;
		lootChance = 0.01f;
		
		FLEEING = new Fleeing();
	}
	
	private static final String ITEM = "item";
	@Override 
	public boolean act() {
		if(Dungeon.visible[pos]){
			Dungeon.thiefdiscovered = true;
			discovered = true;
			
		}
		return super.act();
	}
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ITEM, item );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		item = (Item)bundle.get( ITEM );
	}
	
	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
			return Random.Int( (Dungeon.hero.lvl/3) ,(Dungeon.hero.lvl/3 + 1) + 4);
			} else {
			return Random.Int( (Dungeon.hero.lvl/3) +3 - Dungeon.hero.lvl/6,(Dungeon.hero.lvl/3)*2 + 2) - Dungeon.hero.lvl/6 - Dungeon.hero.lvl/9;
			}
	}
	
	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		super.die( cause );
		
		if (item != null) {
			Dungeon.level.drop( item, pos ).sprite.drop();
		}
	}
	
	@Override
	public int attackSkill( Char target ) {
		return skillpts(8);
	}
	
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) + 1 + Dungeon.hero.lvl/6 + Dungeon.hero.lvl/9;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (item == null && enemy instanceof Hero && steal( (Hero)enemy )) {
			state = FLEEING;
		}
		
		return damage;
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		if (state == FLEEING) {
			Dungeon.level.drop( new Gold(), pos ).sprite.drop();
		}
		
		return damage;
	}
	
	protected boolean steal( Hero hero ) {
		
		Item item = hero.belongings.randomUnequipped();
		if (item != null) {
			
			GLog.w( TXT_STOLE, this.name, item.name() );
			
			item.detachAll( hero.belongings.backpack );
			this.item = item;
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String description() {
		String desc =
			"Deeper levels of the dungeon have always been a hiding place for all kinds of criminals. " +
			"Not all of them could keep a clear mind during their extended periods so far from daylight. Long ago, " +
			"these crazy thieves and bandits have forgotten who they are and why they steal.";
		
		if (item != null) {
			desc += String.format( TXT_CARRIES, Utils.capitalize( this.name ), item.name() );
		}
		
		return desc;
	}
	
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null) {
				sprite.showStatus( CharSprite.NEGATIVE, TXT_RAGE );
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
