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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import com.lh64.noosa.Game;
import com.lh64.randomdungeon.actors.Actor;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Amok;
import com.lh64.randomdungeon.actors.buffs.Hunger;
import com.lh64.randomdungeon.actors.buffs.Light;
import com.lh64.randomdungeon.actors.buffs.Rage;
import com.lh64.randomdungeon.actors.hero.Hero;
import com.lh64.randomdungeon.actors.hero.HeroClass;
import com.lh64.randomdungeon.actors.mobs.npcs.Blacksmith;
import com.lh64.randomdungeon.actors.mobs.npcs.Ghost;
import com.lh64.randomdungeon.actors.mobs.npcs.Imp;
import com.lh64.randomdungeon.actors.mobs.npcs.Wandmaker;
import com.lh64.randomdungeon.items.Ankh;
import com.lh64.randomdungeon.items.Item;
import com.lh64.randomdungeon.items.potions.Potion;
import com.lh64.randomdungeon.items.rings.Ring;
import com.lh64.randomdungeon.items.scrolls.Scroll;
import com.lh64.randomdungeon.items.wands.Wand;
import com.lh64.randomdungeon.levels.CavesBossLevel;
import com.lh64.randomdungeon.levels.CavesLevel;
import com.lh64.randomdungeon.levels.DeadEndLevel;
import com.lh64.randomdungeon.levels.HubLevel;
import com.lh64.randomdungeon.levels.LastLevel;
import com.lh64.randomdungeon.levels.Level;
import com.lh64.randomdungeon.levels.PrisonBossLevel;
import com.lh64.randomdungeon.levels.PrisonLevel;
import com.lh64.randomdungeon.levels.Room;
import com.lh64.randomdungeon.levels.SewerBossLevel;
import com.lh64.randomdungeon.levels.SewerLevel;
import com.lh64.randomdungeon.levels.TownLevel;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.StartScene;
import com.lh64.randomdungeon.ui.QuickSlot;
import com.lh64.randomdungeon.utils.BArray;
import com.lh64.randomdungeon.utils.Utils;
import com.lh64.randomdungeon.windows.WndClickerGame;
import com.lh64.randomdungeon.windows.WndResurrect;
import com.lh64.utils.Bundlable;
import com.lh64.utils.Bundle;
import com.lh64.utils.PathFinder;
import com.lh64.utils.Random;
import com.lh64.utils.SparseArray;

import com.lh64.randomdungeon.scenes.InterlevelScene;
import com.lh64.randomdungeon.scenes.InterlevelScene.Mode;

public class Dungeon {
	

	public static int scrollsOfEnchantment;
	public static boolean dewVial;		// true if the dew vial can be spawned

	
	public static int challenges;
	public static boolean storage =false;
	public static boolean ShopkeeperBag = false;
	public static boolean zerocheck = true;
	public static boolean resethub;
	
	public static Hero hero;
	public static Level level;
	
	public static boolean changename = true;
	public static String name = "";
	public static int color = 0xFFFFFF;
	
	public static int depth;
	public static boolean initshop = true;
	public static int shop1;
	public static boolean shop1visit;
	public static boolean shop2visit;
	public static boolean shop3visit;
	public static boolean shop4visit;
	public static int shop2;
	public static int shop3;
	public static int gold;
	public static int levelTheme = 0;
	public static int previousTheme = 0;
	public static int coins = 0;
	public static int usedSOU = 0;
	public static float version;
	public static float realversion = 0.31f;
	public static int roundswon = 0;
	
	//list of discovered mobs variables (ABC order)
	public static boolean albinodiscovered = false;
	public static boolean banditdiscovered = false;
	public static boolean batdiscovered = false;
	public static boolean brutediscovered = false;
	public static boolean caveflowerdiscovered = false;
	public static boolean cavespiderdiscovered = false;
	public static boolean crabdiscovered = false;
	public static boolean cursediscovered = false;
	public static boolean dm300discovered = false;
	public static boolean fetidratdiscovered = false;
	public static boolean ghostdiscovered = false;
	public static boolean gnolldiscovered = false;
	public static boolean goodiscovered = false;
	public static boolean masterthiefdiscovered = false;
	public static boolean mimicdiscovered = false;
	public static boolean piranhadiscovered = false;
	public static boolean ratdiscovered = false;
	public static boolean ratkingdiscovered = false;
	public static boolean ratprincediscovered = false;
	public static boolean sewerhorsediscovered = false;
	public static boolean shamandiscovered = false;
	public static boolean shieldeddiscovered = false;
	public static boolean skeletondiscovered = false;
	public static boolean spinnerdiscovered = false;
	public static boolean statuediscovered = false;
	public static boolean tengudiscovered = false;
	public static boolean thiefdiscovered = false;
	public static boolean toxicsludgediscovered = false;
	public static boolean trollsmithdiscovered = false;
	public static boolean wraithdiscovered = false;
	public static boolean dungeonfishdiscovered = false;
	
