package net.fe.unit;

import java.util.Map;

/**
 * A unit's combat capabilities
 */
public final class Statistics {
	public final int maxHp;
	public final int str;
	public final int mag;
	public final int skl;
	public final int spd;
	public final int def;
	public final int res;
	public final int lck;
	public final int mov;
	public final int con;
	public final int aid;
	
	/** Creates a Statistics full of zero values */
	public Statistics() { this(0,0,0,0,0,0,0,0,0,0,0); }
	
	@Deprecated
	public Statistics(Map<String, Integer> map) {
		this(
			map.getOrDefault("HP", 0),
			map.getOrDefault("Str", 0),
			map.getOrDefault("Mag", 0),
			map.getOrDefault("Skl", 0),
			map.getOrDefault("Spd", 0),
			map.getOrDefault("Def", 0),
			map.getOrDefault("Res", 0),
			map.getOrDefault("Lck", 0),
			map.getOrDefault("Mov", 0),
			map.getOrDefault("Con", 0),
			map.getOrDefault("Aid", 0)
		);
	}
	
	public Statistics(
		int maxHp,
		int str,
		int mag,
		int skl,
		int spd,
		int def,
		int res,
		int lck,
		int mov,
		int con,
		int aid
	) {
		this.maxHp = maxHp;
		this.str = str;
		this.mag = mag;
		this.skl = skl;
		this.spd = spd;
		this.def = def;
		this.res = res;
		this.lck = lck;
		this.mov = mov;
		this.con = con;
		this.aid = aid;
	}
	
	public Statistics copy(String statName, int value) {
		Statistics retVal = new Statistics(
			("HP".equals(statName) ? value : this.maxHp),
			("Str".equals(statName) ? value : this.str),
			("Mag".equals(statName) ? value : this.mag),
			("Skl".equals(statName) ? value : this.skl),
			("Spd".equals(statName) ? value : this.spd),
			("Def".equals(statName) ? value : this.def),
			("Res".equals(statName) ? value : this.res),
			("Lck".equals(statName) ? value : this.lck),
			("Mov".equals(statName) ? value : this.mov),
			("Con".equals(statName) ? value : this.con),
			("Aid".equals(statName) ? value : this.aid)
		);
		return retVal;
	}
	
	public Statistics plus(Statistics other) {
		return new Statistics(
			this.maxHp + other.maxHp,
			this.str + other.str,
			this.mag + other.mag,
			this.skl + other.skl,
			this.spd + other.spd,
			this.def + other.def,
			this.res + other.res,
			this.lck + other.lck,
			this.mov + other.mov,
			this.con + other.con,
			this.aid + other.aid
		);
	}
	
	public Statistics min(Statistics other) {
		return new Statistics(
			Math.min(this.maxHp, this.maxHp),
			Math.min(this.str, this.str),
			Math.min(this.mag, this.mag),
			Math.min(this.skl, this.skl),
			Math.min(this.spd, this.spd),
			Math.min(this.def, this.def),
			Math.min(this.res, this.res),
			Math.min(this.lck, this.lck),
			Math.min(this.mov, this.mov),
			Math.min(this.con, this.con),
			Math.min(this.aid, this.aid)
		);
	}
	
	public Statistics times(float multiplicand) {
		return new Statistics(
			(int)(this.maxHp * multiplicand),
			(int)(this.str * multiplicand),
			(int)(this.mag * multiplicand),
			(int)(this.skl * multiplicand),
			(int)(this.spd * multiplicand),
			(int)(this.def * multiplicand),
			(int)(this.res * multiplicand),
			(int)(this.lck * multiplicand),
			(int)(this.mov * multiplicand),
			(int)(this.con * multiplicand),
			(int)(this.aid * multiplicand)
		);
	}
	
	@Override
	public int hashCode() {
		// Terrible hashCode implementation, but satisfies requirement
		// that if `a.equals(b)` then `a.hashCode() == b.hashCode()`
		return str;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Statistics) {
			Statistics o2 = (Statistics) other;
			
			return 
				this.maxHp == o2.maxHp &&
				this.str == o2.str &&
				this.mag == o2.mag &&
				this.skl == o2.skl &&
				this.spd == o2.spd &&
				this.def == o2.def &&
				this.res == o2.res &&
				this.lck == o2.lck &&
				this.mov == o2.mov &&
				this.con == o2.con &&
				this.aid == o2.aid;
		} else {
			return false;
		}
	}
	
	// TODO: Get rid of this
	public Map<String, Integer> toMap() {
		Map<String, Integer> retVal = new java.util.HashMap<>();
		retVal.put("HP", maxHp);
		retVal.put("Str", str);
		retVal.put("Mag", mag);
		retVal.put("Skl", skl);
		retVal.put("Spd", spd);
		retVal.put("Def", def);
		retVal.put("Res", res);
		retVal.put("Lck", lck);
		retVal.put("Mov", mov);
		retVal.put("Con", con);
		retVal.put("Aid", aid);
		return retVal;
	}
}
