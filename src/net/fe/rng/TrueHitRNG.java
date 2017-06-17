package net.fe.rng;

/**
 * Just read it up on serenes forest.
 * @author wellme
 *
 */
public class TrueHitRNG extends RNG {

	private static final long serialVersionUID = 6610577841389695278L;

	@Override
	public boolean test(int successRate) {
		return (get() + get()) / 2 < successRate;
	}

	@Override
	public String getName() {
		return "True hit";
	}

}
