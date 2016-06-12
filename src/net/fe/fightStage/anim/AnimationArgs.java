package net.fe.fightStage.anim;

import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.Weapon.Type;
import static java.lang.System.out;

// TODO: Auto-generated Javadoc
/**
 * The Class AnimationArgs.
 */
public class AnimationArgs {
	
	/** The userclass. */
	public String userclass;
	
	/** The wep anim name. */
	public String wepAnimName;
	
	/** The classification. */
	public String classification;
	
	/** The left. */
	public boolean left;
	
	/** The unit. */
	public Unit unit;
	
	/** The range. */
	public int range;
	
	/**
	 * Instantiates a new animation args.
	 *
	 * @param u the u
	 * @param left the left
	 * @param range the range
	 */
	public AnimationArgs(Unit u, boolean left, int range){
		userclass = u.functionalClassName();
		this.left = left;
		this.range = range;
		unit = u;
		Weapon w = u.getWeapon();
		if(w == null){
			Type wType = u.getTheClass().usableWeapon.get(0);
			if(wType.isMagic()){
				wepAnimName = "magic";
				classification = "magic";
			} else {
				wepAnimName = wType.toString().toLowerCase();
				classification = "normal";
			}
		}else if(w.isMagic()){
			wepAnimName = "magic";
			classification = "magic";
		} else {
			wepAnimName = w.type.toString().toLowerCase();
			classification = "normal";
			if(w.range.apply(u.getStats()).contains(range) && range > 1){
				if(w.type == Weapon.Type.AXE){
					wepAnimName = "handaxe";
					classification = "ranged";
				}
				if (w.type == Weapon.Type.LANCE){
					wepAnimName = "javelin";
					classification = "ranged";
				} 
				if (w.type == Weapon.Type.SWORD){
					wepAnimName = "rangedsword";
					classification = "ranged";
				}
			}
			if (w.type == Weapon.Type.BOW){
				this.classification = "ranged";
			}
			if (w.type == Weapon.Type.CROSSBOW){
				wepAnimName = "bow";
				this.classification = "ranged";
			}
		}
	}
}
