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
package com.lh64.randomdungeon.items.scrolls;

import java.util.ArrayList;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.buffs.Blindness;
import com.lh64.randomdungeon.actors.buffs.Invisibility;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.HeroSprite;
import com.lh64.randomdungeon.sprites.ItemSpriteSheet;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.randomdungeon.windows.WndConfirmAscend;

public class ScrollOfReturn extends Item {

	private static final String TXT_BLINDED	= "You can't read a scroll while blinded";
	
	public static final String AC_READ	= "READ";
	
	protected static final float TIME_TO_READ	= 1f;
	
	{
		name = "Scroll of Return";
		image = ItemSpriteSheet.SCROLL_RETURN;
		
		stackable = true;		
		defaultAction = AC_READ;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			
			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
			} else {
				curUser = hero;
				curItem = detach( hero.belongings.backpack );
				doRead();
			}
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	private void doRead() {
		GameScene.flash( 0xFF6644 );
		
		Invisibility.dispel();
		((HeroSprite)curUser.sprite).read();
		Dungeon.gold -= Dungeon.gold/4;
		WndConfirmAscend.go = true;
		WndConfirmAscend.reset(false);
		
		
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String desc() {
		return
			"Upon reading this scroll, the player will be whisked back to the hub, without losing any items.  However, 1/4 of his or her gold will be lost, and the level theme will reset.";
	}
	
	@Override
	public int price() {
		return 80 * quantity;
	}
}
