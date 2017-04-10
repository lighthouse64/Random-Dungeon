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
package com.lh64.randomdungeon.levels;


import com.lh64.noosa.Scene;

import com.lh64.noosa.particles.PixelParticle;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.mobs.Mob;
import com.lh64.randomdungeon.actors.mobs.npcs.Chest;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.utils.ColorMath;
import com.lh64.utils.Random;

public class TownLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}
	
	protected boolean[] water() {
		return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 4 );
	}
	
	@Override
	protected void decorate() {
		
		Mob chest = new Chest();
		while (true) {
			int pos = roomEntrance.random();
			int newpos = roomEntrance.random();
			if (pos != entrance && newpos != pos && newpos != entrance) {
				map[pos] = Terrain.SIGN;
				chest.pos = newpos;
				
				mobs.add(chest);
			
				break;
			} 
			
		}
	}
	
	@Override
	protected void createMobs() {
		super.createMobs();

	}
	
	@Override
	protected void createItems() {
		
	}
	@Override
	protected void paintWater() {
		
	}
	@Override
	protected void paintGrass(){
		
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
	
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Murky water";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "Wet yellowish moss covers the floor.";
		case Terrain.BOOKSHELF:
			return "The bookshelf is packed with cheap useless books. Might it burn?";
		default:
			return super.tileDesc( tile );
		}
	}
	


	public static final class WaterParticle extends PixelParticle {
		
		public WaterParticle() {
			super();
			
			acc.y = 50;
			am = 0.5f;
			
			color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
			size( 2 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			speed.set( Random.Float( -2, +2 ), 0 );
			
			left = lifespan = 0.5f;
		}
	}
}
