package com.mygdx.game.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mygdx.game.FightSystem;
import com.mygdx.game.Hero;
import com.mygdx.game.Location;
import com.mygdx.game.MeleeWeapon;
import com.mygdx.game.Monster;
import com.mygdx.game.Shield;

public class testFightSystem {

	@Test
	public void testDefencesAmountForHeroWithShield() {
		Hero hero = new Hero(5,5,5, new Location(0,0));
		hero.equipShield(Shield.getShieldRandRarity("1"));
		assertEquals(2, FightSystem.howManyDefencesToPick(hero));
	}
	
	@Test
	public void testDefencesAmountForHeroWithoutShield() {
		Hero hero = new Hero(5,5,5, new Location(0,0));
		hero.equipMelee(MeleeWeapon.getMeleeWeaponRandRarity("sword", "1"), 0);
		hero.equipMelee(MeleeWeapon.getMeleeWeaponRandRarity("sword", "1"), 1);
		assertEquals(1, FightSystem.howManyDefencesToPick(hero));
	}
	
	@Test
	public void testAttackAmountForHeroWithTwoWeapons() {
		Hero hero = new Hero(5,5,5, new Location(0,0));
		hero.equipMelee(MeleeWeapon.getMeleeWeaponRandRarity("sword", "1"), 0);
		hero.equipMelee(MeleeWeapon.getMeleeWeaponRandRarity("sword", "1"), 1);
		assertEquals(2, FightSystem.howManyAttacksToPick(hero));
	}
	
	@Test
	public void testAttackAmountForHeroWithOneWeapon() {
		Hero hero = new Hero(5,5,5, new Location(0,0));
		hero.equipMelee(MeleeWeapon.getMeleeWeaponRandRarity("sword", "1"), 0);
		assertEquals(1, FightSystem.howManyAttacksToPick(hero));
	}

}
