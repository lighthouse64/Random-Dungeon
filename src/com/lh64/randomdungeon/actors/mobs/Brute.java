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

import java.util.HashSet;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Terror;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.items.Gold;
import com.lh64.randomdungeon.sprites.BruteSprite;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class Brute extends Mob {

	private static final String TXT_ENRAGED = "%s becomes enraged!";
	
	{
		name = "gnoll brute";
		spriteClass = BruteSprite.class;
		discovered = Dungeon.brutediscovered;
		
		HP = HT = Random.Int(9,13);
		defenseSkill = 3;
		
		EXP = expStats();
		maxLvl = Dungeon.hero.lvl + 4;
		
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	private boolean enraged = false;
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		enraged = HP < HT / 4;
	}
	
	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
			return enraged ? Random.Int( (Dungeon.hero.lvl/3 + 1) +2 ,(Dungeon.hero.lvl/3 + 1)*2 +4) :	
				Random.Int( (Dungeon.hero.lvl/3 + 1) +1 ,(Dungeon.hero.lvl/3 + 1) +4);
			} else {
				return enraged ? Random.Int( (Dungeon.hero.lvl/3 +1 ) +6 - Dungeon.hero.lvl/6 ,(Dungeon.hero.lvl/3 )*3 +8 - Dungeon.hero.lvl/6 ):
						Random.Int( (Dungeon.hero.lvl/3 +1 ) +4 ,(Dungeon.hero.lvl/3 )*2 +8 - Dungeon.hero.lvl/6);
			}
		
				
				
	}
	

	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		Dungeon.brutediscovered = true;
		discovered = true;
		super.die( cause );
	}
	@Override
	public int attackSkill( Char target ) {
		return skillpts(9);
	}
	
	@Override
	public int dr() {
		return 3;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		super.damage( dmg, src );
		
		if (isAlive() && !enraged && HP < HT / 4) {
			enraged = true;
			spend( TICK );
			if (Dungeon.visible[pos]) {
				GLog.w( TXT_ENRAGED, name );
				sprite.showStatus( CharSprite.NEGATIVE, "enraged" );
			}
		}
	}
	
	@Override
	public String description() {
		return
			"Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, " +
			"they go berserk, inflicting even more damage to their enemies.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
