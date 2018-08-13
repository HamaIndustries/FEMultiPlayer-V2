package net.fe.fightStage.anim;

import chu.engine.AnimationData;
import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;
import net.fe.unit.Weapon;
import net.fe.unit.Weapon.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class AnimationArgs.
 */
public final class AnimationArgs {
	
	/** The userclass. */
	public final String userclass;
	
	/** The wep anim name. */
	public final String wepAnimName;
	
	/** The classification. */
	public final AttackAnimationType classification;
	
	/** The left. */
	public final boolean left;
	
	/** The unit. */
	public final Unit unit;
	
	/** The range. */
	public final int range;
	
	/**
	 * Instantiates a new animation args.
	 *
	 * @param u the attacking unit
	 * @param left whether the unit is on the left size
	 * @param range the distance between the attacker and defender
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
				classification = AttackAnimationType.MAGIC;
			} else if (wType == Weapon.Type.CROSSBOW) {
				wepAnimName = "bow";
				classification = AttackAnimationType.NORMAL;
			} else {
				wepAnimName = wType.toString().toLowerCase();
				classification = AttackAnimationType.NORMAL;
			}
		}else if(w.isMagic()){
			wepAnimName = "magic";
			classification = AttackAnimationType.MAGIC;
		} else if (w.type == Weapon.Type.AXE && w.range.apply(u.getStats()).contains(2)) {
			classification = AttackAnimationType.HANDAXE;
			wepAnimName = "handaxe";
		} else {
			if (w.range.apply(u.getStats()).contains(range) && range > 1) {
				classification = AttackAnimationType.RANGED;
				switch (w.type) {
					case AXE: wepAnimName = "handaxe"; break;
					case LANCE: wepAnimName = "javelin"; break;
					case SWORD: wepAnimName = "rangedsword"; break;
					case BOW: wepAnimName = "bow"; break;
					case CROSSBOW: wepAnimName = "bow"; break;
					default: wepAnimName = "magic"; break;
				}
			} else {
				classification = AttackAnimationType.NORMAL;
				if (w.type == Weapon.Type.CROSSBOW) {
					wepAnimName = "bow";
				} else {
					wepAnimName = w.type.toString().toLowerCase();
				}
			}
		}
	}
	
	@Override public String toString() {
		return "AnimationArgs" +
			"[ userclass: " + userclass +
			", wepAnimName: " + wepAnimName +
			", classification: " + classification +
			", left: " + left +
			", unit: " + unit +
			", range: " + range +
		"]";
	}
	
	public static enum AttackAnimationType {
		NORMAL,
		RANGED,
		HANDAXE,
		MAGIC;
		
		public AttackAnimation construct(
			AnimationData data,
			FightStage stage,
			AnimationArgs animArgs
		) {
			switch (this) {
				case NORMAL: return new NormalAttack(data, stage, animArgs);
				case RANGED: return new ProjectileAttack(data, stage, animArgs);
				case HANDAXE: return new HandaxeAttack(data, stage, animArgs);
				case MAGIC: return new MagicAttack(data, stage, animArgs);
			}
			throw new IllegalStateException("Unknown AttackAnimationType: " + this);
		}
		
		public java.util.List<String> preloads(AnimationArgs args) {
			switch (this) {
				case NORMAL: return java.util.Collections.emptyList();
				case RANGED: return java.util.Collections.emptyList();
				case HANDAXE: return java.util.Arrays.asList(
					HandaxeAttack.TEXTURE_NAME,
					"handaxe_return"
				);
				case MAGIC: return java.util.Arrays.asList(
					MagicEffect.getTextureName(args),
					MagicAttack.getBgEffectName(args)
				);
			}
			return java.util.Collections.emptyList();
		}
	}
}
