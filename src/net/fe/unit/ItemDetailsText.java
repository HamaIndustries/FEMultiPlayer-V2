package net.fe.unit;

import java.util.ArrayList;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

import net.fe.fightStage.Brave;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.EclipseSix;
import net.fe.fightStage.LunaPlus;
import net.fe.fightStage.Nosferatu;
import net.fe.fightStage.CrossBow;

/**
 * An entity which displays details about an item.
 * This entity does not include a background.
 */
public final class ItemDetailsText extends Entity{
	/** The item to display details about. */
	private Item item;
	
	
	/**
	 * Instantiates a new item display.
	 *
	 * @param x the upper-left x coordinate
	 * @param y the upper-left y coordinate
	 */
	public ItemDetailsText(float x, float y) {
		super(x,y);
		this.width = 260;
		this.height = 52;
		this.renderDepth = 0.05f;
		this.item = null;
	}
	
	@Override
	public void render(){
		if (item != null) {
			Renderer.drawString("default_med", item.name, this.x + 2, this.y + 4, this.renderDepth);
			
			if (item instanceof HealingItem) {
				HealingItem potion = (HealingItem) item;
				Renderer.drawString("default_med", "Heals " + potion.amount + " HP", this.x + 8, this.y + 28, this.renderDepth);
			} if (item instanceof SkillChargingItem) {
				SkillChargingItem potion = (SkillChargingItem) item;
				Renderer.drawString("default_med", "Increases Rage by " + potion.amount, this.x + 8, this.y + 28, this.renderDepth);
			} else if (item instanceof RiseTome){
				RiseTome rise = (RiseTome) item;
				Renderer.drawString("default_med", "Summons a phantom warrior", this.x + 8, this.y + 28, this.renderDepth);
			} else if (item instanceof Weapon){
				Weapon wep = (Weapon) item;
				{	// When might is irrelevant, show "-" instead
					String s = (wep.getTriggers().contains(new EclipseSix()) ? "-" : "" + wep.mt);
					Renderer.drawString("default_med", "Mt " + s, this.x + 8, this.y + 20, this.renderDepth);
				}
				Renderer.drawString("default_med", "Hit " + wep.hit, this.x + 68, this.y + 20, this.renderDepth);
				Renderer.drawString("default_med", "Crit " + wep.crit, this.x + 128, this.y + 20, this.renderDepth);
				Renderer.drawString("default_med", "Rng " + wep.range.toString(), this.x + 188, this.y + 20, this.renderDepth);
				
				Renderer.drawString("default_med", getFlavorText(wep), this.x + 8, this.y + 36, this.renderDepth);
			}
		}
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	
	private static String getFlavorText(Weapon wep) {
		StringBuilder sb = new StringBuilder();
		
		if(wep.type == Weapon.Type.CROSSBOW) {
			sb.append("A Crossbow. ");
		}
		if(wep.name.contains("Brave")){
			sb.append("Allows double attacks. ");
		}
		if(wep.name.contains("reaver")){
			sb.append("Reverses the weapon triangle. ");
		}
		if(wep.name.contains("Kill") || wep.name.equals("Wo Dao")){
			sb.append("Has a high critical rate. ");
		}
		if(wep.getTriggers().contains(new CrossBow())) {
			sb.append("Ignores user's Str. ");
		}
		if(wep.getTriggers().contains(new EclipseSix())) {
			sb.append("Reduces enemy HP to 1. ");
		}
		if(wep.getTriggers().contains(new LunaPlus())) {
			sb.append("Ignores enemy resistance. ");
		}
		if(wep.getTriggers().contains(new Nosferatu())) {
			sb.append("Restores user HP by half of damage dealt. ");
		}
		if(wep.getCost() == 10000){
			sb.append("A legendary weapon. ");
		}
		if(wep.getCost() == 15000){
			sb.append("Ultimate magic. ");
		}
		if(wep.pref != null) {
			sb.append(wep.pref + " only. ");
		}
		for(String stat: wep.modifiers.toMap().keySet()){
			if(wep.modifiers.toMap().get(stat) != 0){
				sb.append(stat + "+" + wep.modifiers.toMap().get(stat) + ". ");
			}
		}
		if(wep.effective.size() != 0){
			ArrayList<String> eff = new ArrayList<String>();
			if(wep.effective.contains("General")){
				eff.add("armored");
			}
			if(wep.effective.contains("Valkyrie")){
				eff.add("mounted");
			}
			if(wep.effective.contains("Falconknight")){
				eff.add("flying");
			}
			String effText = "";
			for(String s: eff){
				effText += ", " + s;
			}
			if(effText.length() != 0) {
				sb.append("Effective against " + effText.substring(2) + " units. ");
			}
		}
		
		return sb.toString();
	}
}
