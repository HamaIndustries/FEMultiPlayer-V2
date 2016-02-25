package net.fe.unit;

import java.util.Arrays;

public class Voice extends Weapon {
	
	static final long serialVersionUID = 1L;
	
	public Voice(){
		super("Voice");
		id = 74;
		pref = "Azura";
		type = Weapon.Type.STAFF;
		range = Arrays.asList(new Integer[] {1});
		this.setMaxUses(1);
	}
}
