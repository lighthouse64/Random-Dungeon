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
import com.lh64.randomdungeon.actors.buffs.Bleeding;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.items.food.MysteryMeat;
import com.lh64.randomdungeon.sprites.CrabSprite;
import com.lh64.utils.Random;

public class Crab extends Mob {

	{
		name = "Sewer crab";
		spriteClass = CrabSprite.class;
		discovered= Dungeon.crabdiscovered;
		HP = HT = Random.Int(9,13);
		
		defenseSkill = skillpts(4);
		baseSpeed = 2f;
		
		EXP = expStats();
		maxLvl = Dungeon.hero.lvl + 4;
		
		loot = new MysteryMeat();
		lootChance = 0.167f;
	}
	
	@Override
	public int damageRoll() {
		
		return Random.Int( 2,5);
		
	}
	
	@Override
	public int attackSkill( Char target ) {
		return skillpts(11);
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		if (Random.Int( 0,15 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set(damage);
		}
		
		return damage;
	}
	@Override
	public int dr() {
		return 3;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		Dungeon.crabdiscovered = true;
		discovered = true;
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"These huge crabs are at the top of the food chain in the sewers. " +
			"They are extremely fast and their thick exoskeleton can withstand " +
			"heavy blows.";
	}
	@Override
	public String altDescription() {
		return
				"The old small, easygoing crabs have now become huge speedy beasts.  An adventurer, like yourself, had once brought in his two crabs for a good luck charm.  "
				+ "However, that adventurer faced a dreadful fate, and in that time, his crabs escaped.  Not having any predators, as their shells made them a difficult meal, the crabs feasted on the"
				+ "yellow moss at first, and as they grew larger, they began to hunt rats and gnolls.  Over time, their diet of eating such large creatures and yellow moss has led them to mutating into fast, large beings, "
				+ "which is why these crabs are so fierce and quick.";
	}
}
