package net.fe.rng;

/**
 * Removes the possibility of an event failing, always returns true on tests.
 * @author wellme
 */
public class GuaranteedRNG extends RNG {

	private static final long serialVersionUID = 3041588764668735176L;

	@Override
	public boolean test(int successRate) {
		return true;
	}

	@Override
	public int actualSuccessRate(int successRate) {
		return 100;
	}

	@Override
	public String getName() {
		return "Guaranteed";
	}

}
