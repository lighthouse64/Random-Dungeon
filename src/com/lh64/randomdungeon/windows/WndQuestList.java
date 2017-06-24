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



import java.util.ArrayList;

import com.lh64.noosa.BitmapText;
import com.lh64.noosa.Image;
import com.lh64.noosa.ui.Component;
import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.Quests;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.Icons;
import com.lh64.randomdungeon.ui.ScrollPane;
import com.lh64.randomdungeon.ui.Window;

public class WndQuestList extends Window {
	
	private static final int WIDTH		= 112;
	private static final int HEIGHT_P	= 160;
	private static final int HEIGHT_L	= 144;
	
	private static final int ITEM_HEIGHT	= 18;
	private static boolean heroquest1;
	
	private static final String TXT_TITLE	= "Quests";
	
	private BitmapText txtTitle;
	private ScrollPane list;
	private ArrayList<ListItem> items = new ArrayList<WndQuestList.ListItem>();
	
	
	public WndQuestList(Boolean heroquest) {
		
		super();
		updateList(heroquest);
		
	}
	
	private void updateList(Boolean heroquest){
resize( WIDTH, PixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P );
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
		add( txtTitle );
		
		Component content = new Component();
		
		heroquest1 = heroquest;
		
		
		
		float pos = 0;
		items.clear();
		for (Quests.Quest rec : (heroquest == true) ? Quests.heroquests : Quests.quests) {
			
			ListItem item = new ListItem(rec);
			item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			pos += item.height();			
		}
		
		content.setSize( WIDTH, pos );
		
		list = new ScrollPane( content ){
			@Override
			public void onClick( float x, float y ) {
				int size = items.size();
				for (int i=0; i < size; i++) {
					if (items.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add( list );
		list.setRect( 0, txtTitle.height() + 1, WIDTH, height - txtTitle.height() );
		
		
		
	}
	
	private static class ListItem extends Component {
		
		private BitmapText feature;
		private BitmapText depth;
		
		private Image icon;
		private Quests.Quest quest;
		
		public ListItem( Quests.Quest f ) {
			super();
			quest = f;
			feature.text( f.title );
			feature.measure();
			
			depth.text(Integer.toString(f.type1));
			depth.measure();
			
		}
		
		@Override
		protected void createChildren() {
			feature = PixelScene.createText( 9 );
			add( feature );
			
			depth = new BitmapText( PixelScene.font1x );
			add( depth );
			
			icon = Icons.get( Icons.DEPTH );
			add( icon );
		}
		
		@Override
		protected void layout() {
			
			icon.x = width - icon.width;
			
			depth.x = icon.x - 1 - depth.width();
			depth.y = PixelScene.align( y + (height - depth.height()) / 2 );
			
			icon.y = depth.y - 1;
			
			feature.y = PixelScene.align( depth.y + depth.baseLine() - feature.baseLine() );
		}
		
		public boolean onClick(float x, float y){
			if(inside(x,y)){
				GameScene.show(new WndInfoQuest(quest, heroquest1));
				return true;
			} else{
				return false;
			}
		}
	}
	
}
