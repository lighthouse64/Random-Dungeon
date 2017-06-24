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

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.Journal;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.blobs.ToxicGas;
import com.lh64.randomdungeon.actors.buffs.Poison;
import com.lh64.randomdungeon.items.Generator;
import com.lh64.randomdungeon.items.scrolls.ScrollOfPsionicBlast;
import com.lh64.randomdungeon.items.weapon.Weapon;
import com.lh64.randomdungeon.items.weapon.enchantments.Death;
import com.lh64.randomdungeon.items.weapon.enchantments.Leech;
import com.lh64.randomdungeon.items.weapon.melee.MeleeWeapon;
import com.lh64.randomdungeon.sprites.StatueSprite;
import com.lh64.utils.Bundle;
import com.lh64.utils.Random;

public class Statue extends Mob {
	
	{
		name = "animated statue";
		spriteClass = StatueSprite.class;
		discovered = Dungeon.statuediscovered;

		EXP = 0;
		state = PASSIVE;
	}
	
	private Weapon weapon;
	
	public Statue() {
		super();
		
		do {
			weapon = (Weapon)Generator.random( Generator.Category.WEAPON );
		} while (!(weapon instanceof MeleeWeapon) || weapon.level() < 0);
		
		weapon.identify();
		weapon.enchant();
		
		HP = HT = 15 + Dungeon.hero.lvl * 5;
		defenseSkill = 4 + Dungeon.hero.lvl;
	}
	
	private static final String WEAPON	= "weapon";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WEAPON, weapon );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		weapon = (Weapon)bundle.get( WEAPON );
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.visible[pos]) {
			Journal.add( Journal.Feature.STATUE );
			Dungeon.statuediscovered = true;
			discovered = true;
		}
		return super.act();
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( weapon.min(), weapon.max() );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return (int)((9 + Dungeon.hero.lvl) * weapon.ACU);
	}
	
	@Override
	protected float attackDelay() {
		return weapon.DLY;
	}
	
	@Override
	public int dr() {
		return Dungeon.hero.lvl;
	}
	
	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}
		
		super.damage( dmg, src );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		weapon.proc( this, enemy, damage );
		return damage;
	}
	
	@Override
	public void beckon( int cell ) {
		// Do nothing
	}
	
	@Override
	public void die( Object cause ) {
		Dungeon.level.drop( weapon, pos ).sprite.drop();
		super.die( cause );
	}
	
	@Override
	public void destroy() {
		Journal.remove( Journal.Feature.STATUE );
		super.destroy();
	}
	
	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return
			"You would think that it's just another ugly statue of this dungeon, but its red glowing eyes give itself away. " +
			"While the statue itself is made of stone, the _" + weapon.name() + "_, it's wielding, looks real.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ToxicGas.class );
		RESISTANCES.add( Poison.class );
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
		IMMUNITIES.add( Leech.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
