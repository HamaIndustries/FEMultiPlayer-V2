package net.fe.network.message;

import org.newdawn.slick.util.ResourceLoader;

import net.fe.game.Session;
import net.fe.network.Message;

/**
 * The first message sent to a client by the server. Contains information that
 * the Client should know about the server, such as session settings and
 * assigned id.
 */
public final class ClientInit extends Message {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3189568095521401022L;

	/** The id assigned to the client */
	public final byte clientID;

	/** Session data */
	public final Session session;

	/** Hashes for early version checking */
	public final Hashes hashes;

	/**
	 * Instantiates a new client init.
	 *
	 * @param origin the origin
	 * @param clientID the client id
	 * @param s the s
	 */
	public ClientInit(byte origin, byte clientID, Session s) {
		super(origin);
		this.clientID = clientID;
		this.session = s;
		this.hashes = Hashes.pullFromStatics(session.getMap());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.network.Message#toString()
	 */
	public String toString() {
		return super.toString() + "clientID = " + clientID;
	}

	public static final class Hashes implements java.io.Serializable {
		private static final long serialVersionUID = 2213612471712032639L;
		public final String version;
		/** Intended to be a hash of all avaliable items */
		public final int items;
		/** Intended to be a hash of all avaliable units */
		public final int units;
		/** Intended to be a hash of the chosen map */
		public final int map;

		/** Create a Hashes with the explicitly defined set of hashes */
		public Hashes(String version, int items, int units, int map) {
			this.version = version;
			this.items = items;
			this.units = units;
			this.map = map;
		}

		/**
		 * Create a Hashes where values are taken from various global-static
		 * sources.
		 */
		public static Hashes pullFromStatics(String levelName) {
			int mapHash = 0;
			try (java.io.InputStream in = ResourceLoader.getResourceAsStream("levels/" + levelName + ".lvl");
			        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(in)) {
				mapHash = java.util.Objects.hashCode(ois.readObject());
			} catch (java.io.IOException e) {
				mapHash = 0;
			} catch (ClassNotFoundException e) {
				mapHash = 0;
			}

			return new Hashes("??.??.??", net.fe.game.unit.Item.getAllItems().hashCode(),
			        net.fe.game.unit.UnitFactory.getAllUnits().hashCode(), mapHash);
		}

		@Override
		public int hashCode() {
			return items * 31 + units;
		}

		@Override
		public boolean equals(Object other) {
			if (other != null && other instanceof Hashes) {
				return this.units == ((Hashes) other).units && this.items == ((Hashes) other).items
				        && this.map == ((Hashes) other).map && this.version.equals(((Hashes) other).version);
			} else {
				return false;
			}
		}

		public String toString() {
			return "Hashes[units:" + units + "; items:" + items + "; map:" + map + "; version:" + version + "]";
		}
	}
}
