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
		
		HP = HT = Random.Int((Dungeon.hero.lvl /3 +1) * 3 +4,(Dungeon.hero.lvl/3 +1) *3 +6);
		defenseSkill =  ((Dungeon.hero.lvl/3 +1) * 2) + 2;
		
		EXP = Dungeon.hero.lvl;
		maxLvl = Dungeon.hero.lvl + 3;
		
		state = WANDERING;
	}
	
	@Override
	public int damageRoll() {
		return Random.Int( (Dungeon.hero.lvl/3 + 1) +1,(Dungeon.hero.lvl/3 + 1) +4);
	}
	
	@Override
	public int attackSkill( Char target ) {
		return (Dungeon.hero.lvl/3 +1) + 9;
	}
	
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) ;
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
