package net.fe.game.modifier;

import java.util.function.Consumer;
import java.util.function.Function;

import net.fe.builderStage.TeamBuilderResources;
import net.fe.game.fightStage.skill.GamblePlus;
import net.fe.game.fightStage.skill.MiraclePlus;
import net.fe.game.fightStage.skill.PavisePlus;
import net.fe.game.unit.Item;
import net.fe.game.unit.Unit;
import net.fe.game.unit.Weapon;

public enum ModifierList implements Modifier {
	DIVINE_INTERVENTION("Divine intervention", "All units have a version of Miracle that is guarenteed to activate.", (Unit u) -> u.addSkill(new MiraclePlus())),
	
	MADE_IN_CHINA("Made in China", "All weapons have greatly reduced durability. Start with extra gold.",
			r -> r.copyWithNewFunds((i) -> i * 2), s -> s, u -> u.getInventory().forEach(x -> {if(x instanceof Weapon) x.setUsesDEBUGGING(2);})),
	
	PRO_TACTICS("Pro Tactics", "Halves damage to better emulate traditional GBA games. All units get 100% Pavise.", (Unit u) -> u.addSkill(new PavisePlus())),
	
	SUDDEN_DEATH("Sudden Death", "All units start at 1 HP.", (Unit u) -> u.setMaxHp(1)),
	
	TREASURY("Treasury", "Start with the maximum amount of gold.", (RessourcesModifier) limits -> limits.copyWithNewFunds(99999900)),
	
	VEGAS("Vegas", "Gamble! All units have halved hit rates and doubled crit rates.", (Unit u) -> u.addSkill(new GamblePlus())),
	
	VETERANS("Veterans", "Unlimited starting EXP.", (RessourcesModifier) limits -> limits.copyWithNewExp(999999999));
	
	private ModifierList(String name, String description){
		this(name, description, x -> x, x -> x, u -> {});
	}
	
	private ModifierList(String name, String description, RessourcesModifier modifyRessources){
		this(name, description, modifyRessources, x -> x, u -> {});
	}
	
	private ModifierList(String name, String description, ShopModifier modifyShop){
		this(name, description, x -> x, modifyShop, u -> {});
	}
	
	private ModifierList(String name, String description, Consumer<Unit> modifyUnit){
		this(name, description, x -> x, x -> x, modifyUnit);
	}
	
	private ModifierList(String name, String description, 
			RessourcesModifier modifyRessources, ShopModifier modifyShop, Consumer<Unit> modifyUnit){
		this.name = name;
		this.description = description;
		this.modifyRessources = modifyRessources;
		this.modifyShop = modifyShop;
		this.modifyUnit = modifyUnit;
	}
	
	
	private String name;
	private String description;
	private RessourcesModifier modifyRessources;
	private ShopModifier modifyShop;
	private Consumer<Unit> modifyUnit;
	
	@Override
	public TeamBuilderResources modifyTeamResources(TeamBuilderResources limits) {
		return modifyRessources.apply(limits);
	}

	@Override
	public Iterable<Item> modifyShop(Iterable<Item> shop) {
		return modifyShop.apply(shop);
	}

	@Override
	public void initOverworldUnits(Iterable<Unit> units) {
		for(Unit u : units)
			modifyUnit.accept(u);
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public String toString(){
		return name;
	}

	//Blame type erasure
	private interface RessourcesModifier extends Function<TeamBuilderResources, TeamBuilderResources> {}
	private interface ShopModifier extends Function<Iterable<Item>, Iterable<Item>> {}
}
