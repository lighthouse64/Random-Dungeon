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
package com.lh64.randomdungeon.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.lh64.noosa.audio.Sample;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Badges;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.Actor;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.SnipersMark;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.actors.mobs.npcs.Chest;
import com.lh64.randomdungeon.effects.Speck;
import com.lh64.randomdungeon.items.bags.Bag;
import com.lh64.randomdungeon.items.rings.RingOfHaggler;
import com.lh64.randomdungeon.items.weapon.missiles.MissileWeapon;
import com.lh64.randomdungeon.mechanics.Ballistica;
import com.lh64.randomdungeon.scenes.CellSelector;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.ItemSprite;
import com.lh64.randomdungeon.sprites.MissileSprite;
import com.lh64.randomdungeon.ui.QuickSlot;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.randomdungeon.utils.Utils;
import com.lh64.utils.Bundlable;
import com.lh64.utils.Bundle;
import com.lh64.utils.Callback;


public class Item implements Bundlable {

	private static final String TXT_PACK_FULL	= "Your pack is too full for the %s";
	

	
	private static final String TXT_TO_STRING		= "%s";
	private static final String TXT_TO_STRING_X		= "%s x%d";
	private static final String TXT_TO_STRING_LVL	= "%s%+d";
	private static final String TXT_TO_STRING_LVL_X	= "%s%+d x%d";
	
	
	protected static final float TIME_TO_THROW		= 1.0f;
	protected static final float TIME_TO_PICK_UP	= 1.0f;
	protected static final float TIME_TO_DROP		= 0.5f;
	
	public static final String AC_DROP		= "DROP";
	public static final String AC_THROW		= "THROW";
	public static final String AC_STORE     = "STORE";
	public static final String AC_RETRIEVE  = "RETRIEVE";
	public static  String AC_BUY       = "";
	public static  String AC_BUY1      = "";
	
	public String defaultAction;
	
	protected String name = "smth";
	protected int image = 0;
	public static int itemprice;

	

	public boolean stackable = false;
	protected int quantity = 1;
	
	private int level = 0;

	public boolean levelKnown = false;
	
	public boolean cursed;
	public boolean cursedKnown;
	
	public boolean unique = false;
	
