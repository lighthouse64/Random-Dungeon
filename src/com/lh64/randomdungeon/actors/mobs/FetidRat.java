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

import java.util.HashSet;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.blobs.Blob;
import com.lh64.randomdungeon.actors.blobs.ParalyticGas;
import com.lh64.randomdungeon.actors.buffs.Paralysis;
import com.lh64.randomdungeon.items.quest.RatSkull;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.FetidRatSprite;
import com.lh64.utils.Random;

public class FetidRat extends Mob {

	{
		name = "fetid rat";
		spriteClass = FetidRatSprite.class;
		discovered = Dungeon.fetidratdiscovered;
		
		
			HP = HT = Random.Int(9,11);
			
		defenseSkill = 4;
		
		EXP = 1;
		maxLvl = Dungeon.hero.lvl + 3;
		
		state = WANDERING;
	}

	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
			return Random.Int( (Dungeon.hero.lvl/3) +1,(Dungeon.hero.lvl/3 + 1) + 4);
			} else {
			return Random.Int( (Dungeon.hero.lvl/3 + 1) +2 - Dungeon.hero.lvl/6,(Dungeon.hero.lvl/3)*2 + 6) - Dungeon.hero.lvl/6 ;
			}  
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 9;
	}
	
	@Override
	public int dr() {
		return 2  ;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		GameScene.add( Blob.seed( pos, 20, ParalyticGas.class ) );
		
		return super.defenseProc(enemy, damage);
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Dungeon.fetidratdiscovered = true;
		discovered = true;
		Dungeon.level.drop( new RatSkull(), pos ).sprite.drop();
	}
	
	@Override
	public String description() {
		return
			"This sewer rat is much larger than a regular one. It is surrounded by a foul cloud of paralyzing gas.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Paralysis.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
