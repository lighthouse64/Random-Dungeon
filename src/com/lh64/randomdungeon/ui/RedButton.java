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
package com.lh64.randomdungeon.ui;

import com.lh64.noosa.BitmapText;
import com.lh64.noosa.Image;
import com.lh64.noosa.NinePatch;
import com.lh64.noosa.audio.Sample;
import com.lh64.noosa.ui.Button;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Chrome;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.scenes.PixelScene;


public class RedButton extends Button {
	
	protected NinePatch bg;
	protected BitmapText text;
	protected Image icon;
			
	public RedButton( String label ) {
		super();
		
		text.text( label );
		text.measure();
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		bg = Chrome.get( Chrome.Type.BUTTON );
		
			bg.hardlight(Dungeon.color);
		
		add( bg );
		
		text = PixelScene.createText( 9 );
		add( text );
	}
	
	@Override
	protected void layout() {
		
		super.layout();
		
		bg.x = x;
		bg.y = y;
		bg.size( width, height );
		
		text.x = x + (int)(width - text.width()) / 2;
		text.y = y + (int)(height - text.baseLine()) / 2;
		
		if (icon != null) {
			icon.x = x + text.x - icon.width() - 2;
			icon.y = y + (height - icon.height()) / 2;
			
		}
	};
	
	@Override
	protected void onTouchDown() {
		bg.resetColor();
			if(Dungeon.color > 12566463){
				String rgbsplit = Integer.toString(Dungeon.color,16);
				int r = Integer.parseInt(rgbsplit.substring(0,2),16);
				int g = Integer.parseInt(rgbsplit.substring(2,4),16);
				int b = Integer.parseInt(rgbsplit.substring(4,6),16);
				String rhex = Integer.toHexString(r);
				String ghex = Integer.toHexString(g);
				String bhex = Integer.toHexString(b);
				if(r > 0x10){
					r -= 17;
					
				
				}
				if(r<0x10){
					rhex = "0"+Integer.toHexString(r);
				} else{
					rhex = Integer.toHexString(r);
				}
				if(g > 0x10){
					g-=17;
					
				
				}
				if(g<0x10){
					ghex = "0"+Integer.toHexString(g);
				} else{
					ghex = Integer.toHexString(g);
				}
				if(b > 0x10){
					b-=17;
				
				}
				if(b<0x10){
					bhex = "0"+Integer.toHexString(b);
				} else{
					bhex = Integer.toHexString(b);
				}
				
				int rgb = Integer.parseInt(rhex+ghex+bhex,16);
				
			
				
			bg.hardlight(rgb);
			
			} else{
				
				String rgbsplit = Integer.toString(Dungeon.color,16);
				
				if(rgbsplit.length() < 6){
					int l = rgbsplit.length();
					while(l < 6){
						rgbsplit = "0"+rgbsplit;
						l++;
					}
					
				}
				int r = Integer.parseInt(rgbsplit.substring(0,2),16);
				int g = Integer.parseInt(rgbsplit.substring(2,4),16);
				int b = Integer.parseInt(rgbsplit.substring(4,6),16);
				String rhex;
				String ghex;
				String bhex;
				
				r+=17;
				
					rhex = Integer.toHexString(r);

				if(g < 0xEF){
					g+=17;
					
				
				} else{
					g -= 17;
				}
				
					ghex = Integer.toHexString(g);

				if(b < 0xEF){
					b+=17;
				
				} else{
					g-=17;
				}
				
					bhex = Integer.toHexString(b);

				
				
				int rgb = Integer.parseInt(rhex+ghex+bhex,16);
				
				bg.hardlight(rgb);
				
				
			
			}
		
		Sample.INSTANCE.play( Assets.SND_CLICK );
	};
	
	@Override
	protected void onTouchUp() {
		bg.resetColor();
		
			bg.hardlight(Dungeon.color);
	
	};
	
	public void enable( boolean value ) {
		active = value;
		text.alpha( value ? 1.0f : 0.3f );
	}
	
	public void text( String value ) {
		text.text( value );
		text.measure();
		layout();
	}
	
	public void textColor( int value ) {
		text.hardlight( value );
	}
	
	public void icon( Image icon ) {
		if (this.icon != null) {
			remove( this.icon );
		}
		this.icon = icon;
		if (this.icon != null) {
			add( this.icon );
			
			layout();
		}
	}
	
	public float reqWidth() {
		return text.width() + 4;
	}
	
	public float reqHeight() {
		return text.baseLine() + 4;
	}
}
