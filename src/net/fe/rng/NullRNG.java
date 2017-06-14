package net.fe.rng;

/**
 * Removes the possibility of an event happening, always returns false on tests. 
 * @author wellme
 *
 */
public class NullRNG extends RNG {

	@Override
	public boolean test(int successRate) {
		return false;
	}

	@Override
	public String getName() {
		return "Nullified";
	}

}
