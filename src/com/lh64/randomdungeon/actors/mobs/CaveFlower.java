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
import com.lh64.randomdungeon.actors.buffs.Buff;

import com.lh64.randomdungeon.actors.buffs.Vertigo;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;

import com.lh64.randomdungeon.sprites.CaveFlowerSprite;
import com.lh64.utils.Random;

public class CaveFlower extends Mob {
	

	{
		name = "Cave Flower";
		spriteClass = CaveFlowerSprite.class;
		if(Dungeon.hero.lvl <= 6){
		HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*5 +4,(Dungeon.hero.lvl /3 +1)*6 +5);
		} else {
			HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*8 +4,(Dungeon.hero.lvl /3 +1)*10 +5);
		}
		if(Dungeon.hero.lvl <= 6){
		defenseSkill = (Dungeon.hero.lvl/3 +1) + 2;
		} else{
			defenseSkill =(int)( ((1.5*Dungeon.hero.lvl/3) +1) + 3 + Dungeon.hero.lvl/6);
		}
		if(Dungeon.hero.lvl <= 1){
			EXP = Dungeon.hero.lvl;
			} else{
				EXP = Random.Int(Dungeon.hero.lvl/2,Dungeon.hero.lvl);
			}
		maxLvl = Dungeon.hero.lvl / 5 + Dungeon.hero.lvl + 2;
		
	}
	
	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
		return Random.Int( (Dungeon.hero.lvl/3) +1,(Dungeon.hero.lvl/3 + 1) + 3);
		} else {
		return Random.Int( (Dungeon.hero.lvl/3) +3,(Dungeon.hero.lvl/3 )*2 + 7);
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
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 0,20 ) == 0 && Dungeon.hero.lvl <= 6) {
			Buff.affect( enemy, Vertigo.class );
		} 
		else if(Random.Int(0,15) == 0 && Dungeon.hero.lvl > 6){
			Buff.affect( enemy, Vertigo.class );
		}
		
		return damage;
	}
	
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) + 1 + Dungeon.hero.lvl/9;
	}
	
	@Override
	public void die( Object cause ) {
		
		Ghost.Quest.processSewersKill( pos );
		
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"This flower is quite energetic.  It uses its roots and spiky leaves to attack its enemies.  It also occasionally secretes an annoying pollen that causes diziness.";
	}
}
