package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.BORDER_DARK;
import static net.fe.fightStage.FightStage.BORDER_LIGHT;
import java.util.List;
import java.util.ArrayList;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;
import net.fe.FEResources;
import net.fe.fightStage.AttackRecord;
import net.fe.unit.Unit;
import net.fe.FEResources;
import net.fe.unit.BattleStats;
import net.fe.unit.Unit;
import net.fe.unit.UnitIdentifier;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.Renderer;

/**
 * An entity that shows a fight animation without the full
 * extravagance of the FightStage
 */
public final class AbridgedFightScene extends Entity {
	
	private static final Color HP_OUTLINE = new Color(88, 0, 0); 
	private static final Color HP_EMPTY = new Color(0, 160, 40); 
	private static final Color HP_FILLED1 = new Color(184, 248, 224); 
	private static final Color HP_FILLED2 = new Color(136, 248, 144);
	
	private static final float DONE_DELAY = 1f;
	private static final float ATTACK_HALF_TIME = 0.15f;
	private static final float ATTACK_MAX_DISTANCE = 4f;
	
	/** The first unit participating in this fight */
	private final Unit u1;
	/** The second unit participating in this fight */
	private final Unit u2;
	/**
	 * The sequence of AttackRecords to render. Items are removed from this list
	 * as they are rendered, such that the current attack record is always {@code attackQ.get(0);}
	 */
	private final ArrayList<AttackRecord> attackQ;
	/** A Runnable to be called after the Fight Stage plays out */
	private final Runnable returnCallback;
	
	private State state;
	
	/** The currently displayed HP for u1 */
	private float u1hpDisplay;
	/** The currently displayed HP for u2 */
	private float u2hpDisplay;
	/** The actual current HP for u1 */
	private int u1hpTarget;
	/** The actual current HP for u2 */
	private int u2hpTarget;
	
	/** A marker for the passage for time, for states that would like such a thing */
	private float timer;
	
	/** A listing of the parts of a battle animation */
	private enum State {
		/** The inital state before */
		START,
		/** The attacking unit does a charge towards the defender */
		ATTACK,
		/** The defender shakes or something probably. Also hp bars adjust */
		HURTING,
		/** The attack queue has run out, but the animation hasn't fully disappeared yet */
		DONE;
	}
	
	
	public AbridgedFightScene(
		UnitIdentifier u1, UnitIdentifier u2,
		List<AttackRecord> attackQ,
		ClientOverworldStage stage,
		Runnable returnCallback
	) {
		// Assuming view is 328 by 240
		super(64, 32);
		this.width = 328 - 64;
		this.height = 48;
		this.renderDepth = ClientOverworldStage.MENU_DEPTH;
		this.stage = stage;
		
		this.u1 = stage.getUnit(u1);
		this.u2 = stage.getUnit(u2);
		this.attackQ = new ArrayList<>(attackQ);
		this.returnCallback = returnCallback;
		
		if (this.u1.y - stage.camY < 120) {
			this.y = 240 - 48 - 32;
		}
		
		if (Math.abs(this.u1.x - this.u2.x) >= Math.abs(this.u1.y - this.u2.y)) {
			if (this.u1.x > this.u2.x) {
				this.u1.sprite.setAnimation("LEFT");
				this.u2.sprite.setAnimation("RIGHT");
			} else {
				this.u1.sprite.setAnimation("RIGHT");
				this.u2.sprite.setAnimation("LEFT");
			}
		} else {
			if (this.u1.y > this.u2.y) {
				this.u1.sprite.setAnimation("UP");
				this.u2.sprite.setAnimation("DOWN");
			} else {
				this.u1.sprite.setAnimation("DOWN");
				this.u2.sprite.setAnimation("UP");
			}
		}
		
		this.state = State.START;
		this.u1hpTarget = this.u1.getHp();
		this.u2hpTarget = this.u2.getHp();
		this.u1hpDisplay = u1hpTarget;
		this.u2hpDisplay = u2hpTarget;
	}
	
