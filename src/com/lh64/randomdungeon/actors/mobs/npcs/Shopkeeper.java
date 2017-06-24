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

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.effects.CellEmitter;
import com.lh64.randomdungeon.effects.particles.ElmoParticle;
import com.lh64.randomdungeon.items.Ankh;
import com.lh64.randomdungeon.items.Bomb;
import com.lh64.randomdungeon.items.Generator;
import com.lh64.randomdungeon.items.Heap;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.Weightstone;
import com.lh64.randomdungeon.items.bags.PotionBag;
import com.lh64.randomdungeon.items.bags.ScrollHolder;
import com.lh64.randomdungeon.items.bags.SeedPouch;
import com.lh64.randomdungeon.items.bags.WandHolster;
import com.lh64.randomdungeon.items.food.OverpricedRation;
import com.lh64.randomdungeon.items.potions.PotionOfHealing;
import com.lh64.randomdungeon.items.scrolls.ScrollOfIdentify;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.ShopkeeperSprite;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.randomdungeon.windows.WndBag;
import com.lh64.randomdungeon.windows.WndPurchaseItem;

import com.lh64.randomdungeon.windows.WndTradeItem;
import com.lh64.utils.Random;

public class Shopkeeper extends NPC {

	{
		name = "shopkeeper";
		spriteClass = ShopkeeperSprite.class;
		
	}

	@Override
	protected boolean act() {
		
		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		GLog.w("Please don't hit me.");
	}
	
	@Override
	public void add( Buff buff ) {
		
	}
	
	protected void flee() {
		for (Heap heap: Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				
			}
		}
		
		destroy();
		
		sprite.killAndErase();
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return 
			"This stout guy looks more appropriate for a trade district in some large city " +
			"than for a dungeon. His prices explain why he prefers to do business here.";
	}
	
	public static WndBag sell() {
		Dungeon.storage=false;
		Dungeon.ShopkeeperBag=false;
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, "Select an item to sell" );
	}
	
	public static void buy(){
		
	}
	public static void shopinit(){
		Dungeon.storage=false;
		Dungeon.ShopkeeperBag=true;
		Dungeon.hero.shopkeeperbag.resurrect();
		Item item = new Bomb();
		item.collect(Dungeon.hero.shopkeeperbag.backpack);
		Item scroll = Generator.random(Generator.Category.SCROLL);
		scroll.collect(Dungeon.hero.shopkeeperbag.backpack);
		new ScrollOfIdentify().collect(Dungeon.hero.shopkeeperbag.backpack);
		item = Generator.random(Generator.Category.POTION);
		item.collect(Dungeon.hero.shopkeeperbag.backpack);
		new Ankh().collect(Dungeon.hero.shopkeeperbag.backpack);
		item.collect(Dungeon.hero.shopkeeperbag.backpack);
		for (int j = 1; j < 3; j++){
		new OverpricedRation().collect(Dungeon.hero.shopkeeperbag.backpack);
		}
		for (int i = 1; i<Random.Int(2,4); i++){
			item = new PotionOfHealing();
			item.collect(Dungeon.hero.shopkeeperbag.backpack);
}
		if(Dungeon.depth != Dungeon.shop3){
		Generator.random(Generator.Category.WAND).collect(Dungeon.hero.shopkeeperbag.backpack);
		}
		new Weightstone().collect(Dungeon.hero.shopkeeperbag.backpack);
		item = Generator.random(Generator.Category.RING);
		if(Random.Int(0,3)== 0){
			item.degrade(Random.Int(0,4));
			item.cursed = true;
			item.cursedKnown = false;
		}
		item.collect(Dungeon.hero.shopkeeperbag.backpack);
		Dungeon.initshop = false;
	}
	public static void playerbuy(){
		Dungeon.storage = false;
		Dungeon.ShopkeeperBag = true;
		if(Dungeon.depth > 1){
			Dungeon.initshop = false;
		}
		if(Dungeon.initshop == true){
		shopinit();
		Item cheeckbag = Dungeon.hero.belongings.getItem(PotionBag.class);
		if(cheeckbag == null){
			Item newbag = new PotionBag();
			newbag.collect(Dungeon.hero.shopkeeperbag.backpack);
		}
		Item holster = Dungeon.hero.belongings.getItem(WandHolster.class);
		if(holster == null){
			new WandHolster().collect(Dungeon.hero.shopkeeperbag.backpack);
		}
		Dungeon.initshop = false;
		} 
		else if(Dungeon.shop1visit == false && Dungeon.depth == Dungeon.shop1){
			shopinit();
			Item checkpouch = Dungeon.hero.belongings.getItem(SeedPouch.class);
			if(checkpouch == null){
				Item getholder = new SeedPouch();
				getholder.collect(Dungeon.hero.shopkeeperbag.backpack);
			}
			  
			Dungeon.shop1visit = true;
		}
		else if(Dungeon.shop2visit == false && Dungeon.depth == Dungeon.shop2){
			shopinit();
			Item scrollholder = Dungeon.hero.belongings.getItem(ScrollHolder.class);
			if(scrollholder == null){
				Item getholder = new ScrollHolder();
				getholder.collect(Dungeon.hero.shopkeeperbag.backpack);
			}
			Dungeon.shop2visit = true;
		}
		else if(Dungeon.shop3visit == false && Dungeon.depth == Dungeon.shop3){
			shopinit();
			Item holster = Dungeon.hero.belongings.getItem(WandHolster.class);
			if(holster == null){
				new WandHolster().collect(Dungeon.hero.shopkeeperbag.backpack);
			}
			
			Dungeon.shop3visit = true;
		}
		else if(Dungeon.shop4visit == false && Dungeon.depth == 14){
			shopinit();
			Dungeon.shop4visit = true;
		}
		GameScene.show(new WndBag(Dungeon.hero.shopkeeperbag.backpack,null,WndBag.Mode.BUY,null));
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};
public static void choose(){
	
	GameScene.show(new WndPurchaseItem());
}

	@Override
	public void interact() {
		choose();
		
		
	}
}
