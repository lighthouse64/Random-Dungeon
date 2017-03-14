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



import com.lh64.randomdungeon.actors.Char;
import com.lh64.randomdungeon.actors.buffs.Buff;
import com.lh64.randomdungeon.sprites.CharSprite;
import com.lh64.randomdungeon.sprites.FemaleHumanSprite;
import com.lh64.randomdungeon.sprites.HumanSprite;
import com.lh64.utils.Random;

public class HouseHuman extends NPC {
	public static String damageText;
	public static String talkText;
	public static int textroll;
	public static int coloroll;

	{
		
		name = "Person";
		switch(Random.Int(0,3)){
		case 1:
			spriteClass = HumanSprite.class;
			break;
		case 2:
			spriteClass = FemaleHumanSprite.class;
			break;
		default:
			spriteClass = HumanSprite.class;
		}
		state = PASSIVE;
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	

	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	@Override
	public float speed() {
		return 0.5f;
	}
	@Override
	public void damage( int dmg, Object src ) {
		textroll = Random.Int(0,5);
		switch (textroll){
		case 1:
			damageText = "ouch! \n";
			break;
		
		case 2:
			damageText = "Rude. \n";
			break;
			
		default:
			damageText = "That was not nice. \n";
		}
		sprite.showStatus(CharSprite.NEGATIVE, damageText);
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		textroll = Random.Int(1,5);
		coloroll = Random.Int(1,3);
		switch (textroll){
		case 1:
			talkText = "Hello there :) ";
			break;
		
		case 2:
			talkText = "Welcome";
			break;
		
		default:
			talkText = "Greetings ";
			
		}
		
		switch (coloroll){
		case 1:
			sprite.showStatus(CharSprite.OK, talkText);
			break;
		case 2:
			sprite.showStatus(CharSprite.POSITIVE, talkText);
			break;
		default:
			sprite.showStatus(CharSprite.DEFAULT, talkText);
		}
		
	}
	
	@Override
	public String description() {
		return 
			"That random person that stays at home.";
	}
}
