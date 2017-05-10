package net.fe.network.serverui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.fe.network.FEServer;

/**
 * The second panel displayed to the server's host.
 * @see FEServerMainPanel
 * @see FEServerRunPanel
 * @author wellme
 */
public class FEServerRunPanel extends JPanel {

	private static final long serialVersionUID = 8683661864175580614L;

	private String ip;
	

	/**
	 * Initialize the panel.
	 */
	public FEServerRunPanel() {
		this(FEServer.DEFAULT_PORT);
	}


	/**
	 * Initialize the panel with the specified port.
	 * @param port The port on which the server runs.
	 */
	public FEServerRunPanel(int port) {
		
		setLayout(new BorderLayout(0, 0));
		JLabel lblServerAddress = new JLabel();
		
		add(lblServerAddress, BorderLayout.NORTH);
		lblServerAddress.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerAddress.setFont(getFont().deriveFont(20f));
		
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			String text = "Server IP:" + ip;
			if (port != FEServer.DEFAULT_PORT)
				text += ":" + port;
			lblServerAddress.setText(text);
		} catch (UnknownHostException e) {
			lblServerAddress.setText("Error occured while getting the IP");
		}

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnKickAll = new JButton("Kick all");
		btnKickAll.addActionListener((e2) -> FEServer.resetToLobbyAndKickPlayers());
		panel.add(btnKickAll);

		JButton btnCopyToClipboard = new JButton("Copy IP to clipboard");
		btnCopyToClipboard.addActionListener(e -> {
			if (ip == null)
				return;
			String text = ip;
			if (port != FEServer.DEFAULT_PORT)
				text += ":" + port;
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);

		});
		panel.add(btnCopyToClipboard);
	}
}
