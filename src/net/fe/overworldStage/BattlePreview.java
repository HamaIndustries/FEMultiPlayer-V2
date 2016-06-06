package net.fe.overworldStage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Session;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.CombatTrigger;
import net.fe.fightStage.FightStage;
import net.fe.modifier.Modifier;
import net.fe.unit.ItemDisplay;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Animation;
import chu.engine.anim.Renderer;
import chu.engine.anim.Sprite;
import chu.engine.anim.Transform;

// TODO: Auto-generated Javadoc
/**
 * The Class BattlePreview.
 */
public class BattlePreview extends Entity {
	
	/** The defender. */
	private Unit attacker, defender;
	
	/** The sprites. */
	private ArrayList<Sprite> sprites;
	
	/** The mod down. */
	private Sprite leftArrow, rightArrow, x2, x4, modUp, modDown;
	
	/** The range. */
	private int range;
	
	/** The timer. */
	private float timer;

	/**
	 * Instantiates a new battle preview.
	 *
	 * @param x the x
	 * @param y the y
	 * @param a the a
	 * @param d the d
	 * @param range the range
	 */
	public BattlePreview(float x, float y, Unit a, Unit d, int range) {
		super(x, y);
//		System.out.println(d.getPartyColor());
		attacker = a;
		defender = d;
		sprites = new ArrayList<Sprite>();
		rightArrow = new Sprite();
		rightArrow.addAnimation("default",
				FEResources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 0.1f);
		leftArrow = new Sprite();
		leftArrow.addAnimation("default",
				FEResources.getTexture("gui_selectArrow"), 8, 8, 6, 6, 0.1f);
		x2 = new CirclingSprite(2.2f, 1.2f, true);
		x2.addAnimation("default", FEResources.getTexture("x2"));
		x4 = new CirclingSprite(2.2f, 1.2f, true);
		x4.addAnimation("default", FEResources.getTexture("x4"));
		
		modUp = new Sprite();
		Animation up = new Animation(FEResources.getTexture("trianglemod_up"), 7, 10, 3, 3, 0, 10, 0.15f, chu.engine.anim.BlendModeArgs.ALPHA_BLEND);
		modUp.addAnimation("default", up);
		modDown = new Sprite();
		Animation down = new Animation(FEResources.getTexture("trianglemod_down"), 7, 10, 3, 3, 0, 10, 0.15f, chu.engine.anim.BlendModeArgs.ALPHA_BLEND);
		modDown.addAnimation("default", down);
		sprites.add(rightArrow);
		sprites.add(leftArrow);
		sprites.add(x2);
		sprites.add(x4);
		sprites.add(modDown);
		sprites.add(modUp);
		this.range = range;
		this.renderDepth = ClientOverworldStage.MENU_DEPTH;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep() {
		for (Sprite s : sprites) {
			s.update();
		}
		timer += 3*Math.PI/2*Game.getDeltaSeconds();
		if(timer > 2*Math.PI)timer -= 2*Math.PI;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		String aHit = " - ", aAtk = " - ", aCrit = " - ", dHit = " - ", dAtk = " - ", dCrit = " - ";
		int aMult = 1;
		int dMult = 1;
		String aHp = String.format("%d", attacker.getHp());
		String dHp = String.format("%d", defender.getHp());
		Transform flip = new Transform();
		flip.flipHorizontal();
		
		int triMod = attacker.getWeapon().triMod(defender.getWeapon());
		boolean aEffective = false;
		boolean dEffective = false;
		
		//attacker determination
		if (CombatCalculator.shouldAttack(attacker, defender, attacker.getWeapon(), range)) {
			List<String> mods = new ArrayList<String>();
			for(Modifier i : FEMultiplayer.getClient().getSession().getModifiers()){
				mods.add(i.toString());
			}
			if(mods.contains("Vegas")){
				aHit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, CombatCalculator.hitRate(attacker, defender)))/2);
				aAtk = String.format("%d",
						CombatCalculator.calculatePreviewDamage(attacker, defender));
				aCrit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, attacker.crit() - defender.dodge()))*2);
				if(attacker.getStats().spd >= defender.getStats().spd + 4) aMult*=2;
				if(attacker.getWeapon().name.contains("Brave")) aMult*=2;
				aEffective = attacker.getWeapon().effective.contains(defender.noGenderName());
			}else{// normal
				aHit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, CombatCalculator.hitRate(attacker, defender))));
				aAtk = String.format("%d",
						CombatCalculator.calculatePreviewDamage(attacker, defender));
				aCrit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, attacker.crit() - defender.dodge())));
				if(attacker.getStats().spd >= defender.getStats().spd + 4) aMult*=2;
				if(attacker.getWeapon().name.contains("Brave")) aMult*=2;
				aEffective = attacker.getWeapon().effective.contains(defender.noGenderName());
			}
		}
		
		//defender determination
		if (CombatCalculator.shouldAttack(defender, attacker, defender.getWeapon(), range)) {
			List<String> mods = new ArrayList<String>();
			for(Modifier i : FEMultiplayer.getClient().getSession().getModifiers()){
				mods.add(i.toString());
			}
			if(mods.contains("Vegas")){
				dHit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, CombatCalculator.hitRate(defender, attacker)))/2);
				dAtk = String.format("%d",
						CombatCalculator.calculatePreviewDamage(defender, attacker));
				dCrit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, defender.crit() - attacker.dodge()))*2);
				if(defender.getStats().spd >= attacker.getStats().spd + 4) dMult*=2;
				if(defender.getWeapon().name.contains("Brave")) dMult*=2;
				dEffective = defender.getWeapon().effective.contains(attacker.noGenderName());
			}else if(mods.contains("Pro Tactics")){
				dHit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, CombatCalculator.hitRate(defender, attacker))));
				dAtk = String.format("%d",
						CombatCalculator.calculatePreviewDamage(defender, attacker)/2);
				dCrit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, defender.crit() - attacker.dodge())));
				if(defender.getStats().spd >= attacker.getStats().spd + 4) dMult*=2;
				if(defender.getWeapon().name.contains("Brave")) dMult*=2;
				dEffective = defender.getWeapon().effective.contains(attacker.noGenderName());
			}else{// normal
				dHit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, CombatCalculator.hitRate(defender, attacker))));
				dAtk = String.format("%d",
						CombatCalculator.calculatePreviewDamage(defender, attacker));
				dCrit = String.format(
						"%d",
						Math.max(0,
								Math.min(100, defender.crit() - attacker.dodge())));
				if(defender.getStats().spd >= attacker.getStats().spd + 4) dMult*=2;
				if(defender.getWeapon().name.contains("Brave")) dMult*=2;
				dEffective = defender.getWeapon().effective.contains(attacker.noGenderName());
			}
		}

		// Borders
		Renderer.drawRectangle(x - 1, y - 1, x + 91, y + 132, renderDepth,
				FightStage.BORDER_DARK);
		Renderer.drawRectangle(x, y, x + 90, y + 35, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x + 59, y + 35, x + 90, y + 96, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x, y + 35, x + 31, y + 96, renderDepth,
				FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(x, y + 96, x + 90, y + 131, renderDepth,
				FightStage.BORDER_LIGHT);

		// Top item and name
		Renderer.drawRectangle(x + 1, y + 1, x + 89, y + 34, renderDepth,
				attacker.getPartyColor());
		Renderer.drawString("default_med", attacker.name, x + 3, y + 3,
				renderDepth);
		new ItemDisplay((int) x + 7, (int) y + 16, attacker.getWeapon(), false)
				.render(null, aEffective, timer);
		leftArrow.render(x, y + 21, renderDepth);
		rightArrow.render(x + 90, y + 21, renderDepth, flip);
		
		if(triMod > 0) {
			modUp.render(x+16, y+32, 0.04f);
		} else if(triMod < 0) {
			modDown.render(x+16, y+32, 0.04f);
		}

		// Attacker stats

		Renderer.drawRectangle(x + 60, y + 34, x + 89, y + 96, renderDepth,
				attacker.getPartyColor());
		Renderer.drawString("default_med", aHp, x + 63, y + 36, renderDepth);
		Renderer.drawString("default_med", aAtk, x + 63, y + 51, renderDepth);
		Renderer.drawString("default_med", aHit, x + 63, y + 66, renderDepth);
		Renderer.drawString("default_med", aCrit, x + 63, y + 81, renderDepth);
		if(aMult == 2){
			x2.render(x+78, y+50, renderDepth);
		} else if (aMult == 4){
			x4.render(x+78, y+50, renderDepth);
		}

		// Stats text
		Renderer.drawRectangle(x + 31, y + 35, x + 59, y + 96, renderDepth,
				FightStage.NEUTRAL);
		Renderer.drawString("default_med", "HP", x + 37, y + 36, renderDepth);
		Renderer.drawString("default_med", "Atk", x + 36, y + 51, renderDepth);
		Renderer.drawString("default_med", "Hit", x + 36, y + 66, renderDepth);
		Renderer.drawString("default_med", "Crt", x + 36, y + 81, renderDepth);

		// Defender stats
		Renderer.drawRectangle(x + 1, y + 35, x + 30, y + 97, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", dHp, x + 4, y + 36, renderDepth);
		Renderer.drawString("default_med", dAtk, x + 4, y + 51, renderDepth);
		Renderer.drawString("default_med", dHit, x + 4, y + 66, renderDepth);
		Renderer.drawString("default_med", dCrit, x + 4, y + 81, renderDepth);
		if(dMult == 2){
			x2.render(x+19, y+49, renderDepth);
		} else if (dMult == 4){
			x4.render(x+19, y+49, renderDepth);
		}

		// Bottom item and name
		Renderer.drawRectangle(x+1, y + 97, x + 89, y + 130, renderDepth,
				defender.getPartyColor());
		Renderer.drawString("default_med", defender.name, x + 3, y + 99,
				renderDepth);
		new ItemDisplay((int) x + 7, (int) y + 112, defender.getWeapon(), false)
				.render(null, dEffective, timer);
		
		if(triMod < 0) {
			modUp.render(x + 16, y + 128, 0.04f);
		} else if(triMod > 0) {
			modDown.render(x + 16, y + 128, 0.04f);
		}
	}
	
	/**
	 * The Class CirclingSprite.
	 */
	private class CirclingSprite extends Sprite {
		
		/** The timer. */
		private float timer;
		
		/** The radius. */
		private float radius;
		
		/** The period. */
		private float period;
		
		/** The clockwise. */
		private boolean clockwise;
		
		/**
		 * Instantiates a new circling sprite.
		 *
		 * @param radius the radius
		 * @param period the period
		 * @param clockwise the clockwise
		 */
		public CirclingSprite(float radius, float period, boolean clockwise) {
			super();
			timer = 0;
			this.radius = radius;
			this.period = period;
			this.clockwise = clockwise;
		}
		
		/* (non-Javadoc)
		 * @see chu.engine.anim.Sprite#update()
		 */
		public void update() {
			super.update();
			timer += Game.getDeltaSeconds();
			if(timer > period) timer -= period;
		}
		
		/* (non-Javadoc)
		 * @see chu.engine.anim.Sprite#render(float, float, float)
		 */
		public void render(float x, float y, float depth) {
			double radians = timer/period*2*Math.PI;
			if(!clockwise) radians *= -1;
			super.render((float)(x+radius*Math.cos(radians)), (float)(y+radius*Math.sin(radians)), depth);
		}
	}
}
