package net.fe.rng;

/**
 * Removes the possibility of an event happening, always returns false on tests.
 * @author wellme
 *
 */
public class NullRNG extends RNG {

	private static final long serialVersionUID = 178223377406250358L;

	@Override
	public boolean test(int successRate) {
		return false;
	}
	
	@Override
	public int actualSuccessRate(int successRate) {
		return 0;
	}

	@Override
	public String getName() {
		return "Nullified";
	}

}
