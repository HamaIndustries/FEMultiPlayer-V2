package chu.engine;




// TODO: Auto-generated Javadoc
/**
 * The Class Particle.
 */
//Entity with a specific lifetime. It removes itself after its lifetime expires.
public class Particle extends Entity {
	
	/** The timer. */
	private float timer;
	
	/** The lifetime. */
	private int lifetime;
	
	/**
	 * Instantiates a new particle.
	 *
	 * @param x the x
	 * @param y the y
	 * @param lifetime the lifetime
	 */
	public Particle(float x, float y, int lifetime) {
		super(x,y);
		this.lifetime = lifetime;
		timer = 0;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		super.onStep();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#beginStep()
	 */
	@Override
	public void beginStep() {
		timer += Game.getDeltaSeconds();
		if(timer > lifetime) {
			this.destroy();
		}
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#endStep()
	 */
	@Override
	public void endStep() {
		
	}

}