	private static Comparator<Item> itemComparator = new Comparator<Item>() {	
		@Override
		public int compare( Item lhs, Item rhs ) {
			return Generator.Category.order( lhs ) - Generator.Category.order( rhs );
		}
	};
	
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = new ArrayList<String>();
		if (Dungeon.storage == true ){
			actions.add(AC_RETRIEVE);
		}
		if(Dungeon.storage == false && Dungeon.ShopkeeperBag==false){
			if(Dungeon.visible[Chest.cpos]){
			actions.add(AC_STORE);
			}
			actions.add( AC_DROP );
			actions.add( AC_THROW );
			}
		else if(Dungeon.ShopkeeperBag == true && Dungeon.storage ==false){
			if(quantity == 1){
				if (Dungeon.hero.buff( RingOfHaggler.Haggling.class ) != null && itemprice >= 2) {
					AC_BUY = "Buy for " + price()*5 + " gold";
				} else{
			AC_BUY = "Buy for " + price()*10 + " gold";
				}
			actions.add(AC_BUY);
			} else{
				if (Dungeon.hero.buff( RingOfHaggler.Haggling.class ) != null && itemprice >= 2) {
					AC_BUY = "Buy all for " + price()*5 + " gold";
					AC_BUY1 = "Buy 1 for " + price()*5/quantity() + " gold";
				} else{
			AC_BUY = "Buy all for " + price()*10 + " gold";
			AC_BUY1 = "Buy 1 for " + price()*10/quantity() + " gold";
				}
			actions.add(AC_BUY);
		actions.add(AC_BUY1);
				
				
			}
		}
		
		
		return actions;
		}
	
		
	
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack )) {
			
			GameScene.pickUp( this );
			Sample.INSTANCE.play( Assets.SND_ITEM );
			hero.spendAndNext( TIME_TO_PICK_UP );
			return true;
			
		} else {
			return false;
		}
}
	public void doStore (Hero hero){
		ArrayList<Item> items = Dungeon.hero.storage.backpack.items;
		if(items.size()< Dungeon.hero.storage.backpack.size){
		collect(hero.storage.backpack);
		detachAll(hero.belongings.backpack);
		}else{
			GLog.n("Your storage is full.");
		}
	}
	public void doRetrieve (Hero hero){
		ArrayList<Item> items = Dungeon.hero.belongings.backpack.items;
		if(items.size() < Dungeon.hero.belongings.backpack.size){
		collect(hero.belongings.backpack);
		detachAll(hero.storage.backpack);
		} else{
			GLog.n("Your backpack is full.");
		}
	}
	
	public void doBuy1(Hero hero){
		ArrayList<Item> items = Dungeon.hero.belongings.backpack.items;
		itemprice = price()*10/quantity();
		if (Dungeon.hero.buff( RingOfHaggler.Haggling.class ) != null && itemprice >= 2) {
			itemprice /= 2;
		}
		if (Dungeon.gold < itemprice){
			GLog.n("You're too poor to buy that.");
			Dungeon.ShopkeeperBag = false;
			}
		else if(items.size() >= Dungeon.hero.belongings.backpack.size){
			GLog.n("Other items fill your back at the moment.");
		}
		else{
			collect(hero.belongings.backpack);
			detach(hero.shopkeeperbag.backpack);
			Dungeon.gold -= itemprice;
			GLog.p("Please come by and shop again!");
			Dungeon.ShopkeeperBag = false;
			}
	}
	
	public void doBuy(Hero hero){
		ArrayList<Item> items = Dungeon.hero.belongings.backpack.items;
		itemprice = price()*10;
		if (Dungeon.hero.buff( RingOfHaggler.Haggling.class ) != null && itemprice >= 2) {
			itemprice /= 2;
		}
		
			if (Dungeon.gold < itemprice){
			GLog.n("You're too poor to buy that.");
			Dungeon.ShopkeeperBag = false;
			}
			else if(items.size() >= Dungeon.hero.belongings.backpack.size){
				GLog.n("Other items fill your back at the moment.");
			}else{
			collect(hero.belongings.backpack);
			detachAll(hero.shopkeeperbag.backpack);
			Dungeon.gold -= itemprice;
			GLog.p("Please come by and shop again!");
			Dungeon.ShopkeeperBag = false;
			}
		
	}
	public void doDrop( Hero hero ) {	
		hero.spendAndNext( TIME_TO_DROP );			
		Dungeon.level.drop( detachAll( hero.belongings.backpack ), hero.pos ).sprite.drop( hero.pos );	
	}
	public void doThrow( Hero hero ) {
		GameScene.selectCell( thrower );
	}
	
	public void execute( Hero hero, String action ) {
		
		curUser = hero;
		curItem = this;
		
		if (action.equals( AC_DROP )) {
			
			doDrop( hero );
			
		} else if (action.equals( AC_THROW )) {
			
			doThrow( hero );
			
		}
		else if(action.equals(AC_STORE)){
			doStore(hero);
		}
		else if(action.equals(AC_RETRIEVE)){
			doRetrieve(hero);
		} 
		else if (action.equals(AC_BUY)){
			doBuy(hero);
		}
		else if (action.equals(AC_BUY1)){
			doBuy1(hero);
		}
	}
	
	public void execute( Hero hero ) {
		execute( hero, defaultAction );
	}
	
	protected void onThrow( int cell ) {
		Heap heap = Dungeon.level.drop( this, cell );
		if (!heap.isEmpty()) {
			heap.sprite.drop( cell );
		}
	}
	
	public boolean collect( Bag container ) {
		
		ArrayList<Item> items = container.items;
		
		if (items.contains( this )) {
			return true;
		}
		
		for (Item item:items) {
			if (item instanceof Bag && ((Bag)item).grab( this )) {
				return collect( (Bag)item );
			}
		}
		
		if (stackable) {
			
			Class<?>c = getClass();
			for (Item item:items) {
				if (item.getClass() == c) {
					item.quantity += quantity;
					item.updateQuickslot();
					return true;
				}
			}	
		}
		
		if (items.size() < container.size) {
			
			if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
				Badges.validateItemLevelAquired( this );
			}
			
			items.add( this );	
			QuickSlot.refresh();
			Collections.sort( items, itemComparator );
			return true;
			
		} else {
			if(Dungeon.ShopkeeperBag == false && Dungeon.storage == false){
			GLog.n( TXT_PACK_FULL, name() );
			}
			return false;
			
		}
	}
	
	public boolean collect() {
		return collect( Dungeon.hero.belongings.backpack );
	}
	
	public final Item detach( Bag container ) {
		
		if (quantity <= 0) {
			
			return null;
			
		} else
		if (quantity == 1) {

			return detachAll( container );
			
		} else {
			
			quantity--;
			updateQuickslot();
			
			try { 
				Item detached = getClass().newInstance();
				detached.onDetach( );
				return detached;
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public final Item detachAll( Bag container ) {
		
		for (Item item : container.items) {
			if (item == this) {
				container.items.remove( this );
				item.onDetach( );
				QuickSlot.refresh();
				return this;
			} else if (item instanceof Bag) {
				Bag bag = (Bag)item;
				if (bag.contains( this )) {
					return detachAll( bag );
				}
			}
		}
		
		return this;
	}
	
	protected void onDetach( ) {
	}
	
	public int level() {
		return level;
	}
	
	public void level( int value ) {
		level = value;
	}
	
	public int effectiveLevel() {
		return level;
	}
	
	public Item upgrade() {
		
		cursed = false;
		cursedKnown = true;
		
		this.level++;
	
		
		return this;
	}
	
	final public Item upgrade( int n ) {
		for (int i=0; i < n; i++) {
			upgrade();
		}
		
		return this;
	}
	
	public Item degrade() {

		 this.level--;
		 return this;
	}
	
	final public Item degrade( int n ) {
		for (int i=0; i < n; i++) {
			degrade();
		}
		
		return this;
	}
	
	public void use() {
		
	}
	
	
	
	
	
	
	
	public void polish() {
		
	}
	
	
	
	
	public int visiblyUpgraded() {
		return levelKnown ? level : 0;
	}
	
	public boolean visiblyCursed() {
		return cursed && cursedKnown;
	}
	
	
	public boolean isUpgradable() {
		return true;
	}
	
	public boolean isIdentified() {
		return levelKnown && cursedKnown;
	}
	
	public boolean isEquipped( Hero hero ) {
		return false;
	}
	
	public Item identify() {
		
		levelKnown = true;
		cursedKnown = true;
		
		return this;
	}
	
	public static void evoke( Hero hero ) {
		hero.sprite.emitter().burst( Speck.factory( Speck.EVOKE ), 5 );
	}
	
	@Override
	public String toString() {
		
		if (levelKnown && level != 0) {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_LVL_X, name(), level, quantity );
			} else {
				return Utils.format( TXT_TO_STRING_LVL, name(), level );
			}
		} else {
			if (quantity > 1) {
				return Utils.format( TXT_TO_STRING_X, name(), quantity );
			} else {
				return Utils.format( TXT_TO_STRING, name() );
			}
		}
	}
	
	public String name() {
		return name;
	}
	
	public final String trueName() {
		return name;
	}
	
	public int image() {
		return image;
	}
	
	public ItemSprite.Glowing glowing() {
		return null;
	}
	
	public String info() {
		return desc();
	}
	
	public String desc() {
		return "";
	}
	
	public int quantity() {
		return quantity;
	}
	
	public void quantity( int value ) {
		quantity = value;
	}
	
	public int price() {
		return 0;
	}
	
	public int considerState( int price ) {
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level > 0) {
				price *= (level + 1);
				
			} else if (level < 0) {
				price /= (1 - level);
			}
		}
		if (price < 1) {
			price = 1;
		}
		
		return price;
	}
	
	public static Item virtual( Class<? extends Item> cl ) {
		try {
			
			Item item = (Item)cl.newInstance();
			item.quantity = 0;
			return item;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public Item random() {
		return this;
	}
	
	public String status() {
		return quantity != 1 ? Integer.toString( quantity ) : null;
	}
	
	public void updateQuickslot() {
		
		if (stackable) {
			Class<? extends Item> cl = getClass();
			if (QuickSlot.primaryValue == cl || QuickSlot.secondaryValue == cl) {
				QuickSlot.refresh();
			}
		} else if (QuickSlot.primaryValue == this || QuickSlot.secondaryValue == this) {
			QuickSlot.refresh();
		}
	}
	
	private static final String QUANTITY		= "quantity";
	private static final String LEVEL			= "level";
	private static final String LEVEL_KNOWN		= "levelKnown";
	private static final String CURSED			= "cursed";
	private static final String CURSED_KNOWN	= "cursedKnown";

	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( QUANTITY, quantity );
		bundle.put( LEVEL, level );
		bundle.put( LEVEL_KNOWN, levelKnown );
		bundle.put( CURSED, cursed );
		bundle.put( CURSED_KNOWN, cursedKnown );
		
		QuickSlot.save( bundle, this );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		quantity	= bundle.getInt( QUANTITY );
		levelKnown	= bundle.getBoolean( LEVEL_KNOWN );
		cursedKnown	= bundle.getBoolean( CURSED_KNOWN );
		
		int level = bundle.getInt( LEVEL );
		if (level > 0) {
			upgrade( level );
		} else if (level < 0) {
			degrade( -level );
		}
		
		cursed	= bundle.getBoolean( CURSED );
		
		
		
		QuickSlot.restore( bundle, this );
	}
	
	public void cast( final Hero user, int dst ) {
		
		final int cell = Ballistica.cast( user.pos, dst, false, true );
		user.sprite.zap( cell );
		user.busy();
		
		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );
		
		Char enemy = Actor.findChar( cell );
		QuickSlot.target( this, enemy );
		
		// FIXME!!!
		float delay = TIME_TO_THROW;
		if (this instanceof MissileWeapon) {
			delay *= ((MissileWeapon)this).speedFactor( user );
			if (enemy != null) {
				SnipersMark mark = user.buff( SnipersMark.class );
				if (mark != null) {
					if (mark.object == enemy.id()) {
						delay *= 0.5f;
					}
					user.remove( mark );
				}
			}
		}
		final float finalDelay = delay;
		
		((MissileSprite)user.sprite.parent.recycle( MissileSprite.class )).
			reset( user.pos, cell, this, new Callback() {			
				@Override
				public void call() {
					Item.this.detach( user.belongings.backpack ).onThrow( cell );
					user.spendAndNext( finalDelay );
				}
			} );
	}
	
	protected static Hero curUser = null;
	protected static Item curItem = null;
	protected static CellSelector.Listener thrower = new CellSelector.Listener() {	
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curItem.cast( curUser, target );
			}
		}
		@Override
		public String prompt() {
			return "Choose direction of throw";
		}
	};
}