package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class Aether.
 */
public class Aether extends CombatTrigger {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -618799965651832966L;

	/** The phase. */
	private transient int phase;
	
	/** The Constant SOL. */
	private static final int SOL = 0;
	
	/** The Constant LUNA. */
	private static final int LUNA = 1;
	
	
	/**
	 * Instantiates a new aether.
	 */
	public Aether(){
		super(REPLACE_NAME_AFTER_PRE, YOUR_TURN_PRE + YOUR_TURN_POST + YOUR_TURN_DRAIN,
				"aether1", "aether2");
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#attempt(net.fe.unit.Unit, int)
	 */
	@Override
	public boolean attempt(Unit user, int range) {
		//return true;
		return range == 1 && (RNG.get() < user.get("Skl")/2 || phase != SOL);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPreAttack(net.fe.fightStage.CombatCalculator, net.fe.unit.Unit, net.fe.unit.Unit)
	 */
	@Override
	public boolean runPreAttack(CombatCalculator calc, Unit a, Unit d) {
		if(phase == LUNA){
			new Luna(false).runPreAttack(calc, a, d);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runDrain(net.fe.unit.Unit, net.fe.unit.Unit, int)
	 */
	@Override
	public int runDrain(Unit a, Unit d, int damage){
		if(phase == SOL){
			if(damage == 0) return 0;
			return Math.min(damage/2, a.get("HP") - a.getHp());
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#runPostAttack(net.fe.fightStage.CombatCalculator, boolean, net.fe.unit.Unit, net.fe.unit.Unit, int, java.lang.String)
	 */
	@Override
	public void runPostAttack(CombatCalculator calc, boolean dir, Unit a, Unit d,
			int damage, String currentEffect) {
		if(phase == SOL){
			if(d.getHp() > 0){
				phase = LUNA;
				calc.addAttack("Aether");
			}
		} else {
			phase = SOL;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getName()
	 */
	public String getName(){
		return "Aether" + (phase + 1);
	}
	
	/* (non-Javadoc)
	 * @see net.fe.fightStage.CombatTrigger#getCopy()
	 */
	public CombatTrigger getCopy(){
		return new Aether();
	}
}
