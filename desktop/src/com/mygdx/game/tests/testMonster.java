package com.mygdx.game.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mygdx.game.Monster;

public class testMonster {

	@Test
	public void testMonsterGenModifierWeak() {
		Monster m = Monster.getMonsterWithModifier(0);
		assertEquals("weak", m.getMod().getName());
	}
	@Test
	public void testMonsterGenModifierMedium() {
		Monster m = Monster.getMonsterWithModifier(1);
		assertEquals("medium", m.getMod().getName());
	}
	@Test
	public void testMonsterGenModifierStrong() {
		Monster m = Monster.getMonsterWithModifier(2);
		assertEquals("strong", m.getMod().getName());
	}
	@Test
	public void testMonsterGenModifierDangerous() {
		Monster m = Monster.getMonsterWithModifier(3);
		assertEquals("dangerous", m.getMod().getName());
	}
	@Test
	public void testMonsterGenModifierDeadly() {
		Monster m = Monster.getMonsterWithModifier(4);
		assertEquals("deadly", m.getMod().getName());
	}

}
