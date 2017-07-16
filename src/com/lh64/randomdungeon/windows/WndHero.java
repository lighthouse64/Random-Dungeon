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

import java.io.IOException;
import java.util.Locale;

import com.lh64.gltextures.SmartTexture;
import com.lh64.gltextures.TextureCache;
import com.lh64.noosa.BitmapText;
import com.lh64.noosa.Group;
import com.lh64.noosa.Image;
import com.lh64.noosa.TextureFilm;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.Quests;
import com.lh64.randomdungeon.Statistics;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.actors.buffs.Hunger;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.BadgesList;
import com.lh64.randomdungeon.ui.BuffIndicator;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.ScrollPane;
import com.lh64.randomdungeon.utils.Utils;

public class WndHero extends WndTabbed {
	
	public static boolean viewbadge;
	private static final String TXT_STATS	= "Stats";
	private static final String TXT_BUFFS	= "Buffs";
	
	private static final String TXT_EXP		= "Experience";
	private static final String TXT_STR		= "Strength";
	private static final String TXT_HEALTH	= "Health";
	private static final String TXT_HUNGER  = "Satiety";
	private static final String TXT_GOLD	= "Gold Collected";
	
	private static final int WIDTH		= 120;
	private static final int HEIGHT     = 134;
	private static final int TAB_WIDTH	= 40;
	
	private StatsTab stats;
	private BuffsTab buffs;
	private BadgesTab badges;
	
	private SmartTexture icons;
	private TextureFilm film;
	
	public WndHero() {
		
		super();
		
		icons = TextureCache.get( Assets.BUFFS_LARGE );
		film = new TextureFilm( icons, 16, 16 );
		
		stats = new StatsTab();
		add( stats );
		
		buffs = new BuffsTab();
		add( buffs );
		
		badges = new BadgesTab();
		add(badges);
		
		add( new LabeledTab( TXT_STATS ) {
			protected void select( boolean value ) {
				super.select( value );
				stats.visible = stats.active = selected;
			};
			
		} );
		add( new LabeledTab( TXT_BUFFS ) {
			protected void select( boolean value ) {
				super.select( value );
				buffs.visible = buffs.active = selected;
			};
		} );
		add( new LabeledTab( "Badges"){
			protected void select(boolean value) {
				super.select(value);
				badges.visible = badges.active = selected;
				viewbadge = false;
			}
		});
		for (Tab tab : tabs) {
			tab.setSize( TAB_WIDTH, tabHeight() );
		}
		
		resize( WIDTH, (int)Math.max( badges.height(), buffs.height() ) );
		
		select( 0 );
	}
	
	private class StatsTab extends Group {
		
		private static final String TXT_TITLE		= "Level %d %s";
		private static final String TXT_CATALOGUS	= "Catalogues";
		private static final String TXT_JOURNAL		= "Journal";
		
		private static final int GAP = 5;
		
		private float pos;
		
		public StatsTab() {
			viewbadge = false;
			Hero hero = Dungeon.hero; 
			try {
				Dungeon.loadName("Name");
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			BitmapText title = PixelScene.createText( 
				Utils.format( Dungeon.name + ": "+ TXT_TITLE, hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ), 9 );
			title.hardlight( Dungeon.color );
			title.measure();
			add( title );
			
			RedButton btnCatalogus = new RedButton( TXT_CATALOGUS ) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show( new WndCatalogus() );
				}
			};
			btnCatalogus.setRect( 0, title.y + title.height(), btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
			add( btnCatalogus );
			
			RedButton btnJournal = new RedButton( TXT_JOURNAL ) {
				@Override
				protected void onClick() {
					GameScene.show( new WndJournal() );
				}
			};
			btnJournal.setRect( 
				btnCatalogus.right() + 1, btnCatalogus.top(), 
				btnJournal.reqWidth() + 2, btnJournal.reqHeight() + 2 );
			add( btnJournal );
			
			pos = btnCatalogus.bottom() + GAP;
			
			RedButton btnBeastiary = new RedButton("Beastiary"){
				@Override
				protected void onClick(){
					GameScene.show(new WndBeastiary());
				}
			};
			btnBeastiary.setRect(btnJournal.right() + 9, btnJournal.top(), btnBeastiary.reqWidth() + 2, btnBeastiary.reqHeight() + 2);
			add(btnBeastiary);
			
			RedButton btnQuests = new RedButton("Quests"){
				@Override 
				protected void onClick(){
					GameScene.show(new WndQuestList(true));
				}
			};
			btnQuests.setRect(btnJournal.right() + 16, btnJournal.bottom() + 3, btnQuests.reqWidth() + 2, btnQuests.reqHeight() + 2);
			add(btnQuests);
			statSlot( TXT_STR, hero.STR() );
			statSlot( TXT_HEALTH, hero.HP + "/" + hero.HT );
			statSlot( TXT_HUNGER, 400 - Math.round(Hunger.level));
			statSlot( TXT_EXP, hero.exp + "/" + hero.maxExp() );

			pos += GAP;
			
			statSlot( TXT_GOLD, Statistics.goldCollected );
			statSlot( "Enemies slain ", Statistics.enemiesSlain);
			statSlot( "Quests Completed", Quests.completed);
			
			pos += GAP;
		}
		
		private void statSlot( String label, String value ) {
			
			BitmapText txt = PixelScene.createText( label, 8 );
			txt.y = pos;
			add( txt );
			
			txt = PixelScene.createText( value, 8 );
			txt.measure();
			txt.x = PixelScene.align( WIDTH * 0.65f );
			txt.y = pos;
			add( txt );
			
			pos += GAP + txt.baseLine();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		
	}
	
	private class BuffsTab extends Group {
		
		private static final int GAP = 2;
		
		private float pos;
		
		public BuffsTab() {
			for (Buff buff : Dungeon.hero.buffs()) {
				buffSlot( buff );
			}
		}
		
		private void buffSlot( Buff buff ) {
			
			int index = buff.icon();
			
			if (index != BuffIndicator.NONE) {
				
				Image icon = new Image( icons );
				icon.frame( film.get( index ) );
				icon.y = pos;
				add( icon );
				
				BitmapText txt = PixelScene.createText( buff.toString(), 8 );
				txt.x = icon.width + GAP;
				txt.y = pos + (int)(icon.height - txt.baseLine()) / 2;
				add( txt );
				
				pos += GAP + icon.height;
			}
		}
		
		
		
		public float height() {
			return pos;
		}
	}
	private class BadgesTab extends Group{
		private float pos;
		public BadgesTab() {
				super();
				
			camera = WndHero.this.camera;
			ScrollPane list = new BadgesList( false );
			add( list );
			list.setSize( WIDTH, HEIGHT );
			list.setPos((WIDTH/2)*(-1), -80);
			list.controller.x = (WIDTH/3)*(-1);
			
			
		pos += 2 + list.height();
		}
		public float height(){
			return pos;
		}
		
		
	}
	
}
