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

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLES20;
import android.text.InputType;
import android.widget.EditText;

import com.lh64.noosa.BitmapText;
import com.lh64.noosa.Camera;
import com.lh64.noosa.Game;
import com.lh64.noosa.Image;
import com.lh64.noosa.audio.Music;
import com.lh64.noosa.audio.Sample;
import com.lh64.noosa.ui.Button;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.effects.BannerSprites;
import com.lh64.randomdungeon.effects.Fireball;
import com.lh64.randomdungeon.ui.Archs;
import com.lh64.randomdungeon.ui.ExitButton;
import com.lh64.randomdungeon.ui.Icons;
import com.lh64.randomdungeon.ui.PrefsButton;
import com.lh64.randomdungeon.ui.RedButton;


public class TitleScene extends PixelScene {

	private static final String TXT_PLAY		= "Play";
	private static final String TXT_HIGHSCORES	= "Rankings";
	private static final String TXT_BADGES		= "Badges";
	private static final String TXT_ABOUT		= "About";

	
	@Override
	public void create() {
		
		super.create();
		if(Dungeon.changename == true){
			goStore();
			} 
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );
		
		float height = title.height + 
			(PixelDungeon.landscape() ? DashboardItem.SIZE : DashboardItem.SIZE * 2);
		
		title.x = (w - title.width()) / 2;
		title.y = (h - height) / 2;
		
		placeTorch( title.x + 8, title.y + 15 );
		placeTorch( title.x + title.width - 18, title.y + 15 );
		
		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = (float)Math.sin( -(time += Game.elapsed) );
			}
			@Override
			public void draw() {
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
				super.draw();
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
			}
		};
		signs.x = title.x;
		signs.y = title.y;
		add( signs );
		
		DashboardItem btnBadges = new DashboardItem( TXT_BADGES, 3 ) {
			@Override
			protected void onClick() {
				PixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		add( btnBadges );
		
		DashboardItem btnAbout = new DashboardItem( TXT_ABOUT, 1 ) {
			@Override
			protected void onClick() {
				PixelDungeon.switchNoFade( AboutScene.class );
				
			}
		};
		add( btnAbout );
		
		DashboardItem btnPlay = new DashboardItem( TXT_PLAY, 0 ) {
			@Override
			protected void onClick() {
				PixelDungeon.switchNoFade( StartScene.class );
			}
		};
		add( btnPlay );
		
		DashboardItem btnHighscores = new DashboardItem( TXT_HIGHSCORES, 2 ) {
			@Override
			protected void onClick() {
				PixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		add( btnHighscores );
		
		if (PixelDungeon.landscape()) {
			float y = (h + height) / 2 - DashboardItem.SIZE;
			btnHighscores	.setPos( w / 2 - btnHighscores.width(), y );
			btnBadges		.setPos( w / 2, y );
			btnPlay			.setPos( btnHighscores.left() - btnPlay.width(), y );
			btnAbout		.setPos( btnBadges.right(), y );
		} else {
			btnBadges.setPos( w / 2 - btnBadges.width(), (h + height) / 2 - DashboardItem.SIZE );
			btnAbout.setPos( w / 2, (h + height) / 2 - DashboardItem.SIZE );
			btnPlay.setPos( w / 2 - btnPlay.width(), btnAbout.top() - DashboardItem.SIZE );
			btnHighscores.setPos( w / 2, btnPlay.top() );
			
		}
		
		BitmapText version = new BitmapText( "v " + Game.version, font1x );
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width();
		version.y = h - version.height();
		add( version );
		
		RedButton test = new RedButton("Change your name"){
			@Override
			protected void onClick() {
				goStore();
			}
			
		};
		test.setRect(0 , version.y-7, 66, 15);
		add(test);
		try {
			Dungeon.loadName("Name");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		BitmapText str = new BitmapText(Dungeon.name + "'s",PixelScene.font3x);
		str.hardlight(Dungeon.color);
		str.measure();
		str.x = title.x ;
		str.y = title.y -23 ;
		add(str);
		
		//donate button
		DonateButton donate = new DonateButton();
		donate.setPos(w/2 -5, h - 30);
		add(donate);
		
		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setPos( 0, 0 );
		add( btnPrefs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}
	
	//Credit to TypedScroll for the DonateButton :)
	private static class DonateButton extends Button{

		protected Image image;

		public DonateButton() {
			super();

			width = image.width;
			height = image.height;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = Icons.SUPPORT.get();
			add( image );
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = x;
			image.y = y;
		}

		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}

		@Override
		protected void onTouchUp() {
			image.resetColor();
		}

		@Override
		protected void onClick() {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.com/cgi-bin/webscr?cmd=_donations&business=9pipbubbles%40gmail%2ecom&lc=US&item_name=Lighthouse64&item_number=Random%20Dungeon&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHostedGuest"));
			Game.instance.startActivity( browserIntent );
		}

}
	
	private static class DashboardItem extends Button {
		
		public static final float SIZE	= 48;
		
		private static final int IMAGE_SIZE	= 32;
		
		private Image image;
		private BitmapText label;
		
		public DashboardItem( String text, int index ) {
			super();
			
			image.frame( image.texture.uvRect( index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE ) );
			this.label.text( text );
			this.label.measure();
			
			setSize( SIZE, SIZE );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			image = new Image( Assets.DASHBOARD );
			add( image );
			
			label = createText( 9 );
			add( label );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			image.x = align( x + (width - image.width()) / 2 );
			image.y = align( y );
			
			label.x = align( x + (width - label.width()) / 2 );
			label.y = align( image.y + image.height() +2 );
		}
		
		@Override
		protected void onTouchDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 0.8f );
		}
		
		@Override
		protected void onTouchUp() {
			image.resetColor();
		}
	}
	public static void goStore(){
		
		PixelDungeon.instance.runOnUiThread( new Runnable(){
			@Override
			public void run(){
				final EditText input = new EditText(PixelDungeon.instance);
				AlertDialog.Builder builder = new AlertDialog.Builder(PixelDungeon.instance);
				builder.setTitle("Choose your name (Max: 12 characters) \n Non-Ascii characters do not work.");

				// Set up the input
				
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);

				// Set up the buttons
				builder.setPositiveButton("Choose Name", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        String name = input.getText().toString().replaceAll("[^\\x00-\\x7F]", "");
				        if(name.length() < 13){
				        Dungeon.name = name;
				        Dungeon.changename = false;
				        try {
							Dungeon.saveName();
						} catch (IOException e) {
							e.printStackTrace();
						}
				        Game.resetScene();
				        } else{
				        	dialog.cancel();
				        	retry();
				        }
				    }
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				    }
				});

				builder.show();
			}
		});
	}
	
	public static void retry(){
		AlertDialog.Builder retry = new AlertDialog.Builder(PixelDungeon.instance);
		retry.setTitle("Please try again");
		retry.setPositiveButton("retry", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goStore();
				dialog.cancel();
				
			}
		});
		retry.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});
		retry.show();
	}
}
