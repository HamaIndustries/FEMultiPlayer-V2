package net.fe.game.pick;

import net.fe.FEMultiplayer;
import net.fe.builderStage.ClientWaitStage;
import net.fe.builderStage.TeamBuilderStage;
import net.fe.builderStage.WaitStage;
import net.fe.game.Session;
import net.fe.network.server.FEServer;

// TODO: Auto-generated Javadoc
/**
 * The Class AllPick.
 */
public class AllPick implements PickMode {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6038985000232148129L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.pick.PickMode#setUpClient(net.fe.Session)
	 */
	@Override
	public void setUpClient(Session session) {
		if (!FEMultiplayer.getLocalPlayer().isSpectator()) {
			TeamBuilderStage stage = new TeamBuilderStage(false, null, session);
			FEMultiplayer.setCurrentStage(stage);
		} else {
			ClientWaitStage stage = new ClientWaitStage(session);
			FEMultiplayer.setCurrentStage(stage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.fe.pick.PickMode#setUpServer(net.fe.Session)
	 */
	@Override
	public void setUpServer(Session session) {
		FEServer.setCurrentStage(new WaitStage(session));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "All Pick";
	}

}
