package net.fe.fightStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import static java.lang.System.out;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.fightStage.anim.AnimationArgs;
import net.fe.fightStage.anim.AttackAnimation;
import net.fe.fightStage.anim.BackgroundEffect;
import net.fe.fightStage.anim.DodgeAnimation;
import net.fe.fightStage.anim.HUD;
import net.fe.fightStage.anim.HitEffect;
import net.fe.fightStage.anim.MagicAttack;
import net.fe.fightStage.anim.MagicEffect;
import net.fe.fightStage.anim.MissEffect;
import net.fe.fightStage.anim.NoDamageEffect;
import net.fe.fightStage.anim.Platform;
import net.fe.fightStage.anim.SkillIndicator;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.transition.FightOverworldTransition;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.SortByRender;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class FightStage.
 */
public class FightStage extends Stage {
	
	/** The right. */
	private Unit left, right;
	
	/** The right fighter. */
	private FightUnit leftFighter, rightFighter;
	
	/** The right hp. */
	private Healthbar leftHP, rightHP;
	
	/** The range. */
	private int range;
	
	/** The done. */
	private boolean done;

	/** The attack q. */
	private ArrayList<AttackRecord> attackQ;

	/** The bg. */
	private Texture bg;
	
	/** The current event. */
	private int currentEvent;
	
	/** The prev shake timer. */
	private float prevShakeTimer;
	
	/** The shake timer. */
	private float shakeTimer;
	
	/** The timer. */
	private float timer;
	
	/** The shake x. */
	private float shakeX;
	
	/** The shake y. */
	private float shakeY;
	
	/** The camera offset f. */
	private float cameraOffsetF;
	
	/** The camera offset t. */
	private float cameraOffsetT;
	
	/** The camera offset. */
	private float cameraOffset;
	
	/** The darkness. */
	private float darknessT, darkness;
	
	/** The Constant PRELOADED_EFFECTS. */
	public static final HashMap<String, Texture> PRELOADED_EFFECTS = new HashMap<String, Texture>();

	/** The Constant SHAKE_INTERVAL. */
	// Config
	public static final float SHAKE_INTERVAL = 0.05f;
	
	/** The Constant MIN_TIME. */
	public static final float MIN_TIME = 1.5f;

	/** The Constant CENTRAL_AXIS. */
	public static final int CENTRAL_AXIS = 120;
	
	/** The Constant FLOOR. */
	public static final int FLOOR = 105;

	/** The Constant BORDER_DARK. */
	public static final Color BORDER_DARK = new Color(0x483828);
	
	/** The Constant BORDER_LIGHT. */
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	
	/** The Constant NEUTRAL. */
	public static final Color NEUTRAL = new Color(0xb0a878);

	/** The Constant HP_DEPTH. */
	public static final float HP_DEPTH = 0.14f;
	
	/** The Constant HUD_DEPTH. */
	public static final float HUD_DEPTH = 0.15f;
	
	/** The Constant EFFECT_DEPTH. */
	public static final float EFFECT_DEPTH = 0.2f;
	
	/** The Constant UNIT_DEPTH. */
	public static final float UNIT_DEPTH = 0.5f;
	
	/** The Constant PLATFORM_DEPTH. */
	public static final float PLATFORM_DEPTH = 0.7f;
	
	/** The Constant BG_DEPTH. */
	public static final float BG_DEPTH = 1;

	/** The Constant START. */
	public static final int START = 0;
	
	/** The Constant ATTACKING. */
	public static final int ATTACKING = 1;
	
	/** The Constant HIT_EFFECT. */
	public static final int HIT_EFFECT = 2;
	
	/** The Constant HIT_EFFECTED. */
	public static final int HIT_EFFECTED = 3;
	
	/** The Constant ATTACKED. */
	public static final int ATTACKED = 4;
	
	/** The Constant HURTING. */
	public static final int HURTING = 5;
	
	/** The Constant HURTED. */
	public static final int HURTED = 6;
	
	/** The Constant DYING. */
	public static final int DYING = 7;
	
