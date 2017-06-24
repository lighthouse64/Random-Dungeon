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
import com.lh64.randomdungeon.actors.buffs.Blindness;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.buffs.Poison;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.items.Gold;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.sprites.MasterThiefSprite;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class MasterThief extends Mob {
	protected static final String TXT_STOLE	= "%s stole %s from you!";
	protected static final String TXT_CARRIES	= "\n\n%s is carrying a _%s_. Stolen obviously.";
	public Item item;
	{
		name = "Master thief";
		spriteClass = MasterThiefSprite.class;
		discovered = Dungeon.masterthiefdiscovered;
		baseSpeed=1.5f;
		
		HP = HT = Random.Int(10,15);
		defenseSkill = 8;
		baseSpeed = 1.25f;

		maxLvl = Dungeon.hero.lvl+ 5;
		
	}
	
	@Override
	public int damageRoll() {
		return Random.Int(1,6);
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if(Dungeon.hero.lvl > 3){
		if (Random.Int( 0,9 ) == 0) {
			Buff.affect( enemy, Poison.class ).set(damage);
		}
		else if(Random.Int(0,8)==0){
			Buff.prolong( Dungeon.hero, Blindness.class, Random.Int( 3, 7 ) );
		}
		}
		if (item == null && enemy instanceof Hero && steal( (Hero)enemy )) {
			state = HUNTING;
		}else{
			state = HUNTING;
		}
		return damage;
	}
	private static final String ITEM = "item";
	
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
	public int attackSkill( Char target ) {
	
		return 13;
		
	
	
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "I see you..." );
	}
	
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +2 + Dungeon.hero.lvl/6) + Dungeon.hero.lvl/9;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		if (item != null) {
			Dungeon.level.drop( item, pos ).sprite.drop();
		}
		Dungeon.masterthiefdiscovered = true;
		discovered = true;
		sprite.showStatus( CharSprite.NEGATIVE, "..." );
		super.die( cause );
		Dungeon.level.drop(new Gold(Random.Int(50,75)), pos).sprite.drop();
	}
	
	@Override
	public String description() {
		return
			"These thieves, the leaders of the guild, are much more greedy and slim than the other thieves.  In fact, the green of their eyes comes from the love of money."
			+ "\n The master class thieves are dangerous in general, so take precaution.";
	}
}
