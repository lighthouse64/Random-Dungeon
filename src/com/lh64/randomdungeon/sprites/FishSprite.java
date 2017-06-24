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
package com.lh64.randomdungeon.sprites;

import com.lh64.noosa.TextureFilm;
import com.lh64.noosa.tweeners.AlphaTweener;
import com.lh64.randomdungeon.Assets;

public class FishSprite extends MobSprite {
	private static final float FADE_TIME	= 3f;
		public boolean water = false;
		private Animation idleLand;
		private Animation moveLand;
		private Animation attackLand;
		private Animation dieLand;
	public FishSprite() {
		super();
		
		texture( Assets.FISH );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idleLand = new Animation( 2, true );
		idleLand.frames(frames, 10, 10, 10, 11);
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 6 );

			
		
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 1, 2 );
		
		moveLand = new Animation( 10, true );
		moveLand.frames(frames, 12, 13);
		
		attack = new Animation( 15, false );
		attack.frames( frames, 3, 4, 5 );
		
		attackLand = new Animation( 15, false );
		attackLand.frames(frames, 14, 15, 16);
		
		die = new Animation( 10, false );
		die.frames( frames, 7, 8, 9 );
		
		dieLand = new Animation( 10, false );
		dieLand.frames(frames, 17, 18, 19);
		
		play( idle );
	}
	
	@Override
	public void play (Animation anim){
		if(water == false){
			
			if(anim == attack){
				anim = attackLand;
				
			}
			else if(anim == idle){
				anim = idleLand;
			}
			else if(anim == run){
				anim = moveLand;
			}
			else if(anim == die){
				anim = dieLand;
				
				
			}
		}
		super.play(anim);
		
	}
	
	@Override
	public void onComplete(Animation anim){
		if(anim == attackLand){
			idle();
			ch.onAttackComplete();
		}
		else if(anim == dieLand){
			sleeping = false;
			if (emo != null) {
				emo.killAndErase();
			}
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					FishSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
		super.onComplete(anim);
	}
}


