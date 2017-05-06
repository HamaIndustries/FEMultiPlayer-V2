package net.fe.network.serverui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.fe.network.FEServer;

/**
 * A frame containing panels related to the server.
 * Also manages transitions between the two.
 * @see FEServerMainPanel
 * @see FEServerRunPanel
 * @author wellme
 */
public class FEServerFrame extends JFrame {

	private static final long serialVersionUID = -979968310097282927L;
	private FEServerMainPanel mainPanel;
	private FEServerRunPanel runPanel;

	/**
	 * Initialize the frame.
	 */
	public FEServerFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Failed to set look and feel");
		}
		mainPanel = new FEServerMainPanel();
		mainPanel.setServerStartRunnable(this::serverStart);
		getContentPane().add(mainPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	private void serverStart() {
		remove(mainPanel);
		runPanel = new FEServerRunPanel(mainPanel.getPort());
		getContentPane().add(runPanel);
		pack();

		//Does this even need it's own thread?
		new Thread(() -> {
			FEServer feserver = new FEServer(mainPanel.getSession(), mainPanel.getPort());
			try {
				feserver.init();
				feserver.loop();
			} catch (Throwable e) {
				logError(e);
			}
		}).start();
	}
	
	private static void logError(Throwable e) {
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
		System.exit(-1);
	}
}
