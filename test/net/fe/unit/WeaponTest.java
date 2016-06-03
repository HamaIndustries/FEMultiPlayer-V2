package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

public final class WeaponTest {
	
	@Test
	public void test_equals() {
		Weapon left = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new ArrayList<>(), new Statistics(), new ArrayList<>(), null);
		Weapon right = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new ArrayList<>(), new Statistics(), new ArrayList<>(), null);
		assertEquals(left, right);
	}
	
	@Test
	public void test_hashCode() {
		Weapon left = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new ArrayList<>(), new Statistics(), new ArrayList<>(), null);
		Weapon right = new Weapon("asdf", 20, 0, 1, Weapon.Type.SWORD, 2,3,4, new ArrayList<>(), new Statistics(), new ArrayList<>(), null);
		assertEquals(left.hashCode(), right.hashCode());
	}
}
