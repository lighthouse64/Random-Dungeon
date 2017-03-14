package com.lh64.randomdungeon.windows;

import com.lh64.noosa.BitmapTextMultiline;
import com.lh64.randomdungeon.actors.mobs.npcs.Shopkeeper;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;

public class WndPurchaseItem extends Window {

	
	private static final String TXT_MESSAGE	= "Will you buy or sell items?";
	private static final String TXT_YES		= "Buy";
	private static final String TXT_NO		= "Sell";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	public static boolean purchase = false;
	public static boolean visited = false;
	
	public static WndPurchaseItem instance;
	
	public WndPurchaseItem() {
super();
		
		instance = this;
		
		
		BitmapTextMultiline message = PixelScene.createMultiline( TXT_MESSAGE, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		add( message );
		
		RedButton btnYes = new RedButton( TXT_YES ) {
			@Override
			protected void onClick() {
				Shopkeeper.playerbuy();
				hide();
				
			}
		};
		btnYes.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnYes );
		
		RedButton btnNo = new RedButton( TXT_NO ) {
			@Override
			protected void onClick() {
				Shopkeeper.sell();
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
		visited = true;
		hide();
	}
	
}

