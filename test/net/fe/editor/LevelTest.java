package net.fe.editor;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.newdawn.slick.Color;

public final class LevelTest {
	
	private static final int[][] tiles = {
		{1,2,3,4},
		{4,5,6,7},
		{7,8,9,10}
	};
	private static final SpawnPoint[] spawns = {
		new SpawnPoint(0,0,Color.white),
		new SpawnPoint(2,3,Color.black)
	};
	private static final java.util.Set<SpawnPoint> spawns2 =
			new java.util.HashSet<>(java.util.Arrays.asList(spawns));
	
	@Test
	public void test_equals() {
		Level left = new Level(3, 4, tiles, spawns2);
		Level right = new Level(3, 4, tiles, spawns2);
		assertEquals(left, right);
	}
	
	@Test
	public void test_hashCode() {
		Level left = new Level(3, 4, tiles, spawns2);
		Level right = new Level(3, 4, tiles, spawns2);
		assertEquals(left.hashCode(), right.hashCode());
	}
}
