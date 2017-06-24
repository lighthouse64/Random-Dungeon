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
import com.lh64.noosa.ui.Component;

import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.actors.mobs.Albino;
import com.lh64.randomdungeon.actors.mobs.Bandit;
import com.lh64.randomdungeon.actors.mobs.Bat;
import com.lh64.randomdungeon.actors.mobs.Brute;
import com.lh64.randomdungeon.actors.mobs.CaveFlower;
import com.lh64.randomdungeon.actors.mobs.CaveSpider;
import com.lh64.randomdungeon.actors.mobs.Crab;
import com.lh64.randomdungeon.actors.mobs.CursePersonification;
import com.lh64.randomdungeon.actors.mobs.DM300;
import com.lh64.randomdungeon.actors.mobs.DungeonFish;
import com.lh64.randomdungeon.actors.mobs.FetidRat;
import com.lh64.randomdungeon.actors.mobs.Gnoll;
import com.lh64.randomdungeon.actors.mobs.Goo;
import com.lh64.randomdungeon.actors.mobs.MasterThief;
import com.lh64.randomdungeon.actors.mobs.Mimic;
import com.lh64.randomdungeon.actors.mobs.Mob;
import com.lh64.randomdungeon.actors.mobs.Piranha;
import com.lh64.randomdungeon.actors.mobs.Rat;
import com.lh64.randomdungeon.actors.mobs.RatPrince;
import com.lh64.randomdungeon.actors.mobs.SewerHorse;
import com.lh64.randomdungeon.actors.mobs.Shaman;
import com.lh64.randomdungeon.actors.mobs.Shielded;
import com.lh64.randomdungeon.actors.mobs.Skeleton;
import com.lh64.randomdungeon.actors.mobs.Spinner;
import com.lh64.randomdungeon.actors.mobs.Statue;
import com.lh64.randomdungeon.actors.mobs.Tengu;
import com.lh64.randomdungeon.actors.mobs.Thief;
import com.lh64.randomdungeon.actors.mobs.ToxicSludge;
import com.lh64.randomdungeon.actors.mobs.Wraith;
import com.lh64.randomdungeon.actors.mobs.npcs.Blacksmith;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.actors.mobs.npcs.RatKing;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.ui.ScrollPane;
import com.lh64.randomdungeon.ui.Window;


public class WndBeastiary extends Window {
	
	private static final int WIDTH		= 112;
	private static final int HEIGHT_P	= 160;
	private static final int HEIGHT_L	= 144;
	
	private static final int ITEM_HEIGHT	= 25;
	
	private static final String TXT_TITLE	= "Bestiary";
	
	private BitmapText txtTitle;
	private ScrollPane list;
	private ArrayList<ListItem> items = new ArrayList<WndBeastiary.ListItem>();
	public WndBeastiary() {
		
		super();
		
		
		updateList();
		
		
		
		
	}
	
	
	private void updateList(){
	Mob.Mobs.removeAll(Mob.Mobs);	
	Mob.Mobs.add(new Albino());
	Mob.Mobs.add(new Bandit());
	Mob.Mobs.add(new Bat());
	Mob.Mobs.add(new Brute());
	Mob.Mobs.add(new CaveFlower());
	Mob.Mobs.add(new CaveSpider());
	Mob.Mobs.add(new Crab());
	Mob.Mobs.add(new CursePersonification());
	Mob.Mobs.add(new DM300());
	Mob.Mobs.add(new DungeonFish());
	Mob.Mobs.add(new FetidRat());
	Mob.Mobs.add(new Ghost());
	Mob.Mobs.add(new Gnoll());
	Mob.Mobs.add(new Goo());
	Mob.Mobs.add(new MasterThief());
	Mob.Mobs.add(new Mimic());
	Mob.Mobs.add(new Piranha());
	Mob.Mobs.add(new Rat());
	Mob.Mobs.add(new RatKing());
	Mob.Mobs.add(new RatPrince());
	Mob.Mobs.add(new SewerHorse());
	Mob.Mobs.add(new Shaman());
	Mob.Mobs.add(new Shielded());
	Mob.Mobs.add(new Skeleton());
	Mob.Mobs.add(new Spinner());
	Mob.Mobs.add(new Statue());
	Mob.Mobs.add(new Tengu());
	Mob.Mobs.add(new Thief());
	Mob.Mobs.add(new ToxicSludge());
	Mob.Mobs.add(new Blacksmith());
	Mob.Mobs.add(new Wraith());

		
resize( WIDTH, PixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P );
		
txtTitle = PixelScene.createText( TXT_TITLE, 11 );
txtTitle.hardlight( Window.TITLE_COLOR );
txtTitle.measure();
txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
add( txtTitle );
list = new ScrollPane( new Component() ){
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

list.setRect( 0, txtTitle.height(), WIDTH, height - txtTitle.height() );
		Component content = list.content();
		float pos = 0;
		items.clear();
		content.clear();
		for(Mob mob : Mob.Mobs){
			
			ListItem item = new ListItem( mob );
			item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			pos += item.height();
		}
		
		content.setSize( WIDTH, pos );
		
		
	}
	private static class ListItem extends Component {
		
		private BitmapText feature;
		private BitmapText depth;
		private Mob mob1;
		
		private CharSprite icon;
		
		public ListItem( Mob mob ) {
			super();
			mob1 = mob;
			String temptext = mob.name;
			
			icon = mob.sprite();
			if(mob.discovered != true){
				icon.hardlight(0x000000);
				temptext = "???";
			}
			
			feature.text( temptext );
			feature.measure();
			add( icon );
		}
		
		@Override
		protected void createChildren() {
			feature = PixelScene.createText( 10 );
			add( feature );
			
			depth = new BitmapText( PixelScene.font2x );
			add( depth );
			
			
			
		}
		
		@Override
		protected void layout() {
			
			icon.x = width - icon.width;
			
			depth.x = icon.x - 1 - depth.width();
			depth.y = PixelScene.align( y + (height - depth.height()) / 2 );
			
			icon.y = depth.y - 1;
			
			feature.y = PixelScene.align( depth.y + depth.baseLine() - feature.baseLine() );
		}
		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				GameScene.show( new WndInfoMob( mob1, true ) );
				return true;
			} else {
				return false;
			}
		}
	}
}
