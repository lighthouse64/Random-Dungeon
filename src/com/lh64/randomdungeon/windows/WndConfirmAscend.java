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
import com.lh64.randomdungeon.scenes.InterlevelScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;

public class WndConfirmAscend extends Window {
	
	private static final String TXT_MESSAGE	= "Do you want to go back to the hub? "
																					+ "\n Note: all of your gold and non-equipped items will be purged.";
	private static final String TXT_YES		= "Yes, I will return";
	private static final String TXT_NO		= "No, I'm continuing";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	public static boolean go = false;
	
	public static WndConfirmAscend instance;
	public static Object causeOfDeath;
	
	public WndConfirmAscend() {
		
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
				Dungeon.hero.resurrect(-1);
				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
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