package org.jdownloader.extensions.pimpmyjdownloader;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import org.appwork.utils.swing.SwingUtils;
import org.jdownloader.extensions.ExtensionConfigPanel;
import org.jdownloader.extensions.pimpmyjdownloader.save.Configuration;

/**
 * MonkeyBusiness
 *
 * @author Gorille
 *
 */
public class PimpMyJDownloaderConfigPanel extends ExtensionConfigPanel<PimpMyJDownloaderExtension> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private TextField serverIp;
	private TextField serverPort;
	private TextField serverIpMqtt;
	private TextField serverPortMqtt;
	private TextArea logs;
	private JButton restartServer;
	private JButton testMqtt;
	private JButton refreshLog;
	private JButton saveConf;
	private JButton loadConf;
	private JLabel labelFrequency;
	private JSlider frequency;
	private JLabel link;
	private JButton debug;
	private static String frequencyLab = "Refresh Frequency (mqtt message, chromeExtension update) default 5s:";
	private static MouseListener mouseListener;

	private static PimpMyJDownloaderConfigPanel instance;

	public PimpMyJDownloaderConfigPanel(PimpMyJDownloaderExtension trayExtension) {
		super(trayExtension);
		instance = this;
		add(link = new JLabel());
		add(SwingUtils.toBold(new JLabel("Ip server (default: 127.0.0.1):")));
		add(serverIp = new TextField("127.0.0.1"));
		add(SwingUtils.toBold(new JLabel("Port server (default: 8080):")));
		add(serverPort = new TextField("8080"));
		add(SwingUtils.toBold(new JLabel("Mqtt server ip/host:")));
		add(serverIpMqtt = new TextField(""));
		add(SwingUtils.toBold(new JLabel("Mqtt port (default: 1883):")));
		add(serverPortMqtt = new TextField("1883"));

		add(SwingUtils.toBold(labelFrequency = new JLabel(frequencyLab)));
		add(frequency = new JSlider(1000, 3000000, 5000));
		frequency.setMajorTickSpacing(1000);
		frequency.setMinorTickSpacing(1000);

		add(SwingUtils.toBold(new JLabel("Last logs:")));
		add(logs = new TextArea(""));
		add(restartServer = new JButton("Restart Server"));
		add(testMqtt = new JButton("Test Mqtt settings"));
		add(refreshLog = new JButton("RefreshLog"));

		add(saveConf = new JButton("Save config"));
		add(loadConf = new JButton("Load config"));
		add(debug = new JButton("Debug/Undebug Jetty"));

		// Adding listener on restartServer and testMqtt
		restartServer.addActionListener(PimpMyJDownloaderListener.getInstance());
		testMqtt.addActionListener(PimpMyJDownloaderListener.getInstance());
		refreshLog.addActionListener(PimpMyJDownloaderListener.getInstance());
		saveConf.addActionListener(PimpMyJDownloaderListener.getInstance());
		loadConf.addActionListener(PimpMyJDownloaderListener.getInstance());
		debug.addActionListener(PimpMyJDownloaderListener.getInstance());

		frequency.addChangeListener(PimpMyJDownloaderListener.getInstance());
		PimpMyJDownloaderListener.getInstance().refreshLog();

		generateMouseListener("");
	}

	private void generateMouseListener(String url) {

		mouseListener = new MouseListener() {
			public void mouseExited(MouseEvent arg0) {
				link.setText("<HTML><FONT color=\"#000099\">" + url + "</FONT></HTML>");
			}

			public void mouseEntered(MouseEvent arg0) {
				link.setText("<HTML><FONT color=\"#000099\"><U>" + url + "</U></FONT></HTML>");
			}

			public void mouseClicked(MouseEvent arg0) {
				if (Desktop.isDesktopSupported() && url != null) {
					try {
						Desktop.getDesktop().browse(new URI(url));
					} catch (Exception e) {
						PimpMyJDownloaderExtension.log("Error while setting url in parameters tab " + e.getMessage());
					}
				} else {
					JOptionPane pane = new JOptionPane("Could not open link.");
					JDialog dialog = pane.createDialog(new JFrame(), "");
					dialog.setVisible(true);
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		};
		link.addMouseListener(mouseListener);
	}

	public void refreshLinkLabel(String url) {
		link.setText("<HTML><FONT color=\"#000099\">" + url + "</FONT></HTML>");
		link.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (mouseListener != null) {
			link.removeMouseListener(mouseListener);
		}
		generateMouseListener(url);
		link.repaint();
	}

	public void setFrequencyLabel(int i) {
		labelFrequency.setText(frequencyLab + " " + i);
	}

	public void save() {
		Configuration.getInstance().save();
	}

	public void load() {
		Configuration.getInstance().load();
	}

	public void updateContents() {
		PimpMyJDownloaderListener.getInstance().refreshLog();
	}

	public TextField getServerIp() {
		return serverIp;
	}

	public void setServerIp(TextField serverIp) {
		this.serverIp = serverIp;
	}

	public TextField getServerPort() {
		return serverPort;
	}

	public void setServerPort(TextField serverPort) {
		this.serverPort = serverPort;
	}

	public TextField getServerIpMqtt() {
		return serverIpMqtt;
	}

	public void setServerIpMqtt(TextField serverIpMqtt) {
		this.serverIpMqtt = serverIpMqtt;
	}

	public TextField getServerPortMqtt() {
		return serverPortMqtt;
	}

	public void setServerPortMqtt(TextField serverPortMqtt) {
		this.serverPortMqtt = serverPortMqtt;
	}

	public synchronized TextArea getLogs() {
		return logs;
	}

	public synchronized void setLogs(TextArea logs) {
		this.logs = logs;
	}

	public JButton getRestartServer() {
		return restartServer;
	}

	public void setRestartServer(JButton restartServer) {
		this.restartServer = restartServer;
	}

	public JButton getTestMqtt() {
		return testMqtt;
	}

	public void setTestMqtt(JButton testMqtt) {
		this.testMqtt = testMqtt;
	}

	public static PimpMyJDownloaderConfigPanel getInstance() {
		return instance;
	}

	public static void setInstance(PimpMyJDownloaderConfigPanel instance) {
		PimpMyJDownloaderConfigPanel.instance = instance;
	}

	public JSlider getFrequency() {
		return frequency;
	}

	public void setFrequency(JSlider frequency) {
		this.frequency = frequency;
	}

	public JLabel getLabelFrequency() {
		return labelFrequency;
	}

	public void setLabelFrequency(JLabel labelFrequency) {
		this.labelFrequency = labelFrequency;
	}

	public JButton getSaveConf() {
		return saveConf;
	}

	public void setSaveConf(JButton saveConf) {
		this.saveConf = saveConf;
	}

	public JButton getLoadConf() {
		return loadConf;
	}

	public void setLoadConf(JButton loadConf) {
		this.loadConf = loadConf;
	}

	// public JLabel linkify(final String text, String URL, String toolTip)
	// {
	// URI temp = null;
	// try
	// {
	// temp = new URI(URL);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// final URI uri = temp;
	// //link = new JLabel();
	// link.setText("<HTML><FONT color=\"#000099\">"+text+"</FONT></HTML>");
	// if(!toolTip.equals(""))
	// link.setToolTipText(toolTip);
	// link.setCursor(new Cursor(Cursor.HAND_CURSOR));
	// link.addMouseListener(new MouseListener()
	// {
	// public void mouseExited(MouseEvent arg0)
	// {
	// link.setText("<HTML><FONT color=\"#000099\">"+text+"</FONT></HTML>");
	// }
	//
	// public void mouseEntered(MouseEvent arg0)
	// {
	// link.setText("<HTML><FONT
	// color=\"#000099\"><U>"+text+"</U></FONT></HTML>");
	// }
	//
	// public void mouseClicked(MouseEvent arg0)
	// {
	// if (Desktop.isDesktopSupported())
	// {
	// try
	// {
	// Desktop.getDesktop().browse(uri);
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// else
	// {
	// JOptionPane pane = new JOptionPane("Could not open link.");
	// JDialog dialog = pane.createDialog(new JFrame(), "");
	// dialog.setVisible(true);
	// }
	// }
	//
	// public void mousePressed(MouseEvent e)
	// {
	// }
	//
	// public void mouseReleased(MouseEvent e)
	// {
	// }
	// });
	// link.repaint();
	// return link;
	// }

	public JLabel getLink() {
		return link;
	}

	public void setLink(JLabel link) {
		this.link = link;
	}

}