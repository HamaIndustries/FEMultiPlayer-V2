package net.fe.network.message;

import net.fe.Session;
import net.fe.network.Message;

/**
 * The first message sent to a client by the server. Contains information that
 * the Client should know about the server, such as session settings and assigned id.
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
		this.hashes = Hashes.pullFromStatics();
	}
	
	/* (non-Javadoc)
	 * @see net.fe.network.Message#toString()
	 */
	public String toString(){
		return super.toString() + "clientID = " + clientID;
	}
	
	
	public static final class Hashes implements java.io.Serializable {
		private static final long serialVersionUID = 2213612471712032639L;
		public final String version;
		/** Intended to be a hash of all avaliable items */
		public final int items;
		/** Intended to be a hash of all avaliable units */
		public final int units;
		
		public Hashes(String version, int items, int units) {
			this.version = version;
			this.items = items;
			this.units = units;
		}
		
		public static Hashes pullFromStatics() {
			return new Hashes(
				"??.??.??",
				net.fe.unit.Item.getAllItems().hashCode(),
				net.fe.unit.UnitFactory.getAllUnits().hashCode()
			);
		}
		
		@Override
		public int hashCode() { return items * 31 + units; }
		
		@Override
		public boolean equals(Object other) {
			if (other != null && other instanceof Hashes) {
				return this.units == ((Hashes) other).units &&
					this.items ==  ((Hashes) other).items &&
					this.version.equals(((Hashes) other).version);
			} else {
				return false;
			}
		}
		
		public String toString() {
			return "Hashes[units:" + units + "; items:" + items + "; version:" + version + "]"; 
		}
	}
}
