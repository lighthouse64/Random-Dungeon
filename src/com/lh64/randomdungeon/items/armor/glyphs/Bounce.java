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
package com.lh64.randomdungeon.items.armor.glyphs;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.actors.Actor;
import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.mobs.Mob;
import com.lh64.randomdungeon.effects.Pushing;
import com.lh64.randomdungeon.items.armor.Armor;
import com.lh64.randomdungeon.items.armor.Armor.Glyph;
import com.lh64.randomdungeon.levels.Level;
import com.lh64.utils.Random;

public class Bounce extends Glyph {

	private static final String TXT_BOUNCE	= "%s of bounce";
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, armor.effectiveLevel() );
		
		if (Level.adjacent( attacker.pos, defender.pos ) && Random.Int( level + 5) >= 4) {
			
			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				int ofs = Level.NEIGHBOURS8[i];
				if (attacker.pos - defender.pos == ofs) {
					int newPos = attacker.pos + ofs;
					if ((Level.passable[newPos] || Level.avoid[newPos]) && Actor.findChar( newPos ) == null) {
						
						Actor.addDelayed( new Pushing( attacker, attacker.pos, newPos ), -1 );
						
						attacker.pos = newPos;
						// FIXME
						if (attacker instanceof Mob) {
							Dungeon.level.mobPress( (Mob)attacker );
						} else {
							Dungeon.level.press( newPos, attacker );
						}
						
					}
					break;
				}
			}

		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_BOUNCE, weaponName );
	}

}