	// Reason of death
	public static String resultDescription;
	
	public static HashSet<Integer> chapters;
	
	// Hero's field of view
	public static boolean[] visible = new boolean[Level.LENGTH];
	
	public static boolean nightMode;
	
	public static SparseArray<ArrayList<Item>> droppedItems;
	
	public static void init() {

		challenges = PixelDungeon.challenges();
		
		Actor.clear();
		resethub = false;
		PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		
		Scroll.initLabels();
		Potion.initColors();
		Wand.initWoods();
		Ring.initGems();
		//init beastiary
		albinodiscovered = false;
		banditdiscovered = false;
		batdiscovered = false;
		brutediscovered = false;
		caveflowerdiscovered = false;
		cavespiderdiscovered = false;
		crabdiscovered = false;
		cursediscovered = false;
		dm300discovered = false;
		fetidratdiscovered = false;
		ghostdiscovered = false;
		gnolldiscovered = false;
		 goodiscovered = false;
		 masterthiefdiscovered = false;
		 mimicdiscovered = false;
		 piranhadiscovered = false;
		 ratdiscovered = false;
		 ratkingdiscovered = false;
		ratprincediscovered = false;
		sewerhorsediscovered = false;
		shamandiscovered = false;
		shieldeddiscovered = false;
		skeletondiscovered = false;
		spinnerdiscovered = false;
		 statuediscovered = false;
		 tengudiscovered = false;
		thiefdiscovered = false;
		 toxicsludgediscovered = false;
		 trollsmithdiscovered = false;
		 wraithdiscovered = false;
		 dungeonfishdiscovered = false;
		
		Statistics.reset();
		Journal.reset();
		
		Quests.heroquests.clear();
		
		
		usedSOU = 0;
		depth = 0;
		gold = 100;
		Hunger.level = 0;
		initshop = true;
		droppedItems = new SparseArray<ArrayList<Item>>();
		levelTheme = 0;
		previousTheme = 0;

		scrollsOfEnchantment = 0;
		dewVial = false;
		zerocheck = true;
		shop1visit = false;
		shop2visit = false;
		shop3visit = false;
		shop4visit = false;
		chapters = new HashSet<Integer>();

		shop1 = Random.Int(3,5);
		shop2 = Random.Int(7,10);
		shop3 = Random.Int(11,13);
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();
		Quests.quests.clear();
		Quests.quests = new ArrayList<Quests.Quest>();
		Room.shuffleTypes();
		
		QuickSlot.primaryValue = null;
		QuickSlot.secondaryValue = null;
		
		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		StartScene.curClass.initHero( hero );
	}
	
	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	

	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		if (InterlevelScene.mode == Mode.ASCEND){
			
		} else {

		depth++;
		}

		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		
		Arrays.fill( visible, false );
		
		Level level;
		switch (depth) {
		case -1:
		case 0:
			level = new TownLevel();
			break;
		case 1:
			level = new HubLevel();
			break;
		case 2:
			
			levelTheme = Random.Int(1,4);
			while (true){
				if(levelTheme != previousTheme){
					break;
				} else{
					levelTheme = Random.Int(1,4);
				}
				
			}
			if (levelTheme == 1){
				level = new SewerLevel();
			}
			else if (levelTheme == 2){
				
					level = new PrisonLevel();
				
			}
			else if (levelTheme == 3){
				
					level = new CavesLevel();	
				 
			} else{
				level = new DeadEndLevel();
			}
			
			
			break;
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
			if (levelTheme == 1){
				level = new SewerLevel();
			}
			else if (levelTheme == 2){
				level = new PrisonLevel();
			}
			else if (levelTheme == 3){
				level = new CavesLevel();
			}
			else{
				level = new DeadEndLevel();
			}
			break;
		case 15:
			if(levelTheme == 1){
				level = new SewerBossLevel();
			}
			else if (levelTheme == 2){
				level = new PrisonBossLevel();
			}
			else if (levelTheme == 3){
				level = new CavesBossLevel();
			} 
			else {
				level = new DeadEndLevel();
			}
			break;
		case 16:
			level = new LastLevel();
			break;
		default:
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		
		level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();

		
		return level;
	}
	
