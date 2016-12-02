package net.fe;

import java.util.Map.Entry;
import java.util.*;

import org.newdawn.slick.Color;

import chu.engine.Game;
import chu.engine.anim.Renderer;
import chu.engine.entity.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class ControlsDisplay.
 */
public class ControlsDisplay extends Entity{
	
	/** The keys. */
	private Map<String, String> keys;
	
	/** The temp message. */
	private String tempMessage;
	
	/** The temp timer. */
	private float tempTimer;
	
	/** The temp timer max. */
	private float tempTimerMax;
	
	/**
	 * Instantiates a new controls display.
	 */
	public ControlsDisplay() {
		super(0, 300);
		renderDepth = 0;
		keys = new LinkedHashMap<String, String>();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#onStep()
	 */
	public void onStep(){
		if(tempMessage != null){
			tempTimer += Game.getDeltaSeconds();
			if(tempTimer > tempTimerMax){
				tempMessage = null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Entity#render()
	 */
	public void render(){
		Renderer.drawRectangle(x, y, x+480, y+20, renderDepth, new Color(0,0,0,0.5f));
		StringBuilder str = new StringBuilder();
		String sep = "   |   ";
		for(Entry<String, String> entry: keys.entrySet()){
			str.append(sep);
			str.append(FEResources.getKeyMappedName(entry.getKey()) + ": " + entry.getValue());
		}
		String s = str.substring(sep.length());
		if(tempMessage != null){
			s = tempMessage;
		}
		int width = FEResources.getBitmapFont("default_med").getStringWidth(s);
		Renderer.drawString("default_med", s, 240 - width/2, y+5, renderDepth);
	}
	
	/**
	 * Adds the control.
	 *
	 * @param key the key
	 * @param action the action
	 */
	public void addControl(String key, String action){
		keys.put(key, action);
	}
	
	/**
	 * Sets the.
	 *
	 * @param key the key
	 * @param action the action
	 */
	public void set(String key, String action){
		keys.put(key, action);
	}
	
	/**
	 * Removes the control.
	 *
	 * @param key the key
	 */
	public void removeControl(String key){
		keys.remove(key);
	}
	
	/**
	 * Sets the temp message.
	 *
	 * @param msg the msg
	 * @param seconds the seconds
	 */
	public void setTempMessage(String msg, float seconds){
		tempMessage = msg;
		tempTimerMax = seconds;
		tempTimer = 0;
	}
	
}
