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
import com.lh64.randomdungeon.Journal;
import com.lh64.randomdungeon.Quests;
import com.lh64.randomdungeon.Statistics;
import com.lh64.randomdungeon.actors.buffs.Hunger;
import com.lh64.randomdungeon.actors.mobs.npcs.Blacksmith;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.actors.mobs.npcs.Wandmaker;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.bags.Keyring;
import com.lh64.randomdungeon.items.potions.PotionOfHealing;
import com.lh64.randomdungeon.levels.HubLevel;
import com.lh64.randomdungeon.scenes.InterlevelScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;
import com.lh64.utils.Random;

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
	
	public static void reset(boolean go){
		Journal.reset();
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Hunger.level = 0;
		Statistics.deepestFloor = 1;
		if(go == true){
		Dungeon.hero.resurrect(-1);
		Dungeon.gold = 0;
		} 
		Dungeon.deleteLevels(Dungeon.hero.heroClass);
		Dungeon.shop1 = Random.Int(3,10);
		Dungeon.shop2 = Random.Int(11,19);
		Dungeon.shop3 = Random.Int(20,27);
		Dungeon.shop1visit = false;
		Dungeon.shop2visit = false;
		Dungeon.shop3visit = false;
		Dungeon.shop4visit = false;
		Dungeon.initshop = true;
		Dungeon.previousTheme = Dungeon.levelTheme;
		Dungeon.levelTheme = 0;
		Item keyring = Dungeon.hero.belongings.getItem(Keyring.class);
		//reset keys
		if(keyring != null){
			keyring.detachAll(Dungeon.hero.belongings.backpack);
			
			new Keyring().collect(Dungeon.hero.belongings.backpack);
		}
		if(go == true){
		InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
		Game.switchScene( InterlevelScene.class );
		} else{
			InterlevelScene.returnDepth = 0;
			InterlevelScene.returnPos = HubLevel.bottomleft + 3;
			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			Game.switchScene( InterlevelScene.class );
		}
		Dungeon.initshop = true;
		PotionOfHealing.heal(Dungeon.hero);	
		((Hunger)Dungeon.hero.buff( Hunger.class )).satisfy( Hunger.STARVING );
		Quests.quests.removeAll(Quests.quests);
		for(int i = 0; i < 8; i++){
			Quests.quests.add(new Quests.Quest(Random.Int(1,3)));
		}
	}
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
				go = false;
				reset(true);
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
