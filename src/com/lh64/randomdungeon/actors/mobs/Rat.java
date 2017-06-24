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
import com.lh64.randomdungeon.items.food.Cheese;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.sprites.RatSprite;
import com.lh64.utils.Random;

public class Rat extends Mob {

	{
		name = "Sewer rat";
		spriteClass = RatSprite.class;
		discovered = Dungeon.ratdiscovered;
		
		
			HP = HT = Random.Int(7,10);
			
		defenseSkill = 2;
		
		loot = new Cheese();
		lootChance = 0.07f;
		maxLvl = Dungeon.hero.lvl+ 5;
	}
	
	@Override
	public int damageRoll() {
		return Random.Int(1,4);
	}
	
	@Override
	public int attackSkill( Char target ) {
	
		return 7;
		
	
	
	}
	
	

	@Override
	public int dr() {
		return 1;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		
		Dungeon.ratdiscovered = true;
		discovered = true;
		sprite.showStatus( CharSprite.NEGATIVE, "X(" );
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"Sewer rats are aggressive, but rather weak denizens " +
			"of the sewers. They can be dangerous only in big numbers.  These rats also don't have rabies.";
	}
	@Override
	public String altDescription(){
		return "These rats are quite large, at a considerable 3 feet in height, although that is rather small compared to crabs.  They live off of mainly cheese and a few other things, but it is cheese that they really care for.  " +
	"If any one of these creatures happens to obtain cheese, it will guard it with its life.";
	}
}
