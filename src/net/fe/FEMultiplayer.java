package net.fe;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.Sys;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.menu.Notification;
import net.fe.builderStage.TeamDraftStage;
import net.fe.fightStage.CombatCalculator;
import net.fe.fightStage.FightStage;
import net.fe.lobbystage.ClientLobbyStage;
import net.fe.network.Client;
import net.fe.network.FEServer;
import net.fe.network.Message;
import net.fe.network.command.Command;
import net.fe.network.message.CommandMessage;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.objective.Seize;
import net.fe.rng.SimpleRNG;
import net.fe.rng.TrueHitRNG;
import net.fe.unit.Unit;
import net.fe.unit.UnitFactory;
import net.fe.unit.UnitIdentifier;
import net.fe.unit.WeaponFactory;

/**
 * Main class for the Clientside program.
 *
 * @author Shawn
 */
public class FEMultiplayer extends Game{
	
	/** The current stage. */
	private static Stage currentStage;
	
	/** The client. */
	private static Client client;
	
	/** The local player. */
	private static Player localPlayer;
	
	/** The lobby. */
	public static ClientLobbyStage lobby;
	
	/** The connecting stage. */
	public static ConnectStage connect;
	
	/** The test session, for testing fightstage. */
	private static Session testSession;
	
	private static final RunnableList postRenderRunnables = new RunnableList();

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try{
			FEMultiplayer game = new FEMultiplayer();
//			SoundTrack.enabled = false;
			game.init(480, 320, "Fire Emblem Multiplayer");
			/* Testing code */
//			game.testFightStage();
//			game.testOverworldStage();
//			game.testDraftStage();
			game.loop();
		} catch (Exception e){
			System.err.println("Exception occurred, writing to logs...");
			e.printStackTrace();
			try{
				File errLog = new File("error_log_client" + LocalDateTime.now().toString().replace("T", "@").replace(":", "-") + ".log");
				PrintWriter pw = new PrintWriter(errLog);
				e.printStackTrace(pw);
				pw.close();
			}catch (IOException e2){
				e2.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see chu.engine.Game#init(int, int, java.lang.String)
	 */
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		Player p1 = new Player("Player", (byte) 0);
		localPlayer = p1;
		ByteBuffer icon16, icon32;
		icon16 = icon32 = null;
		try {
			icon16 = ByteBuffer.wrap(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/gui/icon16.png")).getTextureData());
			icon32 = ByteBuffer.wrap(TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/gui/icon32.png")).getTextureData());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Display.setIcon(new ByteBuffer[]{icon16, icon32});
		FEResources.loadResources();
		FEResources.loadBitmapFonts();
		WeaponFactory.loadWeapons();
		UnitFactory.loadUnits();
		p1.getParty().setColor(Party.TEAM_BLUE);
		
		/* OpenGL final setup */
		glEnable(GL_LINE_SMOOTH);
		
		UnitFactory.getUnit("Lyn");
		connect = new ConnectStage();
		setCurrentStage(new TitleStage());
		SoundTrack.loop("main");
		
	}
	
	/**
	 * Test draft stage.
	 */
	public void testDraftStage() {
		Player p1 = localPlayer;
		testSession = new Session(new net.fe.overworldStage.objective.Rout(), "test", 6, new java.util.HashSet<>(), new net.fe.pick.Draft(), new TrueHitRNG(), new SimpleRNG(), new SimpleRNG());
		Player p2 = new Player("p2", (byte) 1);
		Player p3 = new Player("p3", (byte) 2);
		p2.getParty().setColor(Party.TEAM_RED);
		p3.getParty().setColor(Party.TEAM_GREEN);
		p2.getParty().addUnit(UnitFactory.getUnit("Mia"));
		p2.getParty().addUnit(UnitFactory.getUnit("L'Arachel"));
		testSession.addPlayer(p1);
		testSession.addPlayer(p2);
		testSession.addPlayer(p3);
		currentStage = new TeamDraftStage(testSession);
	}
	
	/**
	 * Test fight stage.
	 * 
	 * Use debugStat on units to mess with fights, is also useful for testing animations. 
	 * Intentionally crashes at the end for lack of session info, so don't worry about it.
	 * 
	 * range can be set by messing with map.addUnit(), and you can change all the other 
	 * unit-related things to test them out. I can see us using this to use this bit for speed 
	 * testing in the future, if I ever make a gui or something for it.
	 * 
	 * TODO: gui for speed balancing
	 * 
	 */
	public void testFightStage(){
		Player p1 = localPlayer;
		testSession = new Session(new Seize(), "test", 8, new java.util.HashSet<>(), new net.fe.pick.Draft(), new TrueHitRNG(), new SimpleRNG(), new SimpleRNG());
		Player p2 = new Player("p2", (byte) 1);
		p2.getParty().setColor(Party.TEAM_RED);
		p1.getParty().setColor(Party.TEAM_BLUE);
		p2.setTeam(2);
		p1.setTeam(1);
		
		testSession.addPlayer(p1);
		testSession.addPlayer(p2);
		
		final ClientOverworldStage map = new ClientOverworldStage(testSession);
		Unit u1 = UnitFactory.getUnit("Eirika");
		u1.getInventory().add(WeaponFactory.getWeapon("Silver Sword"));
		u1.equip(0);
		map.addUnit(u1, 0, 0);
		u1.setLevel(20);
		u1.loadMapSprites();
		p1.getParty().addUnit(u1);
		
		Unit u2 = UnitFactory.getUnit("Dart");
		u2.getInventory().add(WeaponFactory.getWeapon("Tomahawk"));
		map.addUnit(u2, 1, 0);
		u2.equip(0);
		u2.setLevel(1);
		u2.loadMapSprites();
		p2.getParty().addUnit(u2);
		
		map.processAddStack();
		int u2Uses = u2.getWeapon().getMaxUses();

		//u1.debugStat("Skl");
		//u1.debugStat("Str");
		
		// ^------- put all pre-calc stuff here
		
		CombatCalculator calc = new CombatCalculator(new UnitIdentifier(u1), new UnitIdentifier(u2), FEMultiplayer::getUnit, new TrueHitRNG(), new SimpleRNG(), new SimpleRNG());
		System.out.println(calc.getAttackQueue());
		
		
		u2.getWeapon().setUsesDEBUGGING(u2Uses);
		u1.fillHp();
		u2.fillHp();
		
		
		setCurrentStage(new FightStage(new UnitIdentifier(u1), new UnitIdentifier(u2), calc.getAttackQueue(), map, new EmptyRunnable()));
	}
	
	/**
	 * Test overworld stage.
	 */
	public void testOverworldStage() {
		testSession = new Session(new Seize(), "test", 8, new java.util.HashSet<>(), new net.fe.pick.Draft(), new TrueHitRNG(), new SimpleRNG(), new SimpleRNG());
		testSession.addPlayer(localPlayer);
		
		Player p2 = new Player("P2", (byte)1);
		p2.getParty().setColor(Party.TEAM_RED);
		testSession.addPlayer(p2);
		localPlayer.getParty().setColor(Party.TEAM_BLUE);
		p2.setTeam(2);
		localPlayer.setTeam(1);
		
		Unit u1 = UnitFactory.getUnit("Natasha");
		u1.addToInventory(WeaponFactory.getWeapon("Physic"));
		u1.setHp(1);
		localPlayer.getParty().addUnit(u1);
		
		Unit u3 = UnitFactory.getUnit("Oswin");
		u3.addToInventory(WeaponFactory.getWeapon("Silver Axe"));
		u3.equip(0);
		u3.setHp(1);
		p2.getParty().addUnit(u3);
		
		Unit u4 = UnitFactory.getUnit("Eirika");
		u4.addToInventory(WeaponFactory.getWeapon("Silver Sword"));
		u4.equip(0);
		u4.setHp(1);
		p2.getParty().addUnit(u4);
		
		Unit u2 = UnitFactory.getUnit("Lute");
		u2.addToInventory(WeaponFactory.getWeapon("Physic"));
		u2.setHp(1);
		localPlayer.getParty().addUnit(u2);
		
		currentStage = new ClientOverworldStage(testSession);

		client = new Client("nope", 12345) {
			@Override
			public void sendMessage(Message message) {
				if (message instanceof CommandMessage) {
					((ClientOverworldStage) currentStage).processCommands((CommandMessage) message);
				}
			}
		};
	}
	
	/**
	 * Gets the unit.
	 *
	 * @param id the Unit Identifier
	 * @return the unit
	 */
	public static Unit getUnit(UnitIdentifier id){
		for(Player p : getPlayers().values()){
			if(!p.isSpectator() && p.getParty().getColor().equals(id.partyColor)){
				return p.getParty().search(id.name);
			}
		}
		return null;
	}
	
	/**
	 * Connect.
	 *
	 * @param nickname player nickname
	 * @param ip the host ip
	 * @param port the host port
	 */
	public static void connect(String nickname, String ip, int port) {
		new Thread(() -> {
			getLocalPlayer().setName(nickname);
			client = new Client(ip, port);
			if(client.isOpen()) {
				postRenderRunnables.add(() -> {
					lobby = new ClientLobbyStage(client.getSession());
					setCurrentStage(lobby);
					client.start();
				});
			} else {
				currentStage.addEntity(new Notification(
						180, 120, "default_med", "ERROR: Could not connect to the server!", 5f, new Color(255, 100, 100), 0f));
		}}).start();
	}

	/* (non-Javadoc)
	 * @see chu.engine.Game#loop()
	 */
	@Override
	public void loop() {
		while(!Display.isCloseRequested()) {
			final long time = System.nanoTime();
			glClear(GL_COLOR_BUFFER_BIT |
			        GL_DEPTH_BUFFER_BIT |
			        GL_STENCIL_BUFFER_BIT);
			glClearDepth(1.0f);
			getInput();
			final ArrayList<Message> messages = new ArrayList<>();
			if(client != null){
				synchronized (client.messagesLock) {
					messages.addAll(client.messages);
					for(Message m : messages)
						client.messages.remove(m);
				}
			}
			SoundStore.get().poll(0);
			glPushMatrix();
			//Global resolution scale
//			Renderer.scale(scaleX, scaleY);
				currentStage.beginStep(messages);
				currentStage.onStep();
				currentStage.processAddStack();
				currentStage.processRemoveStack();
				currentStage.render();
//				FEResources.getBitmapFont("stat_numbers").render(
//						(int)(1.0f/getDeltaSeconds())+"", 440f, 0f, 0f);
				currentStage.endStep();
				postRenderRunnables.runAll();
			glPopMatrix();
			Display.update();
			timeDelta = System.nanoTime()-time;
		}
		AL.destroy();
		Display.destroy();
		if(client != null && client.isOpen()) client.quit();
	}
	
	/**
	 * Send. Used by the game to tell the unit on either client to attempt an action.
	 *
	 * @param u the unit Identifier
	 * @param moveX the x-wards movement
	 * @param moveY the y-wards movement
	 * @param cmds the commands for the unit
	 */
	public static void send(UnitIdentifier u, Command... cmds){
		for(Object o: cmds){
			System.out.print(o + " ");
		}
		System.out.println();
		client.sendMessage(new CommandMessage(u, null, cmds));
	}
	
	/**
	 * Sets the current stage.
	 *
	 * @param stage the new current stage
	 */
	public static void setCurrentStage(Stage stage) {
		currentStage = stage;
		if(stage.soundTrack != null){
			SoundTrack.loop(stage.soundTrack);
		}
	}
	
	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public static Client getClient() {
		return client;
	}

	/**
	 * Gets the current stage.
	 *
	 * @return the current stage
	 */
	public static Stage getCurrentStage() {
		return currentStage;
	}

	/**
	 * Sets the local player.
	 *
	 * @param p the new local player
	 */
	public static void setLocalPlayer(Player p) {
		localPlayer = p;
	}
	
	/**
	 * Gets the local player.
	 *
	 * @return the local player
	 */
	public static Player getLocalPlayer() {
		return localPlayer;
	}
	
	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public static HashMap<Byte, Player> getPlayers() {
		return getSession().getPlayerMap();
	}
	
	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public static Session getSession() {
		if(client == null)
			return testSession;
		return client.getSession();
	}
	
	/**
	 * Disconnect from game. 
	 * Allows for resetting server and client if triggered, but is not used in all situations.
	 *
	 * @param message the message
	 */
	public static void disconnectGame(String message){
		/*
		//wouldn't be hard to use something like this to reset to lobby rather than quit the game:
		//at the moment this disconnect is only in a few places between stages, i.e. while waiting
		//so it's not too bad to quit the game.
		Player leaver = null;
		for(Player p : session.getPlayers()) {
			if(p.getID() == message.origin) {
				leaver = p;
			}
		}
		session.removePlayer(leaver);
		System.out.println(leaver.getName()+" LEFT THE GAME");
		 * */
		if(FEServer.getServer() != null) {
			//boot the server back to lobby
			FEServer.resetToLobbyAndKickPlayers();
		}else{
			//exit the client
			if(message!=null && !message.equals("")){
				Sys.alert("FE:MP", message);
			}
			System.exit(0);
		}
	}

	private static final class EmptyRunnable implements Runnable {
		@Override public void run() {}
	}
	
	private static class RunnableList {
		
		private final ArrayList<Runnable> runnables = new ArrayList<Runnable>();
		
		
		public synchronized void runAll() {
			runnables.forEach(r -> r.run());
			runnables.clear();
		}
		
		public synchronized void add(Runnable runnable) {
			runnables.add(runnable);
		}
		
	}
}
