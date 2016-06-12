package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

public final class HealingItemTest {
	
	@Test
	public void testVulneraryEqualsVulnerary() {
		assertTrue(HealingItem.VULNERARY.getCopy().equals(HealingItem.VULNERARY));
	}
	
	@Test
	public void testVulneraryNotEqualsConcoction() {
		assertFalse(HealingItem.CONCOCTION.equals(HealingItem.VULNERARY));
	}
	
}