	/** The Constant RETURNING. */
	public static final int RETURNING = 8;
	
	/** The Constant DONE. */
	public static final int DONE = 9;
	
	/** The stage to return to after the Fight Stage plays out */
	private final ClientOverworldStage returnTo;

	/**
	 * Instantiates a new fight stage.
	 *
	 * @param u1 the u1
	 * @param u2 the u2
	 * @param attackQ the attack q
	 * @param returnTo the stage to return to after this stage has played its animation
	 */
	public FightStage(UnitIdentifier u1, UnitIdentifier u2,
			ArrayList<AttackRecord> attackQ, ClientOverworldStage returnTo) {
		super(u1.partyColor.equals(u2.partyColor) ? "curing" :
				u1.partyColor.equals(FEMultiplayer.getLocalPlayer().getParty().getColor()) ?
						"fight" : "defense");
		shakeTimer = 0;
		prevShakeTimer = 0;
		timer = 0;
		done = false;
		shakeX = 0;
		shakeY = 0;
		left = FEMultiplayer.getUnit(u1);
		right = FEMultiplayer.getUnit(u2);

		range = Grid.getDistance(left, right);
		cameraOffsetF = rangeToHeadDistance(range) - rangeToHeadDistance(1);
		leftFighter = new FightUnit(new AnimationArgs(left, true, range), this);
		rightFighter = new FightUnit(new AnimationArgs(right, false, range),
				this);
		leftHP = new Healthbar(left, true);
		rightHP = new Healthbar(right, false);
		addEntity(leftFighter);
		addEntity(rightFighter);
		addEntity(leftHP);
		addEntity(rightHP);
		addEntity(new Platform(left.getTerrain(), true, range));
		addEntity(new Platform(right.getTerrain(), false, range));
		addEntity(new HUD(left, right, this));
		addEntity(new HUD(right, left, this));
		bg = FEResources.getTexture(right.getTerrain().toString().toLowerCase()
				+ "_bg");

		this.attackQ = attackQ;
		this.returnTo = returnTo;
		preload();
	}
	
	/**
	 * Preload.
	 */
	public void preload(){
		preload(leftFighter);
		preload(rightFighter);
	}
	
	/**
	 * Preload.
	 *
	 * @param f the f
	 */
	public void preload(FightUnit f){
		AnimationArgs args = f.getAnimArgs();
		if(args.classification.equals("magic") && args.unit.getWeapon() != null){
			String name = MagicEffect.getTextureName(args);
			if(!PRELOADED_EFFECTS.containsKey(name)){
				System.out.print("PRE");
				PRELOADED_EFFECTS.put(name, FEResources.getTexture(name));
				name = MagicAttack.getBgEffectName(args);
				System.out.print("PRE");
				PRELOADED_EFFECTS.put(name, FEResources.getTexture(name));
			}
		}
		
		for(AttackRecord rec : attackQ){
			boolean crit = rec.animation.contains("Critical");
			for(String effectName : HitEffect.getEffectNames(args, rec)){
				String name = HitEffect.getHitTextureName(effectName, crit);
				if(!PRELOADED_EFFECTS.containsKey(name)){
					System.out.print("PRE");
					PRELOADED_EFFECTS.put(name, FEResources.getTexture(name));
				}
			}
		}
	}
	
