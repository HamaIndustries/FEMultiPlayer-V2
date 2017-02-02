package net.fe.game.unit;

/**
 * A record of a unit's contributions to a battle
 */
public final class BattleStats implements java.io.Serializable {
	public final int kills;
	public final int assists;
	public final int damage;
	public final int healing;

	/** Creates a Statistics full of zero values */
	public BattleStats() {
		this(0, 0, 0, 0);
	}

	public BattleStats(int kills, int assists, int damage, int healing) {
		this.kills = kills;
		this.assists = assists;
		this.damage = damage;
		this.healing = healing;
	}

	public BattleStats plus(BattleStats other) {
		return new BattleStats(this.kills + other.kills, this.assists + other.assists, this.damage + other.damage,
		        this.healing + other.healing);
	}

	@Override
	public int hashCode() {
		return ((kills * 31 + assists) * 31 + damage) * 31 + healing;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BattleStats) {
			BattleStats o2 = (BattleStats) other;

			return this.kills == o2.kills && this.assists == o2.assists && this.damage == o2.damage
			        && this.healing == o2.healing;
		} else {
			return false;
		}
	}
}
