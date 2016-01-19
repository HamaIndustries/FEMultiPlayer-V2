package net.fe.unit;

public class RiseTome extends Item{
	private static final long serialVersionUID = -2L;
	
	public RiseTome(){
		super("Rise");
		this.setMaxUses(4);
		this.id = 70;
		this.setCost(8000);
	}
	
	public RiseTome getCopy(){
		RiseTome w = new RiseTome();
		return w;
	}
	
	public int compareTo(Item that) {
		if (that instanceof RiseTome) {
			return 0;
		} else {
			return 1;
		}
	}
}
