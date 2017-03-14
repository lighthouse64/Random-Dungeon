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
import com.lh64.randomdungeon.windows.WndResurrect;
import com.lh64.utils.Bundlable;
import com.lh64.utils.Bundle;
import com.lh64.utils.PathFinder;
import com.lh64.utils.Random;
import com.lh64.utils.SparseArray;
import com.lh64.randomdungeon.scenes.InterlevelScene;
import com.lh64.randomdungeon.scenes.InterlevelScene.Mode;

public class Dungeon {
	
	public static int potionOfStrength;
	public static int scrollsOfUpgrade;
	public static int scrollsOfEnchantment;
	public static boolean dewVial;		// true if the dew vial can be spawned

	
	public static int challenges;
	public static boolean storage =false;
	public static boolean ShopkeeperBag = false;
	public static boolean zerocheck = true;
	
	public static Hero hero;
	public static Level level;
	
	public static boolean changename = true;
	public static String name = "";
	
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
	public static int coins = 0;
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
		
		PathFinder.setMapSize( Level.WIDTH, Level.HEIGHT );
		
		Scroll.initLabels();
		Potion.initColors();
		Wand.initWoods();
		Ring.initGems();
		
		Statistics.reset();
		Journal.reset();
		
		depth = 0;
		gold = 0;
		Hunger.level = 0;
		initshop = true;
		droppedItems = new SparseArray<ArrayList<Item>>();
		levelTheme = 0;
		potionOfStrength = 0;
		scrollsOfUpgrade = 0;
		scrollsOfEnchantment = 0;
		dewVial = false;
		zerocheck = true;
		shop1visit = false;
		shop2visit = false;
		shop3visit = false;
		shop4visit = false;
		chapters = new HashSet<Integer>();

		shop1 = Random.Int(3,10);
		shop2 = Random.Int(11,19);
		shop3 = Random.Int(20,27);
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();
		
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
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
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
		case 29:
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
		case 30:
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
	
	public static boolean posNeeded() {
		int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
		return chance( quota, potionOfStrength );
	}
	
	public static boolean souNeeded() {
		int[] quota = {5, 3, 10, 6, 15, 9, 20, 12, 25, 13};
		return chance( quota, scrollsOfUpgrade );
	}
	
	public static boolean soeNeeded() {
		return Random.Int( 12 * (1 + scrollsOfEnchantment) ) < depth;
	}
	
	private static boolean chance( int[] quota, int number ) {
		
		for (int i=0; i < quota.length; i += 2) {
			int qDepth = quota[i];
			if (depth <= qDepth) {
				int qNumber = quota[i + 1];
				return Random.Float() < (float)(qNumber - number) / (qDepth - depth + 1);
			}
		}
		
		return false;
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
	private static final String POS			= "potionsOfStrength";
	private static final String SOU			= "scrollsOfEnhancement";
	private static final String SOE			= "scrollsOfEnchantment";
	private static final String DV			= "dewVial";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	private static final String LEVELTHEME  = "levelTheme";
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
		Bundle bundle = new Bundle();
		bundle.put(NAME, name);
		bundle.put(CHANGENAME, changename);
		OutputStream output = Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE );
		Bundle.write( bundle, output );
		output.close();
	}
	public static void saveGame( String fileName ) throws IOException {
		try {
			Bundle bundle = new Bundle();
			
			bundle.put( VERSION, Game.version );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );
			bundle.put(LEVELTHEME, levelTheme);
			bundle.put(SHOP1, shop1);
			bundle.put(SHOP2, shop2);
			bundle.put(SHOP3, shop3);
			bundle.put(SHOP1VISIT, shop1visit);
			bundle.put(SHOP2VISIT, shop2visit);
			bundle.put(SHOP3VISIT, shop3visit);
			bundle.put(SHOP4VISIT, shop4visit);
			bundle.put(ZEROCHECK, zerocheck);
			bundle.put(COINS, coins);
			loadName("Name");
			bundle.put(NAME, name);
			
			for (int d : droppedItems.keyArray()) {
				bundle.put( String.format( DROPPED, d ), droppedItems.get( d ) );
			}
			
			bundle.put( POS, potionOfStrength );
			bundle.put( SOU, scrollsOfUpgrade );
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
	}
	public static void loadGame( HeroClass cl ) throws IOException {
		loadGame( gameFile( cl ), true );
	}
	
	public static void loadGame( String fileName ) throws IOException {
		loadGame( fileName, false );
	}
	
	public static void loadGame( String fileName, boolean fullLoad ) throws IOException {
		
		Bundle bundle = gameBundle( fileName );
		
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
		
		potionOfStrength = bundle.getInt( POS );
		scrollsOfUpgrade = bundle.getInt( SOU );
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
