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
package com.lh64.randomdungeon;

import java.util.ArrayList;

import com.lh64.randomdungeon.actors.Actor;
import com.lh64.randomdungeon.actors.mobs.Bestiary;
import com.lh64.randomdungeon.actors.mobs.Mob;
import com.lh64.randomdungeon.items.Generator;
import com.lh64.randomdungeon.items.Gold;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.levels.Level;
import com.lh64.randomdungeon.utils.GLog;
import com.lh64.utils.Bundlable;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class Quests {
	public static int completed = 0;
	
	public static class Quest implements Comparable<Quest>, Bundlable {
		public static final String ITEM =  "item";
		public static final String REWARD = "reward";
		public static final String INCOME = "income";
		public static final String TYPE = "questtype";
		public static final String GIVEN = "given";
		public static final String MOB = "MOB";
		public static final String DEPTH = "Mob depth";
		public static final String ADDED = "Added";
		public static final String KILLS = "kills";
		public static final String TIME = "time";
		public static final String REMOVE = "remove";
	
		
		public boolean given = false;
		public int type1;
		public Item item;
		public Mob mob;
		public int kills;
		public float time;
		public boolean added;
		public Item reward;
		public int income;
		public int depth;
		public String description;
		public String title;
		public boolean remove = false;
		public Quest(){
			
		}
		public Quest(int type){
			this.type1 = type;
			this.item = Generator.random();
			while (true){
			this.reward = Generator.random(Generator.Category.QUEST);
			if(this.reward.getClass() != this.item.getClass()){
				break;
			}
			}
			this.income = Random.Int(25,75);
			
			if(type1 == 1){
				title = "Item wanted";
				description = "Help! I've lost my " + item.name() + ", and I need a replacement.  I am offering " + reward.toString() + " for anyone who satisfies this request.";
				
			}
			else if (type1 == 2){
				mob = Bestiary.mob( 1 );
				kills = Random.Int(6,24);
				
				title = "Item Stolen";
				description = "A " + mob.name +  " stole off with my " + item.name() + "  I am offering " + reward.toString() + " for anyone who satisfies this request";
			}
			else if (type1 == 3){
				
				added = false;
				mob = Bestiary.mob( 1 );
				mob.name = "killer " + mob.name;
				mob.HT += 10;
				mob.HP += 10;
				mob.addDefense(Random.Int(1,3));
				
				
				
				title = "Kill the Mob";
				description = "A " + mob.name + " killed one of my fellow adventurer friends, and I want vengence on it!  " + "I'll be offering " + reward.toString() + " to anyone who can kill this beast.";
			
			}
		}
		
		public void tickQuest(){
			if(type1 == 1){
			Item check = Dungeon.hero.belongings.getItem(this.item.getClass());
			if(check != null ){
				
				Dungeon.hero.belongings.backpack.items.remove(check);
				Dungeon.gold += this.income;
				
				if(reward.getClass() == Gold.class){
					Dungeon.gold += Random.Int(25,75);
				} else{
				reward.collect();
				}
				
				remove = true;
				completed ++;
				Badges.validateQuestsCompleted();
				
				GLog.p("The item has been collected and magically transported back to the owner.");
			}
			} 
			else if(type1 == 2){
				Item check = Dungeon.hero.belongings.getItem(this.item.getClass());
				if(check != null && kills <=0 && Statistics.duration > time + 2){
					GLog.p("The mob has been killed and you have retrieved the item!");
					Dungeon.hero.belongings.backpack.items.remove(check);
					Dungeon.gold += this.income;
					if(reward.getClass() == Gold.class){
						Dungeon.gold += Random.Int(25,75);
					} else{
					reward.collect();
					}
					remove = true;
					completed++;
					Badges.validateQuestsCompleted();
				}
			}
			
			else if(type1 == 3){
				if(mob.isAlive() == false){
					Dungeon.gold += this.income;
					if(reward.getClass() == Gold.class){
						Dungeon.gold += Random.Int(25,75);
					} else{
					reward.collect();
					}
					GLog.p("You've killed the mob!");
					remove = true;
					completed++;
					Badges.validateQuestsCompleted();
				}
			}
		}
		
		public void addMob(Level level){
			if(heroquests.contains(this) && added != true && Dungeon.depth > 1){
				
				do {
					mob.pos = level.randomRespawnCell();
				} while (mob.pos == -1);
				level.mobs.add( mob );
				Actor.occupyCell( mob );
			added = true;
			}
			
		}
		public void mobProcess (int pos){
			kills--;
			if(kills <= 0){
				Dungeon.level.drop(item, pos);
				time = Statistics.duration;
			}
		}
		
		
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			item = null;
			item = (Item) bundle.get(ITEM);
			type1 = bundle.getInt(TYPE);
			reward = (Item) bundle.get(REWARD);
			income = bundle.getInt(INCOME);
			remove = bundle.getBoolean(REMOVE);
			if(type1 == 1){
				title = "Item wanted";
				description = "Help! I've lost my " + item.name() + ", and I need a replacement.";
			} else if(type1 == 2){
				title="Item Stolen";
				description = "A mob stole off with my " + item.name();
				mob = (Mob) bundle.get(MOB);
				kills = bundle.getInt(KILLS);
				time = bundle.getFloat(TIME);
			} 
			else if (type1 == 3){
				added = bundle.getBoolean(ADDED);
				mob = (Mob) bundle.get(MOB);
				mob.name = "killer " + mob.name;
				mob.HT += 10;
				mob.HP += 10;
				title = "Kill the Mob";
				description = "A " + mob.name + " killed one of my fellow adventurer friends, and I want vengence on it!  " + "I'll be offering " + reward.toString() + " to anyone who can kill this beast.";
				
			}
			
		}
		
		@Override
		public void storeInBundle(Bundle bundle) {
			bundle.put(REMOVE, remove);
			bundle.put(ITEM, item);
			bundle.put(TYPE,type1);
			bundle.put(REWARD, reward);
			bundle.put(INCOME, income);
			if(type1 == 2){
				
				bundle.put(MOB, mob);
				bundle.put(KILLS, kills);
				bundle.put(TIME, time);
			}
			else if(type1 == 3){
				bundle.put(MOB, mob);
				bundle.put(ADDED, added);
			}
			
		}

		@Override
		public int compareTo(Quest another) {
			return another.type1 - type1;
		}
		
		
	}
	
	public static ArrayList<Quest> quests = new ArrayList<Quest>();
	public static ArrayList<Quest> heroquests = new ArrayList<Quest>();
	
	public static final String QUESTS = "list of quests";
	public static final String HEROQUESTS = "Hero quests";
	
	public static void StoreQuestsInBundle(Bundle bundle){
		bundle.put(QUESTS,quests);
		bundle.put(HEROQUESTS, heroquests);
	}

	
	public static void RestoreQuestsFromBundle(Bundle bundle){
		quests = new ArrayList<Quest>();
		for( Bundlable quest : bundle.getCollection(QUESTS)){
			quests.add((Quest) quest);
		}
		heroquests = new ArrayList<Quest>();
		for ( Bundlable quest : bundle.getCollection(HEROQUESTS)){
			heroquests.add((Quest) quest);
		}
		
	}

	
	
}
