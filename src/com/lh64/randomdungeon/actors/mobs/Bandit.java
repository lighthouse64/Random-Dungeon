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
import com.lh64.randomdungeon.actors.buffs.Blindness;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.sprites.BanditSprite;
import com.lh64.utils.Random;

public class Bandit extends Thief {
	
	public Item item;
	
	{
		name = "crazy bandit";
		spriteClass = BanditSprite.class;
		discovered = Dungeon.banditdiscovered;
	}
	
	@Override
	protected boolean steal( Hero hero ) {
		if (super.steal( hero )) {
			
			Buff.prolong( hero, Blindness.class, Random.Int( 5, 12 ) );
			Dungeon.observe();
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Dungeon.banditdiscovered = true;
		discovered = true;
		Badges.validateRare( this );
	}

}
