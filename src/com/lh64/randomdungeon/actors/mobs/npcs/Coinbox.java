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
package com.lh64.randomdungeon.actors.mobs.npcs;

import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.CoinboxSprite;
import com.lh64.randomdungeon.windows.WndStoreCoins;

public class Coinbox extends NPC {

	{
		name = "Coin Storage";
		spriteClass = CoinboxSprite.class;
		
		state = NOTHING;
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public boolean act(){
		throwItem();
		spend( TICK );
		return true;
	}
	
	@Override
	public float speed() {
		return 0f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		GameScene.show(new WndStoreCoins());
	}
	
	@Override
	public String description() {
		return 
			"These coins stay put.";
	}
}
