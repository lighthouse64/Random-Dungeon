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
import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.utils.Random;

public class WndClickerGame extends Window {
	
	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	
	private static final int MARGIN = 4;
	public static int money = 0;
	public static int rats = 1;
	public static int ratlevel = 1;
	public static int clicklevel = 1;
	public static int multiplier = 1;
	public static int moodlevel = 0;
	public static boolean visible;
	public static BitmapTextMultiline moneyText;
	public static BitmapTextMultiline moodText;
	public static BitmapTextMultiline s1;
	public static BitmapTextMultiline s2;
	public static BitmapTextMultiline s3;
	public static BitmapTextMultiline s4;
	
	
	public WndClickerGame() {
		
		super();
		
		BitmapTextMultiline info = PixelScene.createMultiline("Rat King Gold Pile Builder", 6 );
		info.maxWidth = (PixelDungeon.landscape() ? WIDTH_L : WIDTH_P) - MARGIN * 2;
		info.measure();
		info.x = info.y = MARGIN;
		add( info );
		
		moneyText = PixelScene.createMultiline("Gold (in grams) " + Integer.toString(money), 6 );
		moneyText.measure();
		moneyText.x = MARGIN;
		moneyText.y = info.height() + 4;
		add(moneyText);
		
		moodText = PixelScene.createMultiline("Mood " + Integer.toString(moodlevel), 6 );
		moodText.measure();
		moodText.x = MARGIN;
		moodText.y = info.height() + moneyText.height() + 4;
		add(moodText);
		
		s1 = PixelScene.createMultiline("Number of Rats: " + Integer.toString(rats), 6);
		s2 = PixelScene.createMultiline("Level of Rats: " + Integer.toString(ratlevel), 6);
		s3 = PixelScene.createMultiline("Click level: " + Integer.toString(clicklevel), 6);
		s4 = PixelScene.createMultiline("Multiplier: " + Integer.toString(multiplier), 6);
		s1.measure();
		s2.measure();
		s3.measure();
		s4.measure();
		s1.x = MARGIN;
		s2.x = MARGIN;
		s3.x = MARGIN;
		s4.x = MARGIN;
		s1.y = info.height() + moneyText.height() + moodText.height() + 4;
		s2.y = info.height() + moneyText.height() + moodText.height() + s1.height() + 4;
		s3.y = (info.height()*5) + 4;
		s4.y = (info.height()*6) + 4;
		add(s1);
		add(s2);
		add(s3);
		add(s4);
		
		RedButton click = new RedButton("Click"){
			@Override
			protected void onClick(){
				click();
				
			}
		};
		
		click.setRect(MARGIN, s4.y + 6, 60, 20);
		add(click);
		
		RedButton shop = new RedButton("Shop"){
				@Override
				protected void onClick(){
					shopclick();
				}
		};
		
		shop.setRect(MARGIN, click.bottom() +3, 60, 20);
		add(shop);
		
		
		resize( 
			(int)info.width() + MARGIN * 2 + 4, 
			(int)(info.height()*7) + (int)click.height() + (int)shop.height() + 12 + MARGIN * 2
			);
		visible = isVisible();
	}
	
	public static void updateInfo(){
		money += rats * ratlevel;
		if(Random.Int(1,500) == 40){
			GLog.p("Lucky! You got some gold");
			Dungeon.gold += Random.Int(25,50);
		}
		if(visible == true){
			moneyText.text("Gold (in grams) " + Integer.toString(money));
			moodText.text("Mood " + Integer.toString(moodlevel));
			s1.text("Number of Rats: " + Integer.toString(rats));
			s2.text("Level of Rats: " + Integer.toString(ratlevel));
			s3.text("Click level: " + Integer.toString(clicklevel));
			s4.text("Multiplier: " + Integer.toString(multiplier));
		}
		
	}
	public static void click(){
		
		money+=clicklevel;
		moneyText.text("Gold (in grams) " + Integer.toString(money));
		
	}
	public static void shopclick(){
		GameScene.show(new WndOptionsNoHide("Upgrades","Buy upgrades to grow the rat king's gold pile","Increase Rat Workers (100)","Increase Rat Worker Level (250)","Increase Click Level (250)","Sacrifice the rats and their knowledge"){
			@Override
			protected void onSelect(int index){
				switch(index){
				case 0:
					if(Dungeon.gold >= 100){
					Dungeon.gold -= 100;
					rats += 1;
					}
					
					break;
				case 1:
					if(Dungeon.gold >= 250){
					Dungeon.gold -= 250;
					ratlevel += 1;
					}
					break;
				case 2:
					if(Dungeon.gold >= 250){
					Dungeon.gold -= 250;
					clicklevel += 1;
					} 
					break;
				case 3:
					GameScene.show(new WndOptions("Sacrifice the rats", "By doing this, your rats will all killed, and all their knowledge of gathering gold will be lost.  However, your multiplier will go up by 1, and the rat king's happiness level will go up by the number of rats killed minus 15.  Do you wish to do this?","Yes","No"){
						@Override
						protected void onSelect(int index){
							switch (index){
							case 0:
								if(rats >= 15){
									multiplier += 1;
									rats = 0;
									ratlevel = 0;
									moodlevel += rats - 15;
									
								} else{
									GLog.n("This amount of rats will not suffice");
								}
								break;
							}
						}
					});
					
					break;
				default:
					
					break;
				}
			}
		});
		
	}
}