	/**
	 * Gets the preload.
	 *
	 * @param name the name
	 * @return the preload
	 */
	public static Texture getPreload(String name){
		return PRELOADED_EFFECTS.get(name);
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#beginStep()
	 */
	@Override
	public void beginStep() {
		for (Entity e : entities) {
			e.beginStep();
		}
		timer += Game.getDeltaSeconds();
		if (attackQ.size() != 0) {
			processAttackQueue();
		} else if (!done && timer > MIN_TIME) {
			System.out.println(left.name + " HP:" + left.getHp() + " | "
					+ right.name + " HP:" + right.getHp());
			PRELOADED_EFFECTS.clear();
			FEMultiplayer.reportFightResults(this);
			addEntity(new FightOverworldTransition(returnTo, left,
					right));
			done = true;
		}
		processAddStack();
		processRemoveStack();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#onStep()
	 */
	@Override
	public void onStep() {
		for (Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
		float dx = Math.signum(cameraOffsetT - cameraOffset) * Game.getDeltaSeconds() * 400;
		cameraOffset += dx;
		if((cameraOffsetT - cameraOffset) * dx < 0){
			cameraOffset = cameraOffsetT;
		}
		float dd = Math.signum(darknessT - darkness);
		darkness += dd;
		if((darknessT - darkness) * dd < 0){
			darkness = darknessT;
		}
	}

	/**
	 * Process attack queue.
	 */
	private void processAttackQueue() {
		//TODO Weapon usage
		final AttackRecord rec = attackQ.get(0);
		Unit attacker = FEMultiplayer.getUnit(rec.attacker);
		Unit defender = FEMultiplayer.getUnit(rec.defender);

		FightUnit a = attacker == right ? rightFighter : leftFighter;
		FightUnit d = attacker == right ? leftFighter : rightFighter;
		Healthbar dhp = defender == left ? leftHP : rightHP;
		Healthbar ahp = defender == left ? rightHP : leftHP;

		a.renderDepth = FightStage.UNIT_DEPTH;
		d.renderDepth = FightStage.UNIT_DEPTH + 0.01f;
		
		List<HitEffect> hitEffects = HitEffect.getEffects(a.getAnimArgs(), rec, false);

		if (currentEvent == START) {
			// System.out.println("\n" + rec.attacker.name + "'s turn!");
			// //Debug
			if(attacker == right){
				cameraOffsetT = -cameraOffsetF;
			} else {
				cameraOffsetT = cameraOffsetF;
			}
			ArrayList<String> messages = analyzeAnimation(rec.animation, "(a)", true);
			for (int i = 0; i < messages.size(); i++) {
				addEntity(new SkillIndicator(messages.get(i), attacker == left, i));
			}
			a.setAnimation(rec.animation);
			try{
				a.sprite.setSpeed(((AttackAnimation)a.sprite.getCurrentAnimation()).getDefaultSpeed());
			}catch(ClassCastException e){
				a.sprite.setSpeed(((DodgeAnimation)a.sprite.getCurrentAnimation()).getDefaultSpeed());
			}
			a.sprite.setFrame(0);
			
			d.sprite.setAnimation("ATTACK");
			d.sprite.setFrame(0);
			d.sprite.setSpeed(0);
			
			setCurrentEvent(ATTACKING);
		} else if (currentEvent == ATTACKING) {
			// Let the animation play
		} else if (currentEvent == HIT_EFFECT){
			if(defender == right){
				cameraOffsetT = -cameraOffsetF;
			} else {
				cameraOffsetT = cameraOffsetF;
			}
			
			for (HitEffect h : HitEffect.getEffects(a.getAnimArgs(), rec, true)) {
				addEntity(h);
			}
			if(hitEffects.size() == 0){
				currentEvent = ATTACKED;
			} else {
				currentEvent = HIT_EFFECTED;
			}
			processAddStack();
			
		
		} else if (currentEvent == HIT_EFFECTED){
			//Let anim play
		} else if (currentEvent == ATTACKED) {
			if (rec.damage == 0) {
				// System.out.println("Miss! " + rec.defender.name
				// + " dodged the attack!");
				if (rec.animation.contains("Miss")){
					addEntity(new MissEffect(defender == left));
					d.sprite.setAnimation("DODGE");
					d.sprite.setFrame(0);
					d.sprite.setSpeed(DodgeAnimation.NORMAL_SPEED);
					if(attacker.getWeapon().isMagic()){
						attacker.use(attacker.getWeapon());
					}
				}else{
					addEntity(new NoDamageEffect(defender == left));
					attacker.use(attacker.getWeapon());
				}
				setCurrentEvent(HURTING);
			} else {
				// System.out.println(rec.animation + "! " + rec.defender.name
				// + " took " + rec.damage + " damage!");
				defender.setHp(defender.getHp() - rec.damage);
				attacker.setHp(attacker.getHp() + rec.drain);
				dhp.setHp(dhp.getHp() - rec.damage);
				ahp.setHp(ahp.getHp() + rec.drain, false);
				attacker.use(attacker.getWeapon());
				// battle stats
				if(!defender.getPartyColor().equals(attacker.getPartyColor())) {
					if(rec.damage > 0) {
						defender.getAssisters().add(attacker);
						attacker.addBattleStat("Damage", rec.damage);
						attacker.addBattleStat("Healing", rec.drain);
					}
				} else {
					attacker.addBattleStat("Healing", -rec.damage);
				}
				if(rec.damage > 0) {
					startShaking(hitEffects.get(0).getShakeLength() * 0.05f, hitEffects.get(0).getShakeIntensity());
				}
				
				setCurrentEvent(HURTING);
			}

			ArrayList<String> messages = analyzeAnimation(rec.animation, "(d)", true);
			for (int i = 0; i < messages.size(); i++) {
				addEntity(new SkillIndicator(messages.get(i), attacker != left, i));
			}
		} else if (currentEvent == HURTING) {
			// Try to go to HURTED
			setCurrentEvent(HURTED);
		} else if (currentEvent == HURTED) {
			darknessT= 0;
			if (dhp.getHp() == 0) {
				d.state = FightUnit.FLASHING;
				// battle stats
				attacker.addBattleStat("Kills", 1);
				defender.getAssisters().remove(attacker);
				for(Unit u : defender.getAssisters()) {
					u.addBattleStat("Assists", 1);
				}
				AudioPlayer.playAudio("die");
				currentEvent = DYING;//////////TODO: check here for bug, DYING->DONE illegal transition
			} else {
				currentEvent = RETURNING;
			}
		} else if (currentEvent == DYING) {
			// Let animation for dying guy play
		} else if (currentEvent == RETURNING) {
			a.sprite.setSpeed(((AttackAnimation)a.sprite.getCurrentAnimation()).getDefaultSpeed());
			// Let animation play
		} else if (currentEvent == DONE) {
			
			attackQ.remove(0);
			if(attackQ.size() == 0){
				a.sprite.setAnimation("ATTACK");
				a.sprite.setFrame(0);
				a.sprite.setSpeed(0);
			} else {
				currentEvent = START;
			}
		}
	}

	/**
	 * Analyze animation.
	 *
	 * @param animName the anim name
	 * @param suffix the suffix
	 * @param stripNums the strip nums
	 * @return the array list
	 */
	public static ArrayList<String> analyzeAnimation(String animName,
			String suffix, boolean stripNums) {
		ArrayList<String> ans = new ArrayList<String>();
		String[] animations = animName.split(" ");
		for (String anim : animations) {
			if (anim.endsWith(suffix)) {
				anim = anim.substring(0, anim.length() - suffix.length());
			} else {
				continue;
			}
			if (anim.matches(".*\\d") && stripNums) {
				if (anim.endsWith("1")) {
					anim = anim.substring(0, anim.length() - 1);
				} else {
					continue;
				}
			}
			ans.add(anim.toUpperCase());
		}
		return ans;
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	public void render() {
		Renderer.pushMatrix();
		Renderer.scale(2, 2);
		Renderer.render(bg, 0, 0, 1, 1, 0, 0, 240, 160, 1);
		Renderer.drawRectangle(0, 0, 240, 160, 1, new Color(0,0,0,darkness));
		if (shakeTimer > 0) {
			shakeTimer -= Game.getDeltaSeconds();
			if (prevShakeTimer - shakeTimer > SHAKE_INTERVAL) {
				float factor = Math.min(shakeTimer * 1.2f, 1.0f);
				shakeX *= -factor;
				shakeY *= -factor;
				prevShakeTimer = shakeTimer;
			}
			if (shakeTimer < 0) {
				shakeTimer = 0;
				prevShakeTimer = 0;
				shakeX = 0;
				shakeY = 0;
			}
		}
		
		// Shake
		Renderer.translate((int) shakeX, (int) shakeY);

		SortByRender comparator = new SortByRender();
		PriorityQueue<Entity> renderQueue = new PriorityQueue<Entity>(entities.size()+1, comparator);
		renderQueue.addAll(entities);
		while(!renderQueue.isEmpty()) {
			Entity e = renderQueue.poll();
			Renderer.pushMatrix();
			if(e.renderDepth > HUD_DEPTH && !(e instanceof BackgroundEffect)) {
				Renderer.translate(cameraOffset, 0);
			}
			e.render();
			Renderer.popMatrix();
		}

		// Undo shake translation
		Renderer.popMatrix();
		Renderer.removeClip();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Stage#endStep()
	 */
	@Override
	public void endStep() {
		for (Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}

	/**
	 * Start shaking.
	 *
	 * @param time the time
	 * @param intensity the intensity
	 */
	private void startShaking(float time, int intensity) {
		shakeTimer = time;
		prevShakeTimer = time;
		float dist = intensity;
		shakeX = -dist;
		shakeY = -dist;
	}

	// Getters Setters

	/**
	 * Sets the current event.
	 *
	 * @param event the new current event
	 */
	public void setCurrentEvent(int event) {
		if(event == ATTACKED){

		}
		if (currentEvent == ATTACKING && event == HURTED)
			return;
		if (event == HURTED) {
			if(!hasHitEffects() && leftHP.doneAnimating && rightHP.doneAnimating) {
//				System.out.println(currentEvent + " TO " + event);
				currentEvent = event;
			}
		} else if (event == HURTING || event == currentEvent + 1) {
//			System.out.println(currentEvent + " to " + event);
			currentEvent = event;
		} else {
			//TODO: check here for bug? illegal transit - (7:DYING - 9:DONE)
			throw new IllegalArgumentException("Invalid state transit: "
					+ currentEvent + " to " + event);
		}

	}
	
	/**
	 * Type test.
	 *
	 * @param a the a
	 * @return true, if successful
	 */
	private static boolean typeTest(AttackAnimation a){
		return true;
	}
	
	/**
	 * Type test.
	 *
	 * @param a the a
	 * @return true, if successful
	 */
	private static boolean typeTest(DodgeAnimation a){
		return true;
	}
	
	/**
	 * Sets the darkness.
	 *
	 * @param dark the new darkness
	 */
	public void setDarkness(float dark){
		darknessT = dark;
	}

	/**
	 * Distance to head.
	 *
	 * @return the int
	 */
	public int distanceToHead() {
		return rangeToHeadDistance(range);
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Gets the unit.
	 *
	 * @param i the i
	 * @return the unit
	 */
	public Unit getUnit(int i) {
		return i == 0 ? left : right;
	}

	/**
	 * Checks if is left.
	 *
	 * @param u the u
	 * @return true, if is left
	 */
	public boolean isLeft(Unit u) {
		return u == left;
	}

	/**
	 * Range to head distance.
	 *
	 * @param range the range
	 * @return the int
	 */
	public static int rangeToHeadDistance(int range) {
		if (range == 1) {
			return 32;
		} else {
			return 54;
		}
	}

	/**
	 * Gets the current event.
	 *
	 * @return the current event
	 */
	public int getCurrentEvent() {
		return currentEvent;
	}
	
	/**
	 * Move camera.
	 *
	 * @param left the left
	 */
	public void moveCamera(boolean left){
		cameraOffsetT = left? cameraOffsetF: -cameraOffsetF;
	}
	
	/**
	 * Checks for hit effects.
	 *
	 * @return true, if successful
	 */
	public boolean hasHitEffects() {
		for(Entity e : entities) {
			if (e instanceof HitEffect || e instanceof MissEffect || e instanceof NoDamageEffect) {
				// We're not ready yet. Wait for the hiteffects to go away.
//				System.out.println("still have hiteffects "+e.getClass().getCanonicalName());
				return true;
			}
		}
		return false;
	}

}
