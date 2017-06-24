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
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Bleeding;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.sprites.AlbinoSprite;
import com.lh64.utils.Random;

public class Albino extends Rat {

	{
		name = "Albino rat";
		spriteClass = AlbinoSprite.class;
		discovered = Dungeon.albinodiscovered;
		
		HP = HT = Random.Int(9,12);
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Dungeon.albinodiscovered = true;
		discovered = true;
		Badges.validateRare( this );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set( damage );
		}
		
		return damage;
	}
	

}
