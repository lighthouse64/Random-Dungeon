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
import com.lh64.randomdungeon.actors.buffs.Bleeding;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.items.food.MysteryMeat;
import com.lh64.randomdungeon.sprites.CrabSprite;
import com.lh64.utils.Random;

public class Crab extends Mob {

	{
		name = "Sewer crab";
		spriteClass = CrabSprite.class;
		if(Dungeon.hero.lvl <= 6){
		HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*6 +3,(Dungeon.hero.lvl /3 +1)*7 +5);
		} else {
		HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*8 +3,(Dungeon.hero.lvl /3 +1)*11 +5);
		}
		if(Dungeon.hero.lvl <= 6){
		defenseSkill = ((Dungeon.hero.lvl/3 +1) * 2) + 2;
		} else {
			defenseSkill = ((Dungeon.hero.lvl/3 +1) *2) + 3 + Dungeon.hero.lvl/6;
		}
		baseSpeed = 2f;
		
		if(Dungeon.hero.lvl <= 1){
			EXP = Dungeon.hero.lvl + Random.Int(1,2);
			} else{
				EXP = Random.Int(Dungeon.hero.lvl/2,Dungeon.hero.lvl) + Random.Int(0,Dungeon.hero.lvl/5 + 2);
			}
		maxLvl = Dungeon.hero.lvl + 4;
		
		loot = new MysteryMeat();
		lootChance = 0.167f;
	}
	
	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
		return Random.Int( (Dungeon.hero.lvl/3 + 1) +1 ,(Dungeon.hero.lvl/3 + 1) +5);
		} else {
		return Random.Int( (Dungeon.hero.lvl/3 +1 ) +5 ,(Dungeon.hero.lvl/3 )*2 +8);
		}
	}
	
	@Override
	public int attackSkill( Char target ) {
		if(Dungeon.hero.lvl <= 7){
		return (Dungeon.hero.lvl/3 +1)*2 + 9;
		} else{
			return (Dungeon.hero.lvl/3 +1)*2 + 9 + (Dungeon.hero.lvl/6 + 1);
		}
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if(Dungeon.hero.lvl > 6){
		if (Random.Int( 0,15 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set(damage);
		}
		}
		return damage;
	}
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) + 2 + Dungeon.hero.lvl/6;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"These huge crabs are at the top of the food chain in the sewers. " +
			"They are extremely fast and their thick exoskeleton can withstand " +
			"heavy blows.";
	}
}
