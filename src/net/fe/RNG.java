package net.fe;

import java.util.Random;

public final class RNG {

	private RNG() { }

	private final static Random RNG = new Random();

	/**
	 * Returns a random number between 0 and 100
	 * 
	 * @return a random number between 0 and 100
	 */
	public static int get() {
		return RNG.nextInt(100);
	}

	static {
		long seed = RNG.nextLong();
		RNG.setSeed(seed);
		System.out.println("Seed:" + seed);
	}
}
