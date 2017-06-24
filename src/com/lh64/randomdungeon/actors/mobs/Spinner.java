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
import com.lh64.randomdungeon.actors.blobs.Web;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.buffs.Poison;
import com.lh64.randomdungeon.actors.buffs.Roots;
import com.lh64.randomdungeon.actors.buffs.Terror;
import com.lh64.randomdungeon.items.food.MysteryMeat;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.SpinnerSprite;
import com.lh64.utils.Random;

public class Spinner extends Mob {
	
	{
		name = "cave spinner";
		spriteClass = SpinnerSprite.class;
		discovered = Dungeon.spinnerdiscovered;
		
		if(Dungeon.hero.lvl <= 6){
			HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*5 +4,(Dungeon.hero.lvl /3 +1)*6 +5);
			} else {
				HP = HT = Random.Int((Dungeon.hero.lvl /3 +1)*8 +4,(Dungeon.hero.lvl /3 +1)*10 +5);
			}
		
		defenseSkill = 3;
		
		
		maxLvl = Dungeon.hero.lvl / 5 + Dungeon.hero.lvl + 2;
		
		loot = new MysteryMeat();
		lootChance = 0.125f;
		
		FLEEING = new Fleeing();
	}
	
	@Override
	public int damageRoll() {
		if(Dungeon.hero.lvl <= 8){
			return Random.Int( (Dungeon.hero.lvl/3) ,(Dungeon.hero.lvl/3 + 1) + 4);
			} else {
			return Random.Int( (Dungeon.hero.lvl/3) +3,(Dungeon.hero.lvl/3)*2 + 5);
			}
	}
	
	@Override
	public int attackSkill( Char target ) {
		return skillpts(9);
	}
	@Override
	public int dr() {
		return (Dungeon.hero.lvl /3 +1) + 2 + Dungeon.hero.lvl/6 + Dungeon.hero.lvl/9;
	}
	
	@Override
	protected boolean act() {
		boolean result = super.act();
		if(Dungeon.visible[pos]){
			Dungeon.spinnerdiscovered = true;
			discovered = true;
		}
		if (state == FLEEING && buff( Terror.class ) == null) {
			if (enemy != null && enemySeen && enemy.buff( Poison.class ) == null) {
				state = HUNTING;
			}
		}
		return result;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if(Dungeon.hero.lvl > 6){
		if (Random.Int( 0,9  ) == 0) {
			Buff.affect( enemy, Poison.class ).set( Random.Int( 7, 9 ) * Poison.durationFactor( enemy ) );
			state = FLEEING;
		}
		}
		return damage;
		
	}
	
	@Override
	public void move( int step ) {
		if (state == FLEEING) {
			GameScene.add( Blob.seed( pos, Random.Int( 5, 7 ), Web.class ) );
		}
		super.move( step );
	}
	
	@Override
	public String description() {		
		return 
			"These greenish furry cave spiders try to avoid direct combat, preferring to wait in the distance " +
			"while their victim, entangled in the spinner's excreted cobweb, slowly dies from their poisonous bite.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Poison.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Roots.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null) {
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
