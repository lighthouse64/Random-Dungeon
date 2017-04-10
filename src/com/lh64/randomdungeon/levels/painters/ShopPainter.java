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
package com.lh64.randomdungeon.levels.painters;


import com.lh64.randomdungeon.actors.mobs.Mob;
import com.lh64.randomdungeon.actors.mobs.npcs.Shopkeeper;
import com.lh64.randomdungeon.levels.Level;
import com.lh64.randomdungeon.levels.Room;
import com.lh64.randomdungeon.levels.Terrain;


public class ShopPainter extends Painter {


	
	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );




		
		placeShopkeeper( level, room );
		
		
	}
	

	
	private static void placeShopkeeper( Level level, Room room ) {
		
		int pos;
		do {
			pos = room.random();
		} while (level.heaps.get( pos ) != null);
		
		Mob shopkeeper = new Shopkeeper();
		shopkeeper.pos = pos;
		level.mobs.add( shopkeeper );
		
	}
	

}
