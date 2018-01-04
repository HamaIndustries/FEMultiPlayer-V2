package net.fe.rng;

/**
 * Mimics Path of Exiles' "lucky" mechanic. Tests twice with a single random number,
 * but only one numbers must past the test.
 * @author wellme
 */
public class LuckyRNG extends RNG {

	private static final long serialVersionUID = -8573128345999383723L;
	
	private static final RNG RNG_PROVIDER = new SimpleRNG();

	@Override
	public boolean test(int successRate) {
		return RNG_PROVIDER.test(successRate) || RNG_PROVIDER.test(successRate);
	}

	@Override
	public String getName() {
		return "Lucky";
	}

}
