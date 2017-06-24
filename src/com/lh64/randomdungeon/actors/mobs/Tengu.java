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
package com.lh64.randomdungeon.actors.mobs;

import java.util.HashSet;

import com.lh64.noosa.audio.Sample;
import com.lh64.randomdungeon.Assets;
import com.lh64.randomdungeon.Badges;
import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.Statistics;
import com.lh64.randomdungeon.Badges.Badge;
import com.lh64.randomdungeon.actors.Actor;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.blobs.ToxicGas;
import com.lh64.randomdungeon.actors.buffs.Paralysis;
import com.lh64.randomdungeon.actors.buffs.Poison;
import com.lh64.randomdungeon.actors.hero.HeroSubClass;
import com.lh64.randomdungeon.effects.CellEmitter;
import com.lh64.randomdungeon.effects.Speck;
import com.lh64.randomdungeon.items.TomeOfMastery;
import com.lh64.randomdungeon.items.keys.SkeletonKey;
import com.lh64.randomdungeon.items.scrolls.ScrollOfMagicMapping;
import com.lh64.randomdungeon.items.scrolls.ScrollOfPsionicBlast;
import com.lh64.randomdungeon.items.weapon.enchantments.Death;
import com.lh64.randomdungeon.levels.Level;
import com.lh64.randomdungeon.levels.Terrain;
import com.lh64.randomdungeon.mechanics.Ballistica;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.sprites.TenguSprite;
import com.lh64.utils.Random;

public class Tengu extends Mob {

	private static final int JUMP_DELAY = 5;
	
	{
		name = Dungeon.depth == Statistics.deepestFloor ? "Tengu" : "Memory of Tengu";
		spriteClass = TenguSprite.class;
		discovered = Dungeon.tengudiscovered;
		
		HP = HT = Random.Int(50,75);
		EXP = Dungeon.hero.lvl;
		defenseSkill = 10;
	}
	
	private int timeToJump = JUMP_DELAY;
	@Override 
	public boolean act() {
		if(Dungeon.visible[pos]){
			Dungeon.tengudiscovered = true;
			discovered = true;
			
		}
		return super.act();
	}
	@Override
	public int damageRoll() {
		return Random.Int(4,7);
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 18;
	}
	
	@Override
	public int dr() {
		return 3;
	}
	
	@Override
	public void die( Object cause ) {
		
		Badges.Badge badgeToCheck = null;
		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badgeToCheck = Badge.MASTERY_WARRIOR;
			break;
		case MAGE:
			badgeToCheck = Badge.MASTERY_MAGE;
			break;
		case ROGUE:
			badgeToCheck = Badge.MASTERY_ROGUE;
			break;
		case HUNTRESS:
			badgeToCheck = Badge.MASTERY_HUNTRESS;
			break;
		}
		if (!Badges.isUnlocked( badgeToCheck ) || Dungeon.hero.subClass != HeroSubClass.NONE) {
			Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		super.die( cause );
		
		Badges.validateBossSlain();
		
		yell( "Free at last..." );
	}
	
	@Override
	protected boolean getCloser( int target ) {
		if (Level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {
		timeToJump--;
		if (timeToJump <= 0 && Level.adjacent( pos, enemy.pos )) {
			jump();
			return true;
		} else {
			return super.doAttack( enemy );
		}
	}
	
	private void jump() {
		timeToJump = JUMP_DELAY;
		
		for (int i=0; i < 4; i++) {
			int trapPos;
			do {
				trapPos = Random.Int( Level.LENGTH );
			} while (!Level.fieldOfView[trapPos] || !Level.passable[trapPos]);
			
			if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
				Level.set( trapPos, Terrain.POISON_TRAP );
				GameScene.updateMap( trapPos );
				ScrollOfMagicMapping.discover( trapPos );
			}
		}
		
		int newPos;
		do {
			newPos = Random.Int( Level.LENGTH );
		} while (
			!Level.fieldOfView[newPos] || 
			!Level.passable[newPos] || 
			(enemy != null && Level.adjacent( newPos, enemy.pos )) ||
			Actor.findChar( newPos ) != null);
		
		sprite.move( pos, newPos );
		move( newPos );
		
		if (Dungeon.visible[newPos]) {
			CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
		}
		
		spend( 1 / speed() );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Gotcha, " + Dungeon.hero.heroClass.title() + "!" );
	}
	
	@Override
	public String description() {
		return
			"Tengu are members of the ancient assassins clan, which is also called Tengu. " +
			"These assassins are noted for extensive use of shuriken and traps.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( Paralysis.class);
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
