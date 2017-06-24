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
import com.lh64.randomdungeon.actors.buffs.Ooze;
import com.lh64.randomdungeon.actors.buffs.Poison;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.sprites.ToxicSludgeSprite;
import com.lh64.utils.Random;

public class ToxicSludge extends Mob {
	public String Deathwords;
	public int ChooseWord;

	{
		name = "Toxic Sewer Slime";
		spriteClass = ToxicSludgeSprite.class;
		discovered = Dungeon.toxicsludgediscovered;
		HP = HT = Random.Int(8,10);
		defenseSkill = 3;
		EXP = expStats();
		maxLvl = Dungeon.hero.lvl + 2;
	}
	
	@Override
	public int damageRoll() {
		return Random.Int(1,4);
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 9;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 0,10 ) == 0 && Dungeon.hero.lvl <= 6) {
			if(Random.Int(0,2) == 0){
			Buff.affect( enemy, Ooze.class );
			} else {
				Buff.affect(enemy,Poison.class).set( Random.Int( 5, 7 ) * Poison.durationFactor( enemy ) );
			}
		} 
		else if(Random.Int(0,6) == 0 && Dungeon.hero.lvl > 6){
			if(Random.Int(0,2) == 0){
				Buff.affect( enemy, Ooze.class );
				} else {
					Buff.affect(enemy,Poison.class).set( Random.Int( 6, 8 ) * Poison.durationFactor( enemy ) );
				}
		}
		
		return damage;
	}
	
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) + 1 + Dungeon.hero.lvl/6 + Dungeon.hero.lvl/9;
	}
	
	@Override
	public void die( Object cause ) {
		ChooseWord = Random.Int(0,3);
		switch (ChooseWord){
		case 0:
			Deathwords = "...";
			break;
		case 1:
			Deathwords = "Glurggle...";
			break;
		case 2:
			Deathwords = "GLOOP!";
			break;
		case 3:
			Deathwords = ">_<";
			break;
		default:
			Deathwords = "glurp";
			break;
		}
		Ghost.Quest.processSewersKill( pos );
		sprite.showStatus( CharSprite.NEGATIVE, Deathwords );
		Dungeon.toxicsludgediscovered = true;
		discovered = true;
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"This slime originates from all the dumped out batteries and lethal trash that people threw out.  Its very existence reminds people of what littering can really do.";
	}
}
