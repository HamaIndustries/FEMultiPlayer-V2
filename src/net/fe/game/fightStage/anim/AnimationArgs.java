package net.fe.game.fightStage.anim;

import net.fe.game.unit.Unit;
import net.fe.game.unit.Weapon;
import net.fe.game.unit.Weapon.Type;

public final class AnimationArgs {

	public final String userclass;

	public final String wepAnimName;

	public final String classification;

	public final boolean left;

	public final Unit unit;

	public final int range;

	/**
	 * Instantiates a new animation args.
	 *
	 * @param u the attacking unit
	 * @param left whether the unit is on the left size
	 * @param range the distance between the attacker and defender
	 */
	public AnimationArgs(Unit u, boolean left, int range) {
		userclass = u.functionalClassName();
		this.left = left;
		this.range = range;
		unit = u;
		Weapon w = u.getWeapon();
		if (w == null) {
			Type wType = u.getUnitClass().usableWeapon.get(0);
			if (wType.isMagic()) {
				wepAnimName = "magic";
				classification = "magic";
			} else if (wType == Weapon.Type.CROSSBOW) {
				wepAnimName = "bow";
				classification = "normal";
			} else {
				wepAnimName = wType.toString().toLowerCase();
				classification = "normal";
			}
		} else if (w.isMagic()) {
			wepAnimName = "magic";
			classification = "magic";
		} else {
			if (w.range.apply(u.getStats()).contains(range) && range > 1) {
				classification = "ranged";
				switch (w.type) {
					case AXE:
						wepAnimName = "handaxe";
						break;
					case LANCE:
						wepAnimName = "javelin";
						break;
					case SWORD:
						wepAnimName = "rangedsword";
						break;
					case BOW:
						wepAnimName = "bow";
						break;
					case CROSSBOW:
						wepAnimName = "bow";
						break;
					default:
						wepAnimName = "magic";
						break;
				}
			} else {
				classification = "normal";
				if (w.type == Weapon.Type.CROSSBOW) {
					wepAnimName = "bow";
				} else if (w.type == Weapon.Type.AXE && w.range.apply(u.getStats()).contains(2)) {
					wepAnimName = "handaxe";
				} else {
					wepAnimName = w.type.toString().toLowerCase();
				}
			}
		}
	}
}
