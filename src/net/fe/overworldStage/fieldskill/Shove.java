package net.fe.overworldStage.fieldskill;

import java.io.Serializable;
import java.util.Set;

import net.fe.Command;
import net.fe.unit.Unit;
import net.fe.overworldStage.FieldSkill;
import net.fe.overworldStage.Node;
import net.fe.overworldStage.OverworldContext;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.Zone;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Path;

/**
 * A skill in which a unit moves an adjacent unit one space away from the user
 */
public final class Shove extends FieldSkill {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6468268282716381357L;
	
	/**
	 * A skill that can be used in the overworld
	 */
	public Shove() {
	}
	
	/**
	 * Checks whether the unit is capable of shiving anyone
	 * @param unit the unit to check
	 * @param grid the grid containing the unit
	 */
	@Override
	public boolean allowed(Unit unit, Grid grid) {
		Set<Node> range = grid.getRange(new Node(unit.getXCoord(), unit.getYCoord()), 1);
		for (Node n : range) {
			Unit shovee = grid.getUnit(n.x, n.y);
			if (shovee != null) {
				if (canShove(grid, unit, shovee)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the context to start when this command is selected
	 */
	@Override
	public OverworldContext onSelect(ClientOverworldStage stage, OverworldContext context, Zone z, Unit unit) {
		return new net.fe.overworldStage.context.ShoveTarget(stage, context, z, unit);
	}
	
	@Override
	public Zone getZone(Unit unit, Grid grid) {
		return new Zone(grid.getRange(
					new Node(unit.getXCoord(), unit.getYCoord()), 1),
					Zone.MOVE_DARK);
	}
	
	/**
	 * Returns true if the shover is allowed to shove the shovee
	 */
	public static boolean canShove(Grid grid, Unit shover, Unit shovee) {
		int deltaX = shovee.getXCoord() - shover.getXCoord();
		int deltaY = shovee.getYCoord() - shover.getYCoord();
		
		int shoveToX = shover.getXCoord() + 2 * deltaX;
		int shoveToY = shover.getYCoord() + 2 * deltaY;
		
		return (
			(Math.abs(deltaX) + Math.abs(deltaY)) == 1 && 
			grid.contains(shoveToX, shoveToY) &&
			grid.getTerrain(shoveToX, shoveToY).getMoveCost(shovee.getTheClass()) < shovee.get("Mov") &&
			null == grid.getUnit(shoveToX, shoveToY) &&
			shovee.get("Con") - 2 <= shover.get("Con")
		);
	}
}
