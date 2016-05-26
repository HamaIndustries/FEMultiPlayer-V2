package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;

public final class StatisticsTest {
	
	@Test
	public void test_toMapAndFromMapAreInverses() {
		Statistics vals = new Statistics(1,2,3,4,5,6,7,8,9,10,11);
		Statistics vals2 = new Statistics(vals.toMap());
		assertEquals(vals, vals2);
	}
	
	@Test
	public void test_EmptyConstructorFillsWithZeros() {
		Statistics vals = new Statistics(0,0,0,0,0,0,0,0,0,0,0);
		Statistics vals2 = new Statistics();
		assertEquals(vals, vals2);
	}
	
	@Test
	public void test_CopyChangesNamedStatInNewValue() {
		Statistics vals = new Statistics(0,0,0,0,0,0,0,0,0,0,0);
		Statistics vals2 = vals.copy("Def", 20);
		assertEquals(20, vals2.def);
		assertFalse(vals.equals(vals2));
	}
	
}
