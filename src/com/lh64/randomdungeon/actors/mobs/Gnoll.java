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
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.items.Gold;
import com.lh64.randomdungeon.sprites.GnollSprite;
import com.lh64.utils.Random;

public class Gnoll extends Mob {
	
	{
		name = "gnoll scout";
		spriteClass = GnollSprite.class;
		
		if(Dungeon.hero.lvl <= 6){
			HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*5 +4,(Dungeon.hero.lvl /3 +1)*6 +5);
			} else {
				HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*8 +4,(Dungeon.hero.lvl /3 +1)*10 +5);
			}
		if(Dungeon.hero.lvl<= 6){
		defenseSkill = ((Dungeon.hero.lvl/3 +1) * 2) + 2;
		} else{
			defenseSkill = ((Dungeon.hero.lvl/3 +1)*2) + 3 + Dungeon.hero.lvl/6;
		}
		if(Dungeon.hero.lvl <= 1){
			EXP = Dungeon.hero.lvl + Random.Int(0,2);
			} else{
				EXP = Random.Int(Dungeon.hero.lvl/2,Dungeon.hero.lvl) + Random.Int(0,Dungeon.hero.lvl/5 + 2);
			}
		maxLvl = Dungeon.hero.lvl + 3;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
			return Random.Int( (Dungeon.hero.lvl/3) +1,(Dungeon.hero.lvl/3 + 1) + 4);
			} else {
			return Random.Int( (Dungeon.hero.lvl/3 + 1) +2,(Dungeon.hero.lvl/3)*2 + 6);
			}  
	}
	
	@Override
	public int attackSkill( Char target ) {
		if(Dungeon.hero.lvl <= 7){
		return (Dungeon.hero.lvl/3 +1)*2 + 7;
		} else{
			return (Dungeon.hero.lvl/3 +1)*2 + 7 + (Dungeon.hero.lvl /6 + 1) + Dungeon.hero.lvl/9;
		}
	}
	
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) + 1+ Dungeon.hero.lvl/6;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. " +
			"Gnoll scouts are regular members of their pack; they are not as strong as brutes and not as intelligent as shamans.";
	}
}
