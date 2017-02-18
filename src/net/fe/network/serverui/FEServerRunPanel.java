package net.fe.network.serverui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	private String ipPort;

	/**
	 * Initialize the panel.
	 */
	public FEServerRunPanel() {

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
			ipPort = InetAddress.getLocalHost().getHostAddress();
			label.setText("Server IP: " + ipPort);
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
		copyToClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ipPort != null)
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ipPort), null);
			}
		});
		panel.add(copyToClipboard);
		
	}
}
