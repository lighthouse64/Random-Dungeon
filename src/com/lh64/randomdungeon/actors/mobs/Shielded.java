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

import com.lh64.randomdungeon.Badges;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.sprites.ShieldedSprite;

public class Shielded extends Brute {

	{
		name = "shielded brute";
		spriteClass = ShieldedSprite.class;
		discovered = Dungeon.shieldeddiscovered;
		
		defenseSkill = 4;
	}
	@Override 
	public boolean act() {
		if(Dungeon.visible[pos]){
			Dungeon.shieldeddiscovered = true;
			discovered = true;
			
		}
		return super.act();
	}
	@Override
	public int dr() {
		return 4;
	}
	
	@Override
	public String defenseVerb() {
		return "blocked";
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}
}
