package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

public final class ClassTest {
	
	private static final String[] classNames = {
		"Roy", "Eliwood", "Lyn", "Hector", "Eirika", "Ephraim", "Marth", "Ike",
		"Sniper", "Hero", "Berserker", "Warrior", "Assassin", "Paladin", "Sage",
		"General", "Valkyrie", "Swordmaster", "Sorcerer", "Falconknight",
		"Phantom"
	};
	
	@Test
	public void test_equals() {
		for (String className : classNames) {
			Class left = Class.createClass(className);
			Class right = Class.createClass(className);
			
			assertEquals(left, right);
		}
	}
	
	@Test
	public void test_hashCode() {
		for (String className : classNames) {
			Class left = Class.createClass(className);
			Class right = Class.createClass(className);
			
			assertEquals(left.hashCode(), right.hashCode());
		}
	}
}
