package net.fe.overworldStage;

import static net.fe.fightStage.FightStage.*;

import org.newdawn.slick.opengl.Texture;

import net.fe.FEResources;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.BitmapFont;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class TerrainInfo.
 */
public class TerrainInfo extends Entity implements DoNotDestroy{
	
	/** The dragons. */
	private static Texture dragons = FEResources.getTexture("dragon_separator");

	/**
	 * Instantiates a new terrain info.
	 *
	 * @param c the c
	 */
	public TerrainInfo(Cursor c) {
		super(0, Game.getWindowHeight()/net.fe.FEResources.getWindowScale()-80);
		renderDepth = 0.8f;
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render() {
		Terrain terrain = ((ClientOverworldStage)stage).getHoveredTerrain();
		
		//Main box
		Renderer.drawRectangle(x, y, x+50, y+80, renderDepth, BORDER_DARK);
		Renderer.drawRectangle(x+1, y+1, x+49, y+79, renderDepth, BORDER_LIGHT);
		Renderer.drawRectangle(x+2, y+2, x+48, y+78, renderDepth, NEUTRAL);
		
		//Terrain name ribbon
		Renderer.drawRectangle(x+3, y+3, x+47, y+26, renderDepth, NEUTRAL.darker(0.5f));
		BitmapFont def = FEResources.getBitmapFont("default_med");
		int width = def.getStringWidth(terrain.toString());
		Renderer.drawString("default_med", terrain.toString(), x+25-width/2, y+8, renderDepth);
		
		//Separator
		Renderer.render(dragons, 0, 0, 1, 1, x+7, y+28, x+45, y+37, renderDepth);
		
		//Terrain stats
		String defenseBonusString = getDefenseBonus(terrain);
		String avoBonusString = getAvoBonus(terrain);
		String hpbonusString = getHpBonus(terrain);
		
		Renderer.drawString("default_med", "AVO", x+5, y+40, renderDepth);
		Renderer.drawString("default_med", avoBonusString, x+46-def.getStringWidth(avoBonusString), y+40, renderDepth);
		
		Renderer.drawString("default_med", "DEF", x+5, y+53, renderDepth);
		Renderer.drawString("default_med", defenseBonusString, x+46-def.getStringWidth(defenseBonusString), y+53, renderDepth);
		
		Renderer.drawString("default_med", "HP", x+5, y+66, renderDepth);
		Renderer.drawString("default_med", hpbonusString, x+46-def.getStringWidth(hpbonusString), y+66, renderDepth);
	}
	

	private String getDefenseBonus(Terrain terrain) {
		return ((terrain.getDefenseBonus(null).def ==  terrain.getDefenseBonus(null).res ?
				terrain.getDefenseBonus(null).def + "" :
				terrain.getDefenseBonus(null).def + " | " + terrain.getDefenseBonus(null).res)).replaceAll("-1", "??");
	}
	
	private String getAvoBonus(Terrain terrain) {
		int avo = terrain.getAvoidBonus(null);
		return avo == -1 ? "??" : avo + "";
	}
	
	private String getHpBonus(Terrain terrain) {
		int hp = terrain.healthBonus;
		return hp == -1 ? "??" : hp + "";
	}

}
