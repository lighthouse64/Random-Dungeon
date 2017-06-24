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
		discovered = Dungeon.gnolldiscovered;
		
	
			HP = HT = Random.Int(8,12);
			
		defenseSkill = 2;
		
		maxLvl = Dungeon.hero.lvl + 3;
		
		loot = Gold.class;
		lootChance = 0.5f;
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
	public int dr() {
		return 2;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		Dungeon.gnolldiscovered = true;
		discovered = true;
		super.die( cause );
		
	}
	
	@Override
	public String description() {
		return
			"Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. " +
			"Gnoll scouts are regular members of their pack; they are not as strong as brutes and not as intelligent as shamans.";
	}
}
