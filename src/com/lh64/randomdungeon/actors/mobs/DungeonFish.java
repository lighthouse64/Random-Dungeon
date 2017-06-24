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
import com.lh64.randomdungeon.levels.Level;
import com.lh64.randomdungeon.sprites.FishSprite;
import com.lh64.utils.Random;

public class DungeonFish extends Mob {
	
	{
		name = "Dungeon Fish";
		spriteClass = FishSprite.class;
		
		discovered = Dungeon.dungeonfishdiscovered;
	
			HP = HT = Random.Int(8,13);
			
		defenseSkill = 2;
		
		maxLvl = Dungeon.hero.lvl + 3;
		
		
	}
	 
	@Override
	public void move( int step ){
	    ((FishSprite)sprite).water = Level.water[step];
	    super.move( step );
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
		Dungeon.dungeonfishdiscovered = true;
		discovered = true;
		super.die( cause );
		
	}
	
	@Override
	public String description() {
		return
			"These amphibious fish lurk the dungeons in search of prey.  It may not look like it, but their bites really do hurt.  ";
	}
	
	@Override
	public String altDescription(){
		return
				"This fish was born when a piranha died on land full of eggs.  The spawn of those eggs learned how to adapt to both the land and water and thus have become the way they are now.";
	}
}
