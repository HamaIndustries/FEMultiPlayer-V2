package net.fe.rng;

/**
 * Mimics Path of Exiles' "unlucky" mechanic. Tests twice with a single random number,
 * but both numbers must past the test.
 * @author wellme
 */
public class UnluckyRNG extends RNG {
	
	
	private static final RNG RNG_PROVIDER = new SimpleRNG();

	@Override
	public boolean test(int successRate) {
		return RNG_PROVIDER.test(successRate) && RNG_PROVIDER.test(successRate);
	}

	@Override
	public String getName() {
		return "Unlucky";
	}
}