	public static void resetLevel() {
		
		Actor.clear();
		
		Arrays.fill( visible, false );
		
		level.reset();
		switchLevel( level, level.entrance );
	}
	
	public static boolean shopOnLevel() {
		return depth == 0 || depth == shop1 || depth == shop2 || depth == shop3 || depth == 28;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		return depth == 29;
	}
	
	@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {
		
		nightMode = new Date().getHours() < 7;
		
		Dungeon.level = level;
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.add( level.respawner() );
		}
		
		hero.pos = pos != -1 ? pos : level.exit;
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		
		observe();
	}
	
	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = (ArrayList<Item>)Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<Item>() ); 
		}
		dropped.add( item );
	}
	
	
	

	
	public static boolean soeNeeded() {
		return Random.Int( 9 * (1 + scrollsOfEnchantment) ) < hero.lvl;
	}
	

	
	private static final String RG_GAME_FILE	= "game.dat";
	private static final String RG_DEPTH_FILE	= "depth%d.dat";
	
	private static final String WR_GAME_FILE	= "warrior.dat";
	private static final String WR_DEPTH_FILE	= "warrior%d.dat";
	
	private static final String MG_GAME_FILE	= "mage.dat";
	private static final String MG_DEPTH_FILE	= "mage%d.dat";
	
	private static final String RN_GAME_FILE	= "ranger.dat";
	private static final String RN_DEPTH_FILE	= "ranger%d.dat";
	
	private static final String VERSION		= "version";
	private static final String CHALLENGES	= "challenges";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String LEVEL		= "level";
	private static final String DROPPED		= "dropped%d";
	private static final String SOE			= "scrollsOfEnchantment";
	private static final String DV			= "dewVial";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	private static final String LEVELTHEME  = "levelTheme";
	private static final String PREVIOUSTHEME = "previousTheme";
	private static final String SHOP1       = "first shop";
	private static final String SHOP2       = "second shop";
	private static final String SHOP3       = "third shop";
	private static final String SHOP1VISIT  = "first shop visit check";
	private static final String SHOP2VISIT  = "second shop visit check";
	private static final String SHOP3VISIT  = "third shop visit check";
	private static final String SHOP4VISIT  = "fourth shop visit check";
	private static final String ZEROCHECK   = "zerocheck";
	private static final String COINS       = "coins";
	private static final String NAME        = "name";
	private static final String CHANGENAME  = "change name";
	private static final String COLOR       = "name color";
	private static final String USEDSOU     = "SOU's read";
	private static final String V2          = "Secondary version";
	private static final String HUBRESET    = "Reset hub?";
	private static final String ROUNDSWON   = "Number of rounds won";
	
	//ratking clicker game data
	private static final String MONEY      ="moneyz";
	private static final String RATLEVEL   ="rat level";
	private static final String RATCOUNT   ="# of rats";
	private static final String CLICKLEVEL ="clicklevel";
	private static final String MULTIPLIER = "multiplier";
	private static final String KINGMOOD   = "king mood";
	
	//save data for the mobs discovery variables
	private static final String ALBINODISCOVERED = "Albino rat discovered";
	private static final String BANDITDISCOVERED = "Bandit discovered";
	private static final String BATDISCOVERED = "Bat discovered";
	private static final String BRUTEDISCOVERED = "Brute discovered";
	private static final String CAVEFLOWERDISCOVERED = "Cave Flower discovered";
	private static final String CAVESPIDERDISCOVERED = "Cave Spider discovered";
	private static final String CRABDISCOVERED = "Crab discovered";
	private static final String CURSEDISCOVERED = "Curse Personification discovered";
	private static final String DM300DISCOVERED = "DM 300 discovered";
	private static final String FETIDRATDISCOVERED = "Fetid rat discovered";
	private static final String GHOSTDISCOVERED = "Ghost discovered";
	private static final String GNOLLDISCOVERED = "Gnoll discovered";
	private static final String GOODISCOVERED = "Goo discovered";
	private static final String MASTERTHIEFDISCOVERED = "Master Thief discovered";
	private static final String MIMICDISCOVERED = "Mimic discovered";
	private static final String PIRANHADISCOVERED = "Piranha discovered";
	private static final String RATDISCOVERED ="Rat discovered";
	private static final String RATKINGDISCOVERED = "Rat King discovered";
	private static final String RATPRINCEDISCOVERED = "Rat Prince Discovered";
	private static final String SEWERHORSEDISCOVERED = "Sewer Horse discovered";
	private static final String SHAMANDISCOVERED = "Shaman discovered";
	private static final String SHIELDEDDISCOVERED = "Shielded discovered";
	private static final String SKELETONDISCOVERED = "Skeleton discovered";
	private static final String SPINNERDISCOVERED = "Spinner discovered";
	private static final String STATUEDISCOVERED = "Statue discovered";
	private static final String TENGUDISCOVERED = "Tengu discovered";
	private static final String THIEFDISCOVERED = "Thief discovered";
	private static final String TOXICSLUDGEDISCOVERED = "Toxic Sludge discovered";
	private static final String TROLLSMITHDISCOVERED = "Troll Smith discovered";
	private static final String WRAITHDISCOVERED = "Wraith discovered";
	private static final String DUNGEONFISHDISCOVERED = "Dungeon fish discovered";
	

	
	public static String gameFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return WR_GAME_FILE;
		case MAGE:
			return MG_GAME_FILE;
		case HUNTRESS:
			return RN_GAME_FILE;
		default:
			return RG_GAME_FILE;
		}
	}
	
	private static String depthFile( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return WR_DEPTH_FILE;
		case MAGE:
			return MG_DEPTH_FILE;
		case HUNTRESS:
			return RN_DEPTH_FILE;
		default:
			return RG_DEPTH_FILE;
		}
	}
	

	
	public static void saveNameDetails (String fileName) throws IOException {
		Dungeon.version = realversion;
		Bundle bundle = new Bundle();
		bundle.put(NAME, name);
		bundle.put(CHANGENAME, changename);
		bundle.put(COLOR, color);
		
		OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}
	
	public static void saveGame( String fileName ) throws IOException {
		try {
			Bundle bundle = new Bundle();
			
			//put in data for boolean list of mobs
			bundle.put(ALBINODISCOVERED, albinodiscovered);
			bundle.put(BANDITDISCOVERED, banditdiscovered);
			bundle.put(BATDISCOVERED, batdiscovered);
			bundle.put(BRUTEDISCOVERED, brutediscovered);
			bundle.put(CAVEFLOWERDISCOVERED, caveflowerdiscovered);
			bundle.put(CAVESPIDERDISCOVERED, cavespiderdiscovered);
			bundle.put(CRABDISCOVERED, crabdiscovered);
			bundle.put(CURSEDISCOVERED, cursediscovered);
			bundle.put(DM300DISCOVERED, dm300discovered);
			bundle.put(FETIDRATDISCOVERED, fetidratdiscovered);
			bundle.put(GHOSTDISCOVERED, ghostdiscovered);
			bundle.put(GNOLLDISCOVERED, gnolldiscovered);
			bundle.put(GOODISCOVERED, goodiscovered);
			bundle.put(MASTERTHIEFDISCOVERED, masterthiefdiscovered);
			bundle.put(MIMICDISCOVERED, mimicdiscovered);
			bundle.put(PIRANHADISCOVERED, piranhadiscovered);
			bundle.put(RATDISCOVERED, ratdiscovered);
			bundle.put(RATKINGDISCOVERED, ratkingdiscovered);
			bundle.put(RATPRINCEDISCOVERED, ratprincediscovered);
			bundle.put(SEWERHORSEDISCOVERED, sewerhorsediscovered);
			bundle.put(SHAMANDISCOVERED, shamandiscovered);
			bundle.put(SHIELDEDDISCOVERED, shieldeddiscovered);
			bundle.put(SKELETONDISCOVERED, skeletondiscovered);
			bundle.put(SPINNERDISCOVERED, spinnerdiscovered);
			bundle.put(STATUEDISCOVERED, statuediscovered);
			bundle.put(TENGUDISCOVERED, tengudiscovered);
			bundle.put(THIEFDISCOVERED, thiefdiscovered);
			bundle.put(TROLLSMITHDISCOVERED, trollsmithdiscovered);
			bundle.put(WRAITHDISCOVERED, wraithdiscovered);
			bundle.put(DUNGEONFISHDISCOVERED, dungeonfishdiscovered);
			
			//ratking game
			bundle.put(MONEY, WndClickerGame.money);
			bundle.put(RATLEVEL, WndClickerGame.ratlevel);
			bundle.put(RATCOUNT, WndClickerGame.rats);
			bundle.put(CLICKLEVEL, WndClickerGame.clicklevel);
			bundle.put(MULTIPLIER, WndClickerGame.multiplier);
			bundle.put(KINGMOOD, WndClickerGame.moodlevel);
			
			version = realversion;
			bundle.put(V2, version);
			bundle.put( VERSION, Game.version );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );
			bundle.put(LEVELTHEME, levelTheme);
			bundle.put(PREVIOUSTHEME, previousTheme);
			bundle.put(SHOP1, shop1);
			bundle.put(SHOP2, shop2);
			bundle.put(SHOP3, shop3);
			bundle.put(SHOP1VISIT, shop1visit);
			bundle.put(SHOP2VISIT, shop2visit);
			bundle.put(SHOP3VISIT, shop3visit);
			bundle.put(SHOP4VISIT, shop4visit);
			bundle.put(ZEROCHECK, zerocheck);
			bundle.put(COINS, coins);
			bundle.put(HUBRESET, resethub);
			bundle.put(ROUNDSWON, roundswon);
			loadName("Name");
			saveName();
			
			bundle.put(USEDSOU, usedSOU);
			
			
			
			for (int d : droppedItems.keyArray()) {
				bundle.put( String.format( DROPPED, d ), droppedItems.get( d ) );
			}
			

			bundle.put( SOE, scrollsOfEnchantment );
			bundle.put( DV, dewVial );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			Room.storeRoomsInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Journal.storeInBundle( bundle );
			Quests.StoreQuestsInBundle(bundle);
			
			QuickSlot.save( bundle );
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Wand.save( bundle );
			Ring.save( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
			
		} catch (Exception e) {

			GamesInProgress.setUnknown( hero.heroClass );
		}
	}
	
	public static void saveLevel() throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		OutputStream output = Game.instance.openFileOutput( Utils.format( depthFile( hero.heroClass ), depth ), Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}
	
	public static void saveAll() throws IOException {
		if (hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( gameFile( hero.heroClass ) );
			saveLevel();
			
			GamesInProgress.set( hero.heroClass, depth, hero.lvl, challenges != 0 );
			
		} else if (WndResurrect.instance != null) {
			
			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.causeOfDeath );
			
		}
	}
	public static void saveName() throws IOException {
		saveNameDetails("Name");
	}
	public static void loadName(String fileName) throws IOException {
		Bundle bundle = gameBundle(fileName);
		Dungeon.name = bundle.getString(NAME);
		Dungeon.changename = bundle.getBoolean(CHANGENAME);
		Dungeon.color = bundle.getInt(COLOR);
		
		
	}
	public static void loadGame( HeroClass cl ) throws IOException {
		loadGame( gameFile( cl ), true );
	}
	
	public static void loadGame( String fileName ) throws IOException {
		loadGame( fileName, false );
	}
	
	public static void loadGame( String fileName, boolean fullLoad ) throws IOException {
		
		Bundle bundle = gameBundle( fileName );
		Dungeon.version    = bundle.getFloat(V2);
		//Load booleans for mob discovery
		if(Dungeon.version == Dungeon.realversion){
			Dungeon.albinodiscovered = bundle.getBoolean(ALBINODISCOVERED);
			Dungeon.banditdiscovered = bundle.getBoolean(BANDITDISCOVERED);
			Dungeon.batdiscovered = bundle.getBoolean(BATDISCOVERED);
			Dungeon.caveflowerdiscovered = bundle.getBoolean(CAVEFLOWERDISCOVERED);
			Dungeon.cavespiderdiscovered = bundle.getBoolean(CAVESPIDERDISCOVERED);
			Dungeon.crabdiscovered = bundle.getBoolean(CRABDISCOVERED);
			Dungeon.cursediscovered = bundle.getBoolean(CURSEDISCOVERED);
			Dungeon.dm300discovered = bundle.getBoolean(DM300DISCOVERED);
			Dungeon.fetidratdiscovered = bundle.getBoolean(FETIDRATDISCOVERED);
			Dungeon.ghostdiscovered = bundle.getBoolean(GHOSTDISCOVERED);
			Dungeon.gnolldiscovered = bundle.getBoolean(GNOLLDISCOVERED);
			Dungeon.goodiscovered = bundle.getBoolean(GOODISCOVERED);
			Dungeon.masterthiefdiscovered = bundle.getBoolean(MASTERTHIEFDISCOVERED);
			Dungeon.mimicdiscovered = bundle.getBoolean(MIMICDISCOVERED);
			Dungeon.piranhadiscovered = bundle.getBoolean(PIRANHADISCOVERED);
			Dungeon.ratdiscovered = bundle.getBoolean(RATDISCOVERED);
			Dungeon.ratkingdiscovered = bundle.getBoolean(RATKINGDISCOVERED);
			Dungeon.ratprincediscovered = bundle.getBoolean(RATPRINCEDISCOVERED);
			Dungeon.sewerhorsediscovered = bundle.getBoolean(SEWERHORSEDISCOVERED);
			Dungeon.shamandiscovered = bundle.getBoolean(SHAMANDISCOVERED);
			Dungeon.shieldeddiscovered = bundle.getBoolean(SHIELDEDDISCOVERED);
			Dungeon.skeletondiscovered = bundle.getBoolean(SKELETONDISCOVERED);
			Dungeon.spinnerdiscovered = bundle.getBoolean(SPINNERDISCOVERED);
			Dungeon.statuediscovered = bundle.getBoolean(STATUEDISCOVERED);
			Dungeon.tengudiscovered = bundle.getBoolean(TENGUDISCOVERED);
			Dungeon.thiefdiscovered = bundle.getBoolean(THIEFDISCOVERED);
			Dungeon.toxicsludgediscovered = bundle.getBoolean(TOXICSLUDGEDISCOVERED);
			Dungeon.trollsmithdiscovered = bundle.getBoolean(TROLLSMITHDISCOVERED);
			Dungeon.wraithdiscovered = bundle.getBoolean(WRAITHDISCOVERED);
			Dungeon.dungeonfishdiscovered = bundle.getBoolean(DUNGEONFISHDISCOVERED);
			
			//other things
			previousTheme = bundle.getInt(PREVIOUSTHEME);
			roundswon = bundle.getInt(ROUNDSWON);
		} else{
			albinodiscovered = false;
			banditdiscovered = false;
			batdiscovered = false;
			brutediscovered = false;
			caveflowerdiscovered = false;
			cavespiderdiscovered = false;
			crabdiscovered = false;
			cursediscovered = false;
			dm300discovered = false;
			fetidratdiscovered = false;
			ghostdiscovered = false;
			gnolldiscovered = false;
			 goodiscovered = false;
			 masterthiefdiscovered = false;
			 mimicdiscovered = false;
			 piranhadiscovered = false;
			 ratdiscovered = false;
			 ratkingdiscovered = false;
			ratprincediscovered = false;
			sewerhorsediscovered = false;
			shamandiscovered = false;
			shieldeddiscovered = false;
			skeletondiscovered = false;
			spinnerdiscovered = false;
			 statuediscovered = false;
			 tengudiscovered = false;
			thiefdiscovered = false;
			 toxicsludgediscovered = false;
			 trollsmithdiscovered = false;
			 wraithdiscovered = false;
			 dungeonfishdiscovered = false;
		}
		
		//Wnd Clicker Game
		if(realversion >= 0.31f){
		WndClickerGame.clicklevel = bundle.getInt(CLICKLEVEL);
		WndClickerGame.money = bundle.getInt(MONEY);
		WndClickerGame.multiplier = bundle.getInt(MULTIPLIER);
		WndClickerGame.rats = bundle.getInt(RATCOUNT);
		WndClickerGame.ratlevel = bundle.getInt(RATLEVEL);
		WndClickerGame.moodlevel = bundle.getInt(KINGMOOD);
		}
		
		Dungeon.challenges = bundle.getInt( CHALLENGES );
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		name = bundle.getString(NAME);
		
		if (fullLoad) {
			PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		}
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Wand.restore( bundle );
		Ring.restore( bundle );
		
		scrollsOfEnchantment = bundle.getInt( SOE );
		dewVial = bundle.getBoolean( DV );
		
		if (fullLoad) {
			chapters = new HashSet<Integer>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}
			
			Room.restoreRoomsFromBundle( bundle );
		}
		
		Bundle badges = bundle.getBundle( BADGES );
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		QuickSlot.restore( bundle );
		
		@SuppressWarnings("unused")
		String version = bundle.getString( VERSION );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );
		
		Quests.RestoreQuestsFromBundle(bundle);
		
		QuickSlot.compress();
		
		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );
		levelTheme = bundle.getInt(LEVELTHEME);
		shop1 = bundle.getInt(SHOP1);
		shop2 = bundle.getInt(SHOP2);
		shop3 = bundle.getInt(SHOP3);
		shop1visit = bundle.getBoolean(SHOP1VISIT);
		shop2visit = bundle.getBoolean(SHOP2VISIT);
		shop3visit = bundle.getBoolean(SHOP3VISIT);
		shop4visit = bundle.getBoolean(SHOP4VISIT);
		zerocheck  = bundle.getBoolean(ZEROCHECK);
		coins      = bundle.getInt(COINS);
		name       = bundle.getString(NAME);
		changename = bundle.getBoolean(CHANGENAME);
		usedSOU    = bundle.getInt(USEDSOU);
		resethub   = bundle.getBoolean(HUBRESET);
		
		
		
		
		Statistics.restoreFromBundle( bundle );
		Journal.restoreFromBundle( bundle );
		
		droppedItems = new SparseArray<ArrayList<Item>>();
		for (int i=2; i <= Statistics.deepestFloor + 1; i++) {
			ArrayList<Item> dropped = new ArrayList<Item>();
			for (Bundlable b : bundle.getCollection( String.format( DROPPED, i ) ) ) {
				dropped.add( (Item)b );
			}
			if (!dropped.isEmpty()) {
				droppedItems.put( i, dropped );
			}
		}
	}
	
	public static Level loadLevel( HeroClass cl ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();
		
		InputStream input = Game.instance.openFileInput( Utils.format( depthFile( cl ), depth ) ) ;
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return (Level)bundle.get( "level" );
	}
	
	public static void deleteGame( HeroClass cl, boolean deleteLevels ) {
		
		Game.instance.deleteFile( gameFile( cl ) );
		
		if (deleteLevels) {
			int depth = 1;
			while (Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
				depth++;
			}
		}
		
		GamesInProgress.delete( cl );
	}
	
	public static void deleteLevels(HeroClass cl){
		int depth = 2;
		while (Game.instance.deleteFile( Utils.format( depthFile( cl ), depth ) )) {
			depth++;
		}
	}
	
	public static Bundle gameBundle( String fileName ) throws IOException {
		
		InputStream input = Game.instance.openFileInput( fileName );
		Bundle bundle = Bundle.read( input );
		input.close();
		
		return bundle;
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.challenges = (bundle.getInt( CHALLENGES ) != 0);
		if (info.depth == -1) {
			info.depth = bundle.getInt( "maxDepth" );	// FIXME
		}
		Hero.preview( info, bundle.getBundle( HERO ) );
	}
	
	public static void fail( String desc ) {
		resultDescription = desc;
		if (hero.belongings.getItem( Ankh.class ) == null) { 
			Rankings.INSTANCE.submit( false );
		}
	}
	
	public static void win( String desc ) {
		
		hero.belongings.identify();
		
		if (challenges != 0) {
			Badges.validateChampion();
		}
		
		resultDescription = desc;
		Rankings.INSTANCE.submit( true );
	}
	
	public static void observe() {

		if (level == null) {
			return;
		}
		
		level.updateFieldOfView( hero );
		System.arraycopy( Level.fieldOfView, 0, visible, 0, visible.length );
		
		BArray.or( level.visited, visible, level.visited );
		
		GameScene.afterObserve();
	}
	
	private static boolean[] passable = new boolean[Level.LENGTH];
	
	public static int findPath( Char ch, int from, int to, boolean pass[], boolean[] visible ) {
		
		if (Level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Level.avoid[to]) ? to : -1;
		}
		
		if (ch.flying || ch.buff( Amok.class ) != null || ch.buff( Rage.class ) != null) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		
		return PathFinder.getStep( from, to, passable );
		
	}
	
	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {
		
		if (ch.flying) {
			BArray.or( pass, Level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Level.LENGTH );
		}
		
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				int pos = ((Char)actor).pos;
				if (visible[pos]) {
					passable[pos] = false;
				}
			}
		}
		passable[cur] = true;
		
		return PathFinder.getStepBack( cur, from, passable );
		
	}

}
