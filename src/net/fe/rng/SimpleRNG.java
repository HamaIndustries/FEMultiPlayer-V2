package net.fe.rng;

public class SimpleRNG extends RNG {

	private static final long serialVersionUID = -3412965697555276250L;

	/**
	 * Tests whether or not an event should succeed given a success rate.
	 * @param successRate The rate of success.
	 * The higher this value, the more likely it is for this method to return true
	 * @return True if the event succeeded, false otherwise.
	 */
	@Override
	public boolean test(int successRate) {
		return get() < successRate;
	}

	@Override
	public String getName() {
		return "Single roll";
	}
}
