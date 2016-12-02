package net.fe.game.pick;

import net.fe.FEMultiplayer;
import net.fe.Player;
import net.fe.builderStage.TeamDraftStage;
import net.fe.builderStage.DraftViewStage;
import net.fe.builderStage.WaitStage;
import net.fe.game.Session;
import net.fe.network.FEServer;

/**
 * Draft picking mode.
 */
public class Draft implements PickMode {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6216203906090478045L;

	/* (non-Javadoc)
	 * @see net.fe.pick.PickMode#setUpClient(net.fe.Session)
	 */
	@Override
	public void setUpClient(Session session) {
		for(Player p : session.getPlayers()) {
			p.getParty().clear();
		}
		if(!FEMultiplayer.getLocalPlayer().isSpectator()) {
			TeamDraftStage stage = new TeamDraftStage(session); //thar ye be yeh little scumbucket TODO
			FEMultiplayer.setCurrentStage(stage);
		} else {
			DraftViewStage stage = new DraftViewStage(session);
			//System.out.println(FEMultiplayer.getLocalPlayer().getParty().getColor());
			//TeamDraftStage stage = new TeamDraftStage(session);
			FEMultiplayer.setCurrentStage(stage);
		}
	}

	/* (non-Javadoc)
	 * @see net.fe.pick.PickMode#setUpServer(net.fe.Session)
	 */
	@Override
	public void setUpServer(Session session) {
		for(Player p : session.getPlayers()) {
			p.getParty().clear();
		}
		FEServer.setCurrentStage(new WaitStage(session));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Draft";
	}

}
