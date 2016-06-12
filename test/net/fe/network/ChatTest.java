package net.fe.network;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import net.fe.Player;

public final class ChatTest {
	
	@Test
	public void test_whenEmpty_getLastIsSequenceOfEmptyStrings() {
		Chat dut = new Chat();
		
		assertEquals(java.util.Arrays.asList("", "", "", "", ""), dut.getLast(5));
	}
	
	@Test
	public void test_whenSizeOne_getLastContainsOneMessage() {
		Chat dut = new Chat();
		dut.add(new Player("abc", (byte) 1), "Hello!");
		
		assertEquals(java.util.Arrays.asList("", "", "", "", "abc: Hello!"), dut.getLast(5));
	}
	
	@Test
	public void test_whenSizeMany_getLastContainsParamMessages() {
		Chat dut = new Chat();
		dut.add(new Player("a", (byte) 1), "a!");
		dut.add(new Player("b", (byte) 2), "b!");
		dut.add(new Player("c", (byte) 3), "c!");
		dut.add(new Player("d", (byte) 4), "d!");
		dut.add(new Player("e", (byte) 5), "e!");
		
		assertEquals(java.util.Arrays.asList("d: d!", "e: e!"), dut.getLast(2));
	}
	
}
