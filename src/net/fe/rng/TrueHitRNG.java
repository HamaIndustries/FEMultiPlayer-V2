package net.fe.rng;


/**
 * Just read it up on serenes forest.
 * @author wellme
 *
 */
public class TrueHitRNG extends RNG {

	@Override
	public boolean test(int successRate) {
		return (get() + get()) / 2 < successRate;
	}

}
