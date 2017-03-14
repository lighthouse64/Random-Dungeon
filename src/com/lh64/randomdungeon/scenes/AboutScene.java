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
package com.lh64.randomdungeon.scenes;

import android.content.Intent;
import android.net.Uri;

import com.lh64.input.Touchscreen.Touch;
import com.lh64.noosa.BitmapTextMultiline;
import com.lh64.noosa.Camera;
import com.lh64.noosa.Game;
import com.lh64.noosa.Image;
import com.lh64.noosa.TouchArea;
import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.effects.Flare;
import com.lh64.randomdungeon.ui.Archs;
import com.lh64.randomdungeon.ui.ExitButton;
import com.lh64.randomdungeon.ui.Icons;
import com.lh64.randomdungeon.ui.Window;

public class AboutScene extends PixelScene {

	private static final String TXT = 
		"Modded by Lighthouse64 :P \n"+
		"Code & graphics: Watabou and Lighthouse64\n" +
		"Music: Cube_Code and Lighthouse64\n" +
		"Please donate to me here:\n\n";
	
	private static final String LNK = "paypal.com/cgi-bin/webscr?cmd=_donations&business=9pipbubbles%40gmail%2ecom&lc=US&item_name=Lighthouse64&item_number=Random%20Dungeon&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHostedGuest";
	public static String youtube1 ="\n\n My youtube channel is here: \n";
	public static final String Channel = "youtube.com/channel/UCc3Z_p5Qag9V3lPm3pFQ9lg";
	@Override
	public void create() {
		super.create();
		
		BitmapTextMultiline text = createMultiline( TXT, 8 );
		text.maxWidth = Math.min( Camera.main.width, 120 );
		text.measure();
		add( text );
		
		text.x = align( (Camera.main.width - text.width()) / 2 );
		text.y = align( (Camera.main.height - text.height()) / 2 );
		
		BitmapTextMultiline link = createMultiline( LNK, 8 );
		link.maxWidth = Math.min( Camera.main.width, 120 );
		link.measure();
		link.hardlight( Window.TITLE_COLOR );
		add( link );
		
		link.x = text.x;
		link.y = text.y + text.height();
		
		BitmapTextMultiline channel = createMultiline ( youtube1, 8);
		channel.maxWidth = Math.min( Camera.main.width, 120 );
		channel.measure();
		channel.hardlight( 0x38c491 );
		add( channel );
		
		channel.x = text.x;
		channel.y = link.y + link.height();
		
		BitmapTextMultiline youtube = createMultiline ( Channel, 8);
		youtube.maxWidth = Math.min( Camera.main.width, 120 );
		youtube.measure();
		youtube.hardlight( Window.TITLE_COLOR);
		add( youtube );
		
		youtube.x = text.x;
		youtube.y = channel.y + channel.height();
		
		
		TouchArea hotArea = new TouchArea( link ) {
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + LNK ) );
				Game.instance.startActivity( intent );
			}
		};
		add( hotArea );
		
		TouchArea link2Area = new TouchArea( youtube ){
			@Override
			protected void onClick( Touch touch ) {
				Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "http://" + Channel ) );
				Game.instance.startActivity( intent );
			}
		};
		add( link2Area );
		
		Image wata = Icons.WATA.get();
		wata.x = align( (Camera.main.width - wata.width) / 2 );
		wata.y = text.y - wata.height - 8;
		add( wata );
		
		new Flare( 7, 64 ).color( 0x112253, true ).show( wata, 0 ).angularSpeed = +20;
		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		PixelDungeon.switchNoFade( TitleScene.class );
	}
}
