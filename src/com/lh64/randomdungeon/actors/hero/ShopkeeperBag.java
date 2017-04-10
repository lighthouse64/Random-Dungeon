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
package com.lh64.randomdungeon.actors.hero;

import java.util.Iterator;

import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.bags.Bag;
import com.lh64.randomdungeon.items.keys.Key;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class ShopkeeperBag implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 30;
	
	private Hero owner;
	
	public Bag backpack;	


	
	public ShopkeeperBag( Hero owner ) {
		this.owner = owner;
		
		backpack = new Bag() {{
			name = "Shop";
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;
	}
	

	
	public void storeInBundle( Bundle bundle ) {
	
		backpack.storeInBundle( bundle );
	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );

	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Key> T getKey( Class<T> kind, int depth ) {
		
		for (Item item : backpack) {
			if (item.getClass() == kind && ((Key)item).depth == depth) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public void countIronKeys() {
	
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {
	
		for (Item item : backpack) {
			item.cursedKnown = true;
		}
	}
	
	public void uncurseEquipped() {
		
	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}
	
	public void resurrect() {
		for (Item item : backpack.items.toArray( new Item[0])) {
				item.detachAll( backpack );
	}
	}
	
	public int charge( boolean full) {
		
		int count = 0;
		
		
		return count;
	}
	
	public int discharge() {
		
		int count = 0;
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator(); 
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		
		
		
		@Override
		public boolean hasNext() {
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
			default:
				backpackIterator.remove();
			}
		}
	}
}
