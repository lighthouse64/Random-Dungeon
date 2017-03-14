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
package com.lh64.randomdungeon.windows;

import com.lh64.noosa.BitmapTextMultiline;
import com.lh64.noosa.Game;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.Statistics;
import com.lh64.randomdungeon.actors.buffs.Hunger;
import com.lh64.randomdungeon.scenes.InterlevelScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;
import com.lh64.utils.Random;

public class WndConfirmReturn extends Window {
	
	private static final String TXT_MESSAGE	= "Do you want to warp back to the hub?  This is your only way back, without purging your non-equipped items.";
	private static final String TXT_YES		= "Yes, I will return";
	private static final String TXT_NO		= "No, I'm staying";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	public static boolean go = false;
	
	public static WndConfirmReturn instance;
	public static Object causeOfDeath;
	
	public WndConfirmReturn() {
		
		super();
		
		instance = this;
		
		
		BitmapTextMultiline message = PixelScene.createMultiline( TXT_MESSAGE, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		add( message );
		
		RedButton btnYes = new RedButton( TXT_YES ) {
			@Override
			protected void onClick() {
				hide();
				go = true;
				Hunger.level = 0;
				Statistics.deepestFloor = 1;
				Dungeon.deleteLevels(Dungeon.hero.heroClass);
				Dungeon.shop1 = Random.Int(3,10);
				Dungeon.shop2 = Random.Int(11,19);
				Dungeon.shop3 = Random.Int(20,27);
				Dungeon.initshop = true;
				Dungeon.levelTheme = 0;
				InterlevelScene.returnDepth = 1;
				InterlevelScene.returnPos = 165;
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				Game.switchScene( InterlevelScene.class );
			}
		};
		btnYes.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnYes );
		
		RedButton btnNo = new RedButton( TXT_NO ) {
			@Override
			protected void onClick() {
				hide();
				
			}
		};
		
		btnNo.setRect( 0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );
		
		resize( WIDTH, (int)btnNo.bottom() );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
	
	@Override
	public void onBackPressed() {
	}
}
