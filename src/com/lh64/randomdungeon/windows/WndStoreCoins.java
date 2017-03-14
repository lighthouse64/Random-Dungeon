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
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;

import android.content.Context;




public class WndStoreCoins extends Window {

	
	private static final String TXT_MESSAGE	= "What will you do with your gold?";
	private static final String TXT_YES		= "Store";
	private static final String TXT_NO		= "Withdraw";
	private static final String TXT_VIEW    = "View Balance";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;

	public static Context context;
	public static WndStoreCoins instance;

	
	public WndStoreCoins() {
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
				GameScene.show(new DepositOrWithdraw(true));
			}
		};
		btnYes.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnYes );
		
		RedButton btnView = new RedButton( TXT_VIEW ) {
			@Override
			protected void onClick() {
				GameScene.show(new WndMessage("Your current balance is " + Dungeon.coins + " gold."));
				hide();
			}
		};
		btnView.setRect(0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add(btnView);
		RedButton btnNo = new RedButton( TXT_NO ) {
			@Override
			protected void onClick() {
				
				hide();
				GameScene.show(new DepositOrWithdraw(false));
			}
		};
		
		btnNo.setRect( 0, btnView.bottom() + GAP, WIDTH, BTN_HEIGHT );
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
		
		hide();
	}
	
}
