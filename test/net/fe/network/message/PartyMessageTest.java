package net.fe.network.message;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import net.fe.unit.*;
import net.fe.modifier.*;

public final class PartyMessageTest {
	
	@Before
	public void before_loadItems() {
		WeaponFactory.loadWeapons();
		UnitFactory.loadUnits();
	}
	
	@Test
	public void test_acceptsEmptyParty() {
		new PartyMessage(new ArrayList<>()).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test
	public void test_acceptsSingleLord() {
		ArrayList<Unit> units = new ArrayList<>(2);
		units.add(UnitFactory.getUnit("Ike"));
		
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test
	public void test_acceptsSingleVassal() {
		ArrayList<Unit> units = new ArrayList<>(2);
		units.add(UnitFactory.getUnit("Lute"));
		
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test
	public void test_acceptsNormalInventory_Weapon() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(Item.getItem("Fire"));
			u.addToInventory(Item.getItem("Iron Sword"));
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test
	public void test_acceptsNormalInventory_Rise() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(Item.getItem("Rise"));
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test
	public void test_acceptsNormalInventory_Elixir() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(Item.getItem("Elixir"));
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_RejectsUnknownItem() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(new HealingItem("Gaius Confect", 5, 0, 100));
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_RejectsUnusualItem() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(new HealingItem("Iron Sword", -5, 0, 100));
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	//@Test(expected=IllegalStateException.class)
	public void test_RejectsUnusualUnit_Stats() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.debugStat("Str");
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_RejectsUnusualUnit_Triggers() {
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addSkill(new net.fe.fightStage.Lethality());
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_RejectsTooHighLevelUnits() {
		ArrayList<Unit> units = new ArrayList<>(2);
		for (int i = 0; i < 8; i++) {
			Unit u = UnitFactory.getUnit("Lute");
			u.setLevel(20);
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), new ArrayList<>());
	}
	
	@Test
	public void test_AcceptsVeterans() {
		ArrayList<Modifier> mods = new ArrayList<>(1);
		mods.add(new Veterans());
		ArrayList<Unit> units = new ArrayList<>(8);
		for (int i = 0; i < 8; i++) {
			Unit u = UnitFactory.getUnit("Lute");
			u.setLevel(20);
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, Item.getAllItems(), mods);
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_RejectsTooMuchSpent() {
		Item liquidGold = new HealingItem("Liquid Gold", 1, 0, net.fe.builderStage.TeamBuilderStage.FUNDS + 1);
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(liquidGold);
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, java.util.Collections.singleton(liquidGold), new ArrayList<>());
	}
	
	@Test
	public void test_AcceptsTreasury() {
		ArrayList<Modifier> mods = new ArrayList<>(1);
		mods.add(new Treasury());
		Item liquidGold = new HealingItem("Liquid Gold", 1, 0, net.fe.builderStage.TeamBuilderStage.FUNDS + 1);
		ArrayList<Unit> units = new ArrayList<>(2);
		{
			Unit u = UnitFactory.getUnit("Lute");
			u.addToInventory(liquidGold);
			units.add(u);
		}
		new PartyMessage(units).validateTeam(UnitFactory::getUnit, java.util.Collections.singleton(liquidGold), mods);
	}
	
}