	public void beginStep() {
		switch (this.state) {
			case START: {
				this.timer = 0;
				if (attackQ.size() == 0) {
					this.state = State.DONE;
				} else {
					this.state = State.ATTACK;
				}
			}; break;
			case ATTACK: {
 				this.timer += Game.getDeltaSeconds();
				
				if (timer > ATTACK_HALF_TIME * 2) {
					final Unit attacker = ((ClientOverworldStage) stage).getUnit(attackQ.get(0).attacker);
					final Unit defender = ((ClientOverworldStage) stage).getUnit(attackQ.get(0).defender);
					attacker.setHp(attacker.getHp() + attackQ.get(0).drain);
					defender.setHp(defender.getHp() - attackQ.get(0).damage);
					if (!attackQ.get(0).animation.contains("Miss") || attacker.getWeapon().isMagic()) {
						attacker.use(attacker.getWeapon());
					}
					if(attackQ.get(0).damage > 0) {
						defender.getAssisters().add(attacker);
						attacker.addBattleStats(new BattleStats(
							/* kills = */ 0,
							/* assists = */ 0,
							/* damage = */ attackQ.get(0).damage,
							/* healing = */ attackQ.get(0).drain
						));
					}
					if(attackQ.get(0).damage < 0) {
						defender.getAssisters().add(attacker);
						attacker.addBattleStats(new BattleStats(
							/* kills = */ 0,
							/* assists = */ 0,
							/* damage = */ 0,
							/* healing = */ -attackQ.get(0).damage
						));
					}
					if (defender.getHp() == 0) {
						attacker.addBattleStats(new BattleStats(/* kills = */ 1, 0, 0, 0));
						defender.getAssisters().remove(attacker);
						for(Unit u : defender.getAssisters()) {
							u.addBattleStats(new BattleStats(0, /* assists = */ 1, 0, 0));
						}
					}
					
					u1hpTarget = u1.getHp();
					u2hpTarget = u2.getHp();
					
					this.state = State.HURTING;
				}
			}; break;
			case HURTING: {
				if (Math.abs(u1hpDisplay - u1hpTarget) < 1 && Math.abs(u2hpDisplay - u2hpTarget) < 1) {
					this.attackQ.remove(0);
					this.state = State.START;
					
				} else {
					if (Math.abs(u1hpDisplay - u1hpTarget) >= 1) {
						u1hpDisplay += Math.signum(u1hpTarget - u1hpDisplay) * 
								Math.min(Math.abs(u1hpDisplay - u1hpTarget), 30 * Game.getDeltaSeconds());
					}
					if (Math.abs(u2hpDisplay - u2hpTarget) >= 1) {
						u2hpDisplay += Math.signum(u2hpTarget - u2hpDisplay) * 
								Math.min(Math.abs(u2hpDisplay - u2hpTarget), 30 * Game.getDeltaSeconds());
					}
					
				}
			}; break;
 			case DONE: {
 				this.timer += Game.getDeltaSeconds();
 				
 				if (this.timer > DONE_DELAY) {
 					this.onComplete();
 				}
			}; break;
		}
	}
	
	public void onStep() {
		switch (this.state) {
			case START: {
			}; break;
			case ATTACK: {
				final Unit attacker = ((ClientOverworldStage) stage).getUnit(attackQ.get(0).attacker);
				final Unit defender = ((ClientOverworldStage) stage).getUnit(attackQ.get(0).defender);
				
				final float angle = (float) Math.atan2(defender.y - attacker.y, defender.x - attacker.x);
				final float distance = ATTACK_MAX_DISTANCE * (
					timer < ATTACK_HALF_TIME ?
					timer / ATTACK_HALF_TIME :
					(2 * ATTACK_HALF_TIME - timer) / ATTACK_HALF_TIME
				);
				final float dx = (float) Math.cos(angle) * distance;
				final float dy = (float) Math.sin(angle) * distance;
				
				attacker.x = attacker.getOrigX() * 16 + dx;
				attacker.y = attacker.getOrigY() * 16 + dy;
			}; break;
			case HURTING: {
			}; break;
			case DONE: {
			}; break;
		}
	}
	
	public void render() {
		Renderer.drawRectangle(this.x, this.y, this.x + this.width, this.y + this.height, this.renderDepth, BORDER_DARK);
		Renderer.drawRectangle(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, this.renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(this.x + 2, this.y + 2, this.x + this.width / 2 - 1, this.y + this.height - 2, this.renderDepth, u1.getPartyColor());
		Renderer.drawRectangle(this.x + this.width / 2 + 1, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, this.renderDepth, u2.getPartyColor());
		
		{
			final int nameWidth = FEResources.getBitmapFont("default_med").getStringWidth(u1.name);
			Renderer.drawString("default_med", u1.name, this.x + this.width / 4 - nameWidth / 2, this.y + 5, this.renderDepth);
			this.renderHealth((int) u1hpDisplay, u1.getStats().maxHp, (int) this.x + 26, (int) this.y + 6 + 16);
		}
		{
			final int nameWidth = FEResources.getBitmapFont("default_med").getStringWidth(u2.name);
			Renderer.drawString("default_med", u2.name, this.x + this.width * 3 / 4 - nameWidth / 2, this.y + 5, this.renderDepth);
			this.renderHealth((int) u2hpDisplay, u2.getStats().maxHp, (int) this.x + (int) this.width / 2 + 26, (int) this.y + 6 + 16);
		}
	}
	
	private void renderHealth(int current, int total, int x, int y) {
		int offY = 0;
		int offX = 0;
		int width = FEResources.getBitmapFont("stat_numbers").getStringWidth((int)current + "");
		if(total <= 40) {
			Renderer.drawString("stat_numbers", (int)current + "", x-5-width, y-2, renderDepth);
		} else {
			Renderer.drawString("stat_numbers", (int)current + "", x-5-width, y+2, renderDepth);
		}
		for (int hp = 1; hp <= total; hp++) {
			Renderer.drawRectangle(x + offX, y + offY, x + offX + 2, y + offY + 6, renderDepth, HP_OUTLINE);
			if (hp <= current) {
				Renderer.drawRectangle(x + offX + 1, y + offY + 1, x + offX + 2, y + offY + 3, renderDepth, HP_FILLED1);
				Renderer.drawRectangle(x + offX + 1, y + offY + 3, x + offX + 2, y + offY + 5, renderDepth, HP_FILLED2);
			} else {
				Renderer.drawRectangle(x + offX + 1, y + offY + 1, x + offX + 2, y + offY + 5, renderDepth, HP_EMPTY);
			}
			
			if(hp == 40){
				offY = 8;
				offX = 0;
			} else {
				offX +=2;
			}
		}
	}
	
	private void onComplete() {
		this.destroy();
		this.u1.sprite.setAnimation("IDLE");
		this.u2.sprite.setAnimation("IDLE");
		this.returnCallback.run();
	}
}
