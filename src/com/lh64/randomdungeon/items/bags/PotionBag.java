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
package com.lh64.randomdungeon.items.bags;




import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.potions.Potion;
import com.lh64.randomdungeon.sprites.ItemSpriteSheet;


public class PotionBag extends Bag {

	{
		name = "potion bag";
		image = ItemSpriteSheet.POTIONBAG;
		
		size = 20;
	}
	
	@Override
	public boolean grab( Item item ) {
		
		return item instanceof Potion;
		}
	@Override
	public boolean collect(Bag container) {
		if(super.collect(container)){
		
		return true;
		} else{
			return false;
		}
	}
	
	
	@Override
	public void onDetach( ) {
		if(Dungeon.storage == true || Dungeon.ShopkeeperBag == true){
		for (Item item : Dungeon.hero.belongings.backpack.items.toArray( new Item[0] )) {
			if (grab( item ) ) {
				item.detachAll( Dungeon.hero.belongings.backpack );
				item.collect( this );
			}
		}
		}
	}
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"It saves space in your bag for potions.";
	}
}
