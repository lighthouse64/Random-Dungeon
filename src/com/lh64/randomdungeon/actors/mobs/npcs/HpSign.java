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
package com.lh64.randomdungeon.actors.mobs.npcs;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.buffs.Hunger;
import com.lh64.randomdungeon.items.potions.PotionOfHealing;
import com.lh64.randomdungeon.sprites.hpsignSprite;

public class HpSign extends NPC {

	{
		name = "HP SIGN";
		spriteClass = hpsignSprite.class;
		
		state = PASSIVE;
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	@Override
	public boolean act(){
		throwItem();
		spend( TICK );
		return true;
	}
	

	
	@Override
	public void add( Buff buff ) {
	}
	
	
	@Override
	public void interact() {
	PotionOfHealing.heal(Dungeon.hero);	
	((Hunger)Dungeon.hero.buff( Hunger.class )).satisfy( Hunger.STARVING );
	}
	
	@Override
	public String description() {
		return 
			"The easiest way for me to make it possible for you to regain your health.";
	}
}
