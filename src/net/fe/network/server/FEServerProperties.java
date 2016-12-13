package net.fe.network.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import net.fe.game.Session;
import net.fe.game.modifier.Modifier;
import net.fe.game.pick.PickMode;
import net.fe.overworldStage.objective.Objective;


@Deprecated
public final class FEServerProperties {
	
	

	private static PickMode pickMode;
	private static HashSet<Modifier> modifiers;
	private static String map;
	private static int maxUnits;
	private static Objective objective;
	
	
	public static void kickAll(){
		FEServer.resetToLobbyAndKickPlayers();
	}
	
	
	public static void startServer() {
		Thread serverThread = new Thread() {
			public void run() {
				Session s = new Session(objective, map, maxUnits, modifiers, pickMode);
				FEServer feserver = new FEServer(s);
				try {
					feserver.init();
					feserver.loop();
				} catch (Exception e) {
					System.err.println("Exception occurred, writing to logs...");
					e.printStackTrace();
					try {
						File errLog = new File("error_log_server" + System.currentTimeMillis() % 100000000 + ".log");
						PrintWriter pw = new PrintWriter(errLog);
						e.printStackTrace(pw);
						pw.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					System.exit(0);
				}
			}
		};
		serverThread.start();
	}
	
	public static void addModifier(Modifier mod){
		modifiers.add(mod);
	}


	public static PickMode getPickMode() {
		return pickMode;
	}


	public static void setPickMode(PickMode pickMode) {
		FEServerProperties.pickMode = pickMode;
	}


	public static String getMap() {
		return map;
	}


	public static void setMap(String map) {
		FEServerProperties.map = map;
	}


	public static int getMaxUnits() {
		return maxUnits;
	}


	public static void setMaxUnits(int maxUnits) {
		FEServerProperties.maxUnits = maxUnits;
	}


	public static Objective getObjective() {
		return objective;
	}


	public static void setObjective(Objective objective) {
		FEServerProperties.objective = objective;
	}
	
}
