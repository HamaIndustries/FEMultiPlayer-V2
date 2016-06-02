package net.fe.editor;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.newdawn.slick.Color;

public final class SpawnPointTest {
	
	private static final int[] ints = {-1, 0, 1, 15, 31};
	private static final Color[] colors = {Color.red, Color.blue, new Color(0.1f,0.5f,0.8f)};
	
	@Test
	public void test_equals() {
		for (int x : ints)
		for (int y : ints)
		for (Color c : colors) {
			SpawnPoint left = new SpawnPoint(x, y, c);
			SpawnPoint right = new SpawnPoint(x, y, c);
			assertEquals(left, right);
			
			SpawnPoint r2 = new SpawnPoint(-10, y, c);
			assertFalse(left.equals(r2));
			SpawnPoint r3 = new SpawnPoint(y, -10, c);
			assertFalse(left.equals(r3));
//			SpawnPoint r4 = new SpawnPoint(x, y, Color.white);
//			assertFalse(left.equals(r4));
		}
	}
	
	@Test
	public void test_hashCode() {
		for (int x : ints)
		for (int y : ints)
		for (Color c : colors) {
			SpawnPoint left = new SpawnPoint(x, y, c);
			SpawnPoint right = new SpawnPoint(x, y, c);
			
			assertEquals(left.hashCode(), right.hashCode());
		}
	}
}
