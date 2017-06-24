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
import com.lh64.randomdungeon.actors.mobs.Yog.BurningFist;
import com.lh64.randomdungeon.actors.mobs.Yog.RottingFist;
import com.lh64.randomdungeon.actors.mobs.npcs.Human;
import com.lh64.utils.Random;

public class Bestiary {

	public static Mob mob( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	public static Mob mutable( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		
		if (Random.Int( 30 ) == 0) {
			if (cl == Rat.class) {
				cl = Albino.class;
			} else if (cl == Thief.class) {
				cl = Bandit.class;
			} else if (cl == Brute.class) {
				cl = Shielded.class;
			} else if (cl == Monk.class) {
				cl = Senior.class;
			} else if (cl == Scorpio.class) {
				cl = Acidic.class;
			}
		}
		
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Class<?> mobClass( int depth ) {
		
		float[] chances;
		Class<?>[] classes;
		
		switch (depth) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:	
			if (Dungeon.levelTheme == 1){
				chances = new float[]{ 1, 1, 1, 0.8f, 0.7f, 0.07f, 1 };
				classes = new Class<?>[]{ Rat.class, Gnoll.class, SewerHorse.class, Crab.class, ToxicSludge.class, RatPrince.class, DungeonFish.class };
			}
			else if (Dungeon.levelTheme == 2){
				chances = new float[]{ 1, 1, 0.8f, 1, 0.75f };
				classes = new Class<?>[]{ Shaman.class, Gnoll.class, Skeleton.class, Thief.class, MasterThief.class };
			}
			else if (Dungeon.levelTheme == 3){
				chances = new float[]{ 1, 1, 1, 0.75f, 1, 0.9f, 0.07f };
				classes = new Class<?>[]{ Bat.class, Gnoll.class, Spinner.class, Brute.class, CaveFlower.class, Shaman.class, CaveSpider.class };
			}
			else{
				chances = new float[]{ 1, 1, 1, 1, 1, 1, 0.5f, 0.5f };
				classes = new Class<?>[]{ Rat.class, Gnoll.class, ToxicSludge.class, SewerHorse.class, Shaman.class, CaveFlower.class, Crab.class, Bat.class };
			}
			break;
		case 29:
			if(Dungeon.levelTheme == 1){
			chances = new float[]{ 1};
			classes = new Class<?>[]{ Goo.class };
			}
			else if (Dungeon.levelTheme == 2){
				chances = new float[]{ 1};
				classes = new Class<?>[]{ Tengu.class };
			}
			else if (Dungeon.levelTheme == 3){
				chances = new float[]{ 1 };
				classes = new Class<?>[]{ DM300.class };
			} else{
				chances = new float[]{ 1, 1, 1, 1, 1, 1, 0.5f, 0.5f };
				classes = new Class<?>[]{ Rat.class, Gnoll.class, ToxicSludge.class, SewerHorse.class, Shaman.class, CaveFlower.class, Crab.class, Bat.class };
			}
			break;
		default:
			chances = new float[]{ 1f };
			classes = new Class<?>[]{ Human.class };
		}
		
		return classes[ Random.chances( chances )];
	}
	
	public static boolean isBoss( Char mob ) {
		return 
			mob instanceof Goo || 
			mob instanceof Tengu || 
			mob instanceof DM300 ||  
			mob instanceof Yog || mob instanceof BurningFist || mob instanceof RottingFist;
	}
}
