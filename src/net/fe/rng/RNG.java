package net.fe.rng;

import java.io.Serializable;
import java.util.Random;

/**
 * The super class of all random number generating classes. Every subclass should use the
 * method {@link #get()} or the random number generator to generate a number. this ensures
 * that it respects the random number generator's seed.
 * @author wellme
 *
 */
public abstract class RNG implements Serializable {

	private static final long serialVersionUID = -6644390454723272241L;
	
	protected final static Random RNG = new Random();

	/**
	 * Returns a random integer uniformly distributed between 0 and 100.
	 * @return a random integer uniformly distributed between 0 and 100.
	 */
	protected static int get() {
		return RNG.nextInt(100);
	}

	/**
	 * Tests whether or not an event should succeed given a success rate.
	 * @param successRate The rate of success.
	 * The higher this value, the more likely it is for this method to return true.
	 * @return True if the event succeeded, false otherwise.
	 */
	public abstract boolean test(int successRate);

	/**
	 * Returns the name of the random number generator.
	 * @return the name of the random number generator.
	 */
	public abstract String getName();

	/**
	 * Returns a description of  the random number generator.
	 * @return a description of  the random number generator.
	 */
	public String getDescription() {
		return null;
	}

	@Override
	public String toString() {
		return getName();
	}

	static {
		long seed = RNG.nextLong();
		RNG.setSeed(seed);
		System.out.println("Seed:" + seed);
	}
}
