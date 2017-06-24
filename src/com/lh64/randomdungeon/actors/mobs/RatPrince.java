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
import com.lh64.randomdungeon.items.food.MoldyCheese;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.sprites.RatPrinceSprite;
import com.lh64.utils.Random;

public class RatPrince extends Mob {

	{
		name = "Rat Prince";
		spriteClass = RatPrinceSprite.class;
		discovered = Dungeon.ratprincediscovered;
		
		HP = HT = Random.Int(12,15);
		defenseSkill = 3;
		
		if(Random.Int(1,2) == 2){
		loot = new Cheese();
		} else{
			loot = new MoldyCheese();
		}
		
		lootChance = 1f;
		maxLvl = Dungeon.hero.lvl+ 5;
	}

	@Override
	public int damageRoll() {
		return Random.Int(3,7);
	}
	
	@Override
	public int attackSkill( Char target ) {
	
		return 7;
		
	
	
	}
	
	@Override
	public int dr() {
		return 2;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		sprite.showStatus( CharSprite.NEGATIVE, "..." );
		Dungeon.ratprincediscovered = true;
		discovered = true;
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"Prince rats are rather aggressive and strong denizens" +
			"of the sewers.  In their quest to become the rat king, they have gone mad and contracted rabies.  Potions of purification are of no use.  They will never come back to normal at this time.";
	}
}
