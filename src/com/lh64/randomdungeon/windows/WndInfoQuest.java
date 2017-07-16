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
import com.lh64.randomdungeon.Quests;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.Icons;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;
import com.lh64.randomdungeon.utils.GLog;

public class WndInfoQuest extends Window {
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	private static Quests.Quest q;
	public WndInfoQuest( Quests.Quest quest, Boolean heroquest ) {
		
		super();
		q = quest;
		IconTitle titlebar = new IconTitle();
		titlebar.icon( Icons.get( Icons.DEPTH ) );
		titlebar.label( quest.title );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline info = PixelScene.createMultiline( 6 );
		add( info );
		
		info.text( quest.description );
		info.maxWidth = WIDTH;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		
		RedButton takequest = new RedButton("Accept Quest"){
			@Override 
			protected void onClick(){
				if(Quests.heroquests.size() >= 8){
					GLog.n("You can't take any more quests");
				} else{
				if(q.given != true){
				Quests.heroquests.add(q);
				Quests.quests.remove(q);
				q.given = true;
				} else{
					GLog.n("This quest has already been taken");
					
				}
				}
				
				hide();
				
			}
		};
		
		takequest.setRect(info.x + 7, info.y + 30, takequest.reqWidth() + 2, takequest.reqHeight() + 2);
		
	RedButton removequest = new RedButton("Remove Quest"){
		@Override
		protected void onClick(){
			if(Quests.heroquests.contains(q)){
			Quests.heroquests.remove(q);
			} else{
				GLog.h("This quest has already been removed");
			}
			hide();
			
		}
	};
	removequest.setRect(info.x + 7, info.y + 30, removequest.reqWidth() + 2, removequest.reqHeight() + 2);
	
	
		if(heroquest != true){
		add(takequest);
		} else{
		add(removequest);
		}
			resize( WIDTH, (int)(takequest.bottom() + info.height()) );
		
	}
}
