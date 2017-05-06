package net.fe.network.serverui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.BoxLayout;
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

	private JPanel mainPanel;

	private static final long serialVersionUID = 8683661864175580614L;

	private String ip;
	public FEServerRunPanel() {
		this(FEServer.DEFAULT_PORT);
	}

	/**
	 * Initialize the panel.
	 */
	public FEServerRunPanel(int port) {

		setLayout(new BorderLayout(0, 0));
		mainPanel = new JPanel();
		add(mainPanel, BorderLayout.CENTER);

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText("TODO");
		label.setFont(getFont().deriveFont(20f));
		//label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			String text = "Server IP:" + ip;
			if (port != FEServer.DEFAULT_PORT)
				text += ":" + port;
			label.setText(text);
		} catch (UnknownHostException e) {
			label.setText("Error occured while getting the IP");
		}

		mainPanel.add(label, BorderLayout.NORTH);

		Component verticalStrut = Box.createVerticalStrut(20);
		mainPanel.add(verticalStrut);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton kickAll = new JButton("Kick all");
		kickAll.addActionListener((e2) -> FEServer.resetToLobbyAndKickPlayers());
		panel.add(kickAll);

		JButton copyToClipboard = new JButton("Copy IP to clipboard");
		copyToClipboard.addActionListener(e -> {
			if (ip == null)
				return;
			String text = ip;
			if (port != FEServer.DEFAULT_PORT)
				text += ":" + port;
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);

		});
		panel.add(copyToClipboard);

	}

	public void setPort(int port) {
	}

}
