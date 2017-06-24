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



import com.lh64.randomdungeon.items.scrolls.ScrollOfReturn;
import com.lh64.randomdungeon.levels.Level;
import com.lh64.randomdungeon.levels.Room;
import com.lh64.randomdungeon.levels.Terrain;
import com.lh64.utils.Random;



public class AbandonedHousePainter extends Painter {


	
	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );
		


		
		
		
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
			int tempdoorpos = door.x + door.y*Level.WIDTH;
			
			placeholes(level, room, tempdoorpos);
			
		}
		
		
	}


	
	public static void placeholes( Level level, Room room, int tempdoorpos){
		int pos = room.random();
		set (level, pos, Terrain.EMPTY_WELL);
		if(Random.Int(1,4) > 1){
		level.drop(new ScrollOfReturn(), pos);
		}
		
		while(true){
			int pos1 = room.random();
			int pos2 = room.random();
			if(pos1 != pos && pos2 != pos && pos2 != pos1 && pos1 != tempdoorpos+ 1 && pos2 != tempdoorpos+1 && pos1 != tempdoorpos - 1 && pos2 != tempdoorpos - 1 && pos1 != tempdoorpos+ Level.WIDTH && pos2 != tempdoorpos+ Level.WIDTH && pos1 != tempdoorpos - Level.WIDTH  && pos2 != tempdoorpos - Level.WIDTH){
				
				set(level,pos1,Terrain.CHASM_FLOOR_SP);
				set(level,pos2,Terrain.CHASM_FLOOR_SP);
				
				break;
			}
		}
	}
}