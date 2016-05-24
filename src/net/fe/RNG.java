package net.fe;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class RNG.
 */
public final class RNG {
	
	private RNG() {}
	
	/** The rng. */
	private final static Random RNG = new Random();
	
	/**
	 * Returns a random number between 0 and 100
	 * @return a random number between 0 and 100
	 */
	public static int get(){
		return RNG.nextInt(100);
	}
	
	static{
		long seed = RNG.nextLong();
		RNG.setSeed(seed);
		// Temporarily rig the RNG
//		System.out.println("**********************************");
//		System.out.println("******WARNING: RNG IS RIGGED******");
//		System.out.println("**********************************");
//		RNG.setSeed(3171284040465844943L);
		System.out.println("Seed:" + seed);
	}
}
