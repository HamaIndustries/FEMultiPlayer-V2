package net.fe.lobbystage;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.fe.FEMultiplayer;
import net.fe.FEResources;
import net.fe.Party;
import net.fe.Player;
import net.fe.Session;
import net.fe.ConnectStage;
import net.fe.editor.Level;
import net.fe.modifier.Modifier;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.ReadyMessage;
import net.fe.network.message.StartPicking;
import net.fe.overworldStage.ClientOverworldStage.FogType;
import net.fe.overworldStage.ClientOverworldStage.SpectatorFogOption;
import net.fe.overworldStage.Grid;
import net.fe.overworldStage.Tile;
import net.fe.overworldStage.Terrain;
import net.fe.unit.Class;
import net.fe.unit.Statistics;
import net.fe.unit.Unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Renderer;
import chu.engine.anim.ShaderArgs;
import chu.engine.anim.Transform;
import chu.engine.menu.MenuButton;

// TODO: Auto-generated Javadoc
/**
 * Lobby where the players wait before the game.
 * @author Shawn
 *
 */
public class ClientLobbyStage extends LobbyStage {
	
	/** Colors used in rendering the stage */
	public static final Color BORDER_DARK = new Color(0x483828);
	/** Colors used in rendering the stage */
	public static final Color BORDER_LIGHT = new Color(0xf8f0c8);
	/** Colors used in rendering the stage */
	public static final Color NEUTRAL = new Color(0xb0a878);
	/** Colors used in rendering the stage */
	public static final Color NEUTRAL_DARK = new Color(0x58543c);
	
	/** a unit used to deduce the session's state regarding fog vision */
	// the non-zero HP is so that the unit does not attempt to remove itself from the not-currently-existing overworld stage upon construction
	private final Unit GENERIC_THIEF = new Unit("---", Class.createClass("Assassin"), '-', new Statistics().copy("HP", 1), new Statistics());
	/** a unit used to deduce the session's state regarding fog vision */
	private final Unit GENERIC_NONTHIEF = new Unit("---", Class.createClass("Paladin"), '-', new Statistics().copy("HP", 1), new Statistics());
	
	/**
	 * Instantiates a new client lobby stage.
	 *
	 * @param session the session
	 */
	public ClientLobbyStage(Session session) {
		super(session);
		{
			final LobbyChatBox chatInput = new LobbyChatBox();
			addEntity(chatInput);
			
			addEntity(new TextureMenuButton(259, 294, 47, 20, FEResources.getTexture("send_button"), () -> chatInput.send()));
		}
		
		addEntity(new TextureMenuButton(409, 282, 64, 32, FEResources.getTexture("exit_button"), () -> {
				AudioPlayer.playAudio("select");
				FEMultiplayer.getClient().quit();
				FEMultiplayer.setCurrentStage(FEMultiplayer.connect);
		}));
		
		addEntity(new TextureMenuButton(329, 282, 64, 32, FEResources.getTexture("ready_button"), () -> {
				AudioPlayer.playAudio("select");
				FEMultiplayer.getClient().sendMessage(new ReadyMessage());
		}));
		
		addEntity(new TextureMenuButton(369, 242, 64, 32, FEResources.getTexture("spectate_button"), () -> {
				AudioPlayer.playAudio("select");
				FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_SPECTATOR);
		}));
		
