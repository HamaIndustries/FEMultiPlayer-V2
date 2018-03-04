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
	public int actualSuccessRate(int successRate) {
		float probability = 0;
		if (successRate <= 50)
			probability = successRate * (successRate * 2 + 1);
		else
			probability = -2 * successRate * successRate + 399 * successRate - 9900;
		return (int) Math.round(probability / 100);
	}

	@Override
	public String getName() {
		return "True hit";
	}

}
