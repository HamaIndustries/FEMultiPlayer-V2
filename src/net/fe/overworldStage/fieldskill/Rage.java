package net.fe.overworldStage.fieldskill;

import java.io.Serializable;
import java.util.Set;

import net.fe.fightStage.RagePp;
import net.fe.unit.Unit;
import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Zone.ZoneType;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Path;
import net.fe.overworldStage.context.AttackTarget;

/**
 * Initiate an attack with a RagePp skill.
 */
public final class Rage extends FieldSkill {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	public Rage() {
	}
	
	@Override
	public boolean allowed(Unit unit, Grid grid) {
		if (unit.getSkillCharge() < 3) return false;
		
		Set<Node> range = grid.getRange(
			new Node(unit.getXCoord(), unit.getYCoord()),
			unit.getTotalWepRange(false)
		);
		for (Node n : range) {
			Unit p = grid.getVisibleUnit(n.x, n.y);
			if (p != null && !unit.getParty().isAlly(p.getParty())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean allowedWithFog(Unit unit, Grid grid) {
		return this.allowed(unit, grid);
	}
	
	/**
	 * Returns the context to start when this command is selected
	 */
	@Override
	public OverworldContext onSelect(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		return new AttackTarget(stage, context, z, unit, java.util.Collections.singletonList(new RagePp()));
	}
	
	@Override
	public Zone getZone(Unit unit, Grid grid) {
		return new Zone(
			grid.getRange(
				new Node(unit.getXCoord(), unit.getYCoord()),
				unit.getTotalWepRange(false)
			),
			ZoneType.ATTACK_DARK
		);
	}
	
	@Override
	public int hashCode() { return (int) serialVersionUID; }
	@Override
	public boolean equals(Object other) { return other instanceof Smite; }
}