		addEntity(new TextMenuButton(329, 204, 64, 30, "Blue", Party.TEAM_BLUE, () -> {
			AudioPlayer.playAudio("select");
			FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_BLUE);
		}));
		
		addEntity(new TextMenuButton(409, 204, 64, 30, "Red", Party.TEAM_RED, () -> {
			AudioPlayer.playAudio("select");
			FEMultiplayer.getLocalPlayer().joinTeam(Player.TEAM_RED);
		}));
	}

	/* (non-Javadoc)
	 * @see net.fe.lobbystage.LobbyStage#beginStep()
	 */
	@Override
	public void beginStep(List<Message> messages) {
		super.beginStep(messages);
		for(Entity e : entities) {
			e.beginStep();
		}
		for(Message message : messages) {
			if(message instanceof StartPicking) {
				// Set up global list of players
				for(Player p : FEMultiplayer.getPlayers().values()) {
					if(p.equals(FEMultiplayer.getLocalPlayer()))
						FEMultiplayer.setLocalPlayer(p);
					if(p.isSpectator())
						p.getParty().clear();
				}
				session.getPickMode().setUpClient(session);
			}
		}
		processAddStack();
		processRemoveStack();
	}

	/* (non-Javadoc)
	 * @see net.fe.lobbystage.LobbyStage#onStep()
	 */
	@Override
	public void onStep() {
		super.onStep();
		for(Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	/* (non-Javadoc)
	 * @see net.fe.lobbystage.LobbyStage#endStep()
	 */
	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Stage#render()
	 */
	@Override
	public void render() {
		super.render();
		// Draw a border around the entire visible area
		Renderer.drawRectangle(0, 0, 480, 320, 1.0f, NEUTRAL);
		Renderer.drawRectangle(1, 1, 479, 319, 1.0f, BORDER_DARK);
		Renderer.drawRectangle(2, 2, 478, 318, 1.0f, BORDER_LIGHT);
		Renderer.drawRectangle(3, 3, 477, 317, 1.0f, NEUTRAL);
		
		final chu.engine.anim.BitmapFont defaultFont = FEResources.getBitmapFont("default_med");
		final int marginX = 6;
		// Draw session information
		{
			final int x = marginX;
			final int y = 22;
			Renderer.drawString("default_med", "Game info", x, y - 14, 0.9f);
			Renderer.drawRectangle(x, y, x + 160 - marginX * 2, y + 164, 1.0f, NEUTRAL_DARK);
			
			int yy = y + 2;
			Renderer.drawString("default_med", "Objective: " + session.getObjective().getDescription(), x+2, yy, 0.9f);
			yy += 14;
			Renderer.drawString("default_med", "Pick mode: " + session.getPickMode(), x+2, yy, 0.9f);
			yy += 14;
			Renderer.drawString("default_med", "Max Units: " + session.getMaxUnits(), x+2, yy, 0.9f);
			yy += 14;
			Renderer.drawString("default_med", "Fog: " + session.getFogOption(), x+2, yy, 0.9f);
			yy += 14;
			if (session.getFogOption() != FogType.NONE) {
				final int thiefSight = session.getSight(GENERIC_THIEF);
				final int pallySight = session.getSight(GENERIC_NONTHIEF);
				
				Renderer.drawString("default_med", "Sight: " + pallySight + " | " + thiefSight, x+20, yy, 0.9f);
				yy += 14;
			}
			Renderer.drawString("default_med", "Hit RNG: " + session.getHitRNG(), x+2, yy, 0.9f);
			yy += 14;
			Renderer.drawString("default_med", "Crit RNG: " + session.getCritRNG(), x+2, yy, 0.9f);
			yy += 14;
			Renderer.drawString("default_med", "Skill RNG: " + session.getSkillRNG(), x+2, yy, 0.9f);
			yy += 14;
			
			if (! session.getModifiers().isEmpty()) {
				Renderer.drawString("default_med", "Modifiers: ", x+2, yy, 0.9f);
				yy += 14;
				for(Modifier m : session.getModifiers()) {
					Renderer.drawString("default_med", "* "+m.toString(), x+20, yy, 0.9f);
					yy += 14;
				}
			}
		}
		
		// Draw map
		{
			final int x = 160 + marginX;
			final int y = 22;
			Renderer.drawString("default_med", "Map", x, y - 14, 0.9f);
			Renderer.drawRectangle(x, y, x + 160 - marginX * 2, y + 164, 1.0f, NEUTRAL_DARK);
			
			Renderer.drawString("default_med", session.getMap(), x + 80 - defaultFont.getStringWidth(session.getMap()) / 2, y + 2, 0.9f);
			
			// I'd like to cache this - since there's file IO here - but the session doesn't have a real level when given to the constructor
			try (
				final java.io.InputStream in = this.getClass().getClassLoader().getResourceAsStream("levels/" + session.getMap() + ".lvl");
				final java.io.ObjectInputStream ois = new java.io.ObjectInputStream(in)
			) {
				final Level level = (Level) ois.readObject();
				
				final float regionWidth = 160 - marginX * 2 - 4;
				final float regionHeight = 164 - 14;
				final float maxTileWidth = regionWidth / level.width;
				final float maxTileHeight = regionHeight / level.height;
				final float tileDim = (float) Math.floor(Math.min(maxTileWidth, maxTileHeight));
				
				for (int i = 0; i < level.width; i++)
				for (int j = 0; j < level.height; j++) {
					final Color c = terrainToColor(Tile.getTerrainFromID(level.tiles[j][i]));
					Renderer.drawRectangle(x + 2 + tileDim * i, y + 20 + tileDim * j, x + 2 + tileDim + tileDim * i, y + 20 + tileDim + tileDim * j, 0.5f, c);
				}
			} catch (java.io.IOException ex) {
				// This problem should be detected by ClientInit
			} catch (ClassNotFoundException ex) {
				// This problem should be detected by ClientInit
			}
		}
		
		// Draw players
		{
			final int x = 160 * 2 + marginX;
			final int y = 22;
			Renderer.drawString("default_med", "Players", x, y - 14, 0.9f);
			Renderer.drawRectangle(x, y, x + 160 - marginX * 2, y + 164, 1.0f, NEUTRAL_DARK);
			
			int yy = y + 2;
			Renderer.drawString("default_med", "Blue Team", x + 80 - defaultFont.getStringWidth("Blue Team") / 2, yy, 0.9f);
			yy += 14;
			yy += drawPlayers(x, yy, session.getPlayers(), (p) -> p.getTeam() == Player.TEAM_BLUE);
			yy += 8;
			
			Renderer.drawString("default_med", "Red Team", x + 80 - defaultFont.getStringWidth("Red Team") / 2, yy, 0.9f);
			yy += 14;
			yy += drawPlayers(x, yy, session.getPlayers(), (p) -> p.getTeam() == Player.TEAM_RED);
			yy += 8;
			
			Renderer.drawString("default_med", "Spectators", x + 80 - defaultFont.getStringWidth("Spectators") / 2, yy, 0.9f);
			yy += 14;
			yy += drawPlayers(x, yy, session.getPlayers(), (p) -> p.getTeam() == Player.TEAM_SPECTATOR);
		}
		
		//Draw chat
		{
			final int x = 6;
			final int y = 202;
			Renderer.drawString("default_med", "Chat", x, y-14, 0.9f);
			Renderer.drawRectangle(x, y, x+300, y+89, 1.0f, NEUTRAL_DARK);
			List<String> chats = session.getChatlog().getLast(5);
			for(int i=0; i<5; i++) {
				Renderer.drawString("default_med", chats.get(i), x+2, y+2+i*16, 0.8f);
			}
		}
	}
	
	/**
	 * @return the height of the drawn area
	 */
	private static int drawPlayers(final int x, final int y, final Player[] players, final Predicate<Player> filter) {
		int yy = 0;
		for (Player p : players) {
			if (filter.test(p)) {
				Transform t = new Transform();
				if(p.ready) {
					t.setColor(new Color(90,200,90));
				}
				
				Renderer.drawString("default_med", p.getName(), x + 2, y + yy, 0.8f, t);
				yy += 14;
			}
		}
		return yy;
	}
	
	/** Returns a color that can be used to represent a Terrain; for example in a simplified minimap */
	private static Color terrainToColor(Terrain t) {
		// the intention is the brightness of the return value gets darker as
		// the terrain gets harder to traverse with hue disambiguating between
		// terrains with similar attributes
		switch (t) {
			case PLAIN: return new Color(64,255,64);
			case PATH: return new Color(192,160,64);
			case FOREST: return new Color(32,128,32);
			case FLOOR: return new Color(192,192,192);
			case PILLAR: return new Color(96,96,96);
			case MOUNTAIN: return new Color(96,64,16);
			case VILLAGE: return new Color(192,160,64);
			case PEAK: return new Color(16,8,0);
			case FORT: return new Color(32,32,96);
			case SEA: return new Color(0,0,32);
			case DESERT: return new Color(160,128,32);
			case WALL: return new Color(8,8,8);
			case FENCE: return new Color(8,8,8);
			case NONE: return new Color(0,0,0);
			case CLIFF: return new Color(32,16,0);
			case THRONE: return new Color(32,32,96);
			case HILL: return new Color(96,64,16);
			case HOUSE: return new Color(192,160,64);
			case UNKNOWN: return new Color(0,0,0);
			default: return new Color(0,0,0);
		}
	}
	
	private static final class TextureMenuButton extends MenuButton {
		private final Runnable onClick;
		public TextureMenuButton(float x, float y, float w, float h, Texture texture, Runnable onClick) {
			super(x, y, w, h);
			sprite.addAnimation("default", texture);
			this.onClick = onClick;
		}
		@Override
		public void onClick() {
			onClick.run();
		}
		@Override
		public void render() {
			if(hover) {
				sprite.render(x, y, renderDepth, null, new ShaderArgs("lighten", 0.5f));
			} else {
				sprite.render(x, y, renderDepth);
			}
		}
	}
	
	private static final class TextMenuButton extends MenuButton {
		private final String text;
		private final Color fillColor;
		private final Runnable onClick;
		public TextMenuButton(float x, float y, float w, float h, String text, Color fillColor, Runnable onClick) {
			super(x, y, w, h);
			/// TextMenuButton <: MenuButton <: Entity
			/// `MenuButton.width` is private, therefore `super.width` is a violation of a MenuButton's private access
			/// This is despite Entity having a `width` that is public.
			((Entity) this).width = w;
			((Entity) this).height = h;
			this.text = text;
			this.fillColor = fillColor;
			this.onClick = onClick;
		}
		@Override
		public void onClick() {
			onClick.run();
		}
		@Override
		public void render() {
			if(hover){
				Renderer.drawBorderedRectangle(x, y, x + ((Entity) this).width, y + ((Entity) this).height, renderDepth, fillColor.brighter(), BORDER_LIGHT, BORDER_DARK);
			} else {
				Renderer.drawBorderedRectangle(x, y, x + ((Entity) this).width, y + ((Entity) this).height, renderDepth, fillColor, BORDER_LIGHT, BORDER_DARK);
			}
			final float stringWidth = FEResources.getBitmapFont("default_med").getStringWidth(text);
			Renderer.drawString("default_med", text, x + (((Entity) this).width - stringWidth) / 2, y+8,  renderDepth);
		}
	}
}
