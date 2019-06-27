package org.jdownloader.extensions.pimpmyjdownloader;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.appwork.shutdown.ShutdownController;
import org.appwork.shutdown.ShutdownRequest;
import org.appwork.shutdown.ShutdownVetoListener;
import org.appwork.utils.Application;
import org.appwork.utils.logging2.LogSource;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.jdownloader.extensions.AbstractExtension;
import org.jdownloader.extensions.ExtensionConfigPanel;
import org.jdownloader.extensions.StartException;
import org.jdownloader.extensions.pimpmyjdownloader.dto.MonkeyDownload;
import org.jdownloader.extensions.pimpmyjdownloader.save.Configuration;
import org.jdownloader.extensions.pimpmyjdownloader.translate.PimpMyJDownloaderTranslation;
import org.jdownloader.logging.LogController;

import jd.controlling.downloadcontroller.DownloadWatchDog;
import jd.controlling.downloadcontroller.SingleDownloadController;
import jd.controlling.linkcollector.LinkCollectingJob;
import jd.controlling.linkcollector.LinkCollector;
import jd.controlling.linkcollector.LinkOrigin;
import jd.controlling.linkcollector.LinkOriginDetails;
import jd.controlling.linkcrawler.CrawledLink;
import jd.plugins.AddonPanel;

/**
 * MonkeyBusiness
 *
 * @author Gorille
 *
 *
 */
public class PimpMyJDownloaderExtension extends AbstractExtension<PimpMyJDownloaderConfig, PimpMyJDownloaderTranslation>
		implements Runnable, ShutdownVetoListener {
	private final AtomicReference<Thread> currentThread = new AtomicReference<Thread>(null);
	private ExtensionConfigPanel<PimpMyJDownloaderExtension> configPanel;
	private static LogSource logger;
	private final static Integer maxLogLines = 500;
	private static List<String> logs = new ArrayList<String>();
	private Server server;
	private Integer port;
	private String ip;
	private String ipmqtt;
	private Map<String, LinkCollector> linkCrawler = new HashMap<String, LinkCollector>();
	private Map<String, MonkeyDownload> monkeysDownload = new HashMap<String, MonkeyDownload>();
	public static PimpMyJDownloaderExtension instance;
	PimpMyJDownloaderListener pimpListener;

	public ExtensionConfigPanel<PimpMyJDownloaderExtension> getConfigPanel() {
		return this.configPanel;
	}

	public boolean hasConfigPanel() {
		return true;
	}

	public PimpMyJDownloaderExtension() throws StartException {
		setTitle(this.T.jd_plugins_optional_pimpmyjdownloader_jdpimpmyjdownloader());
	}

	public boolean isLinuxRunnable() {
		return true;
	}

	public boolean isDefaultEnabled() {
		return true;
	}

	protected void stop() {
		try {
			stopServer();
		} catch (Exception e) {
			log("Can't stop server ! " + e.getMessage());
		}
		setThread(null);
	}

	protected boolean isPimpMyJDownloaderThread() {
		return (Thread.currentThread() == this.currentThread.get());
	}

	public String getIconKey() {
		try {
			InputStream src = getClass().getResourceAsStream("/images/monkey.png");
			Files.copy(src, Paths.get(Application.getHome() + "/themes/standard/org/jdownloader/images/monkey.png"),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			log("Couldn't copy icon file " + e.getMessage());
		}
		return "monkey";
		// return "settings";
	}

	public boolean isQuickToggleEnabled() {
		return true;
	}

	private void setThread(Thread thread) {
		if (thread != null) {
			ShutdownController.getInstance().addShutdownVetoListener(this);
		} else {
			ShutdownController.getInstance().removeShutdownVetoListener(this);
		}
		this.currentThread.getAndSet(thread);
		if (thread != null) {
			thread.start();
		}
	}

	protected void start() throws StartException {
		(new Thread("PimpMyJDownloader") {
			public void run() {
				Configuration.getInstance().load();
				startServer();
				while (true) {
					try {
						Long frequency = new Long(PimpMyJDownloaderConfigPanel.getInstance().getFrequency().getValue())
								.longValue();
						Thread.sleep(frequency);
						log("PimpMyJDownloader daemon " + linkCrawler.size() + " link(s)");
						analyseLink();
						logger.flush();
					} catch (InterruptedException e) {
						e.printStackTrace();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							log(e1.getMessage());
						}
						log(e.getMessage());
					}
				}
			}
		}).start();
	}

	public void stopServer() {
		try {
			log("Stopping jetty server");
			server.stop();
		} catch (Exception e) {
			log("Could'nt stop server " + e.getMessage());
		}
	}

	public void startServerBis() throws Exception {
//		List<Handler> ahandlerList = new ArrayList<Handler>();
//	
//		ServletContextHandler adminContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
//		adminContext.setContextPath("/admin");
//		adminContext.addServlet(new ServletHolder(new PimpServlet()), "/admin/*");
//		
//		
//		
//		
////		String webInfDir = "WEB-INF";
////		if (getClass().getClassLoader().getResource("WEB-INF") != null) {
////			webInfDir = getClass().getClassLoader().getResource("WEB-INF").toExternalForm();
////		}
////		webAppContext.setOverrideDescriptor(webInfDir + "/web.xml");
//
//		WebAppContext webAppContext = new WebAppContext();
//		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
//		context.addFilter(AuthenticationFilter.class, "/*", null);
//		context.setServer(server);
////		
////		webAppContext.setContextPath("/");
		String webInterfaceDir = null;
		try {
			webInterfaceDir = getClass().getClassLoader().getResource("webinterface").toExternalForm();
		} catch (Exception e) {
		}
		if (webInterfaceDir == null) {
			log("Couldn't retrieve webinterface in resource jar, bad jar");
		}
		
		log("webInterfaceDir=" + webInterfaceDir);
////		webAppContext.setResourceBase(webInterfaceDir);
////		webAppContext.setResourceBase("webinterface");
//		
//		
//		ahandlerList.add(webAppContext);
//		ahandlerList.add(adminContext);
//
//		ContextHandlerCollection contexts = new ContextHandlerCollection();
//		Handler[] handlers = new Handler[ahandlerList.size()];
//		for (int i = 0; i < ahandlerList.size(); i++) {
//			handlers[i] = ahandlerList.get(i);
//		}
//		contexts.setHandlers(handlers);
//		server.setHandler(contexts);
		
		ServletContextHandler handlerWeb = new ServletContextHandler();
		DefaultServlet servlet = new DefaultServlet();
		ServletHolder holderWeb = new ServletHolder(servlet);
		handlerWeb.setContextPath("/");
		handlerWeb.setResourceBase(webInterfaceDir);
		handlerWeb.addServlet(holderWeb, "/*");
		
		
		ServletContextHandler handlerAdmin = new ServletContextHandler();
		PimpServlet adminServlet = new PimpServlet();
		ServletHolder holderAdmin= new ServletHolder(adminServlet);
		handlerAdmin.setContextPath("/admin");
		handlerAdmin.addServlet(holderAdmin, "/*");
		
	    List<Handler> ahandlerList = new ArrayList<Handler>();
		ahandlerList.add(handlerWeb);
		ahandlerList.add(handlerAdmin);
		
		
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		Handler[] handlers = new Handler[ahandlerList.size()];
		for (int i = 0; i < ahandlerList.size(); i++) {
			handlers[i] = ahandlerList.get(i);
		}
		contexts.setHandlers(handlers);
		server.setHandler(contexts);

	    
	    
	    
	    
	    
	    

//	    server.setHandler(handler);

		
		
		org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());
		try {
			server.start();
		} catch (Exception e) {
			log("Couldn't start server " + e.getMessage());
		}
	}

	public void startServer() {
		log("Start pimpMyJDownloader! Oun Oun !");
		port = Integer.parseInt(PimpMyJDownloaderConfigPanel.getInstance().getServerPort().getText());
		if (port == null || "".equals(port)) {
			log("Port is not set, setting 8080 by default");
			port = 8080;
		}
		ip = PimpMyJDownloaderConfigPanel.getInstance().getServerIp().getText();
		InetSocketAddress socketAddress = null;
		if (ip == null || "".equals(ip)) {
			log("Ip is not set, setting by default");
			InetAddress inetadr;
			try {
				inetadr = InetAddress.getLocalHost();
				ip = inetadr.getHostAddress();
			} catch (UnknownHostException e) {
				log("Can't find ip, try with 127.0.0.1");
				ip = "127.0.0.1";
			}
			socketAddress = InetSocketAddress.createUnresolved(ip, port);
		} else {
			log("Ip is set to " + ip + ", binding with this");
			socketAddress = InetSocketAddress.createUnresolved(ip, port);
		}
		try {
			server = new Server(socketAddress);
			// ServletHandler handler = new ServletHandler();
			// server.setHandler(handler);
			// handler.addServletWithMapping(PimpServlet.class, "/*");
			//
			// log("Starting Jetty Server");
			// server.start();
			startServerBis();
			log("Server should be started @ http://" + ip + ":" + port);
			PimpMyJDownloaderConfigPanel.getInstance().refreshLinkLabel("http://" + PimpMyJDownloaderExtension.getInstance().getIp() + ":" + PimpMyJDownloaderExtension.getInstance().getPort());
		} catch (Exception exception) {
			log(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private void analyseLink() {
		for (String link : linkCrawler.keySet()) {
			LinkCollector lc = linkCrawler.get(link);
			for (CrawledLink cl : lc.getAllChildren()) {
				log("Set autostart on " + link);
				cl.setAutoConfirmEnabled(true);
				cl.setAutoStartEnabled(true);
				cl.getFinishedDate();
			}
		}
		for (SingleDownloadController sdc : DownloadWatchDog.getInstance().getRunningDownloadLinks()) {
			MonkeyDownload mdl = PimpMyJDownloaderExtension.getInstance()
					.getMonkeyDowloadByLink(sdc.getDownloadLink().getView().getDisplayUrl());
			if (mdl != null) {
				try {
					mdl.setDownloadedSize(sdc.getDownloadInstance().getDownloadable().getDownloadBytesLoaded());
					mdl.setSize(sdc.getDownloadInstance().getDownloadable().getDownloadTotalBytes());
					mdl.setFileName(sdc.getDownloadInstance().getDownloadable().getFinalFileName());
					mdl.setOutputPath(sdc.getDownloadInstance().getDownloadable().getFinalFileOutput());
				} catch (Exception e) {
					log("Couldn't update MonkeyDownload with link " + sdc.getDownloadLink().getView().getDisplayUrl());
				}
			} else {
				log("Could'nt find download with link " + sdc.getDownloadLink().getView().getDisplayUrl());
			}
		}
		for (MonkeyDownload mdl : monkeysDownload.values()) {
			log(mdl.toJson());
			sendMqttMessage(mdl.toJson());
		}
		Integer minutes = Integer.parseInt(Fonctions.getDateFormat(new Date(), "mm"));
		if (minutes % 10 == 0) {
			Long memoryMax = Runtime.getRuntime().maxMemory() / 1024 / 1024;
			Long usedMemory = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
			Long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
			log("Free memory=" + freeMemory + "Mb usedMemory=" + usedMemory + "Mb MaxMemory=" + memoryMax + "Mb");
		}
		PimpMyJDownloaderConfigPanel.getInstance()
				.refreshLinkLabel("http://" + PimpMyJDownloaderExtension.getInstance().getIp() + ":"
						+ PimpMyJDownloaderExtension.getInstance().getPort());
	}

	public synchronized static void log(String message) {
		if (logs.size() > 1 && logs.get(logs.size() - 1) != null
				&& Fonctions.getFieldFromString(logs.get(logs.size() - 1), "\\|", 0).replaceAll("^[0-9]*\\s", "A ")
						.equals("A " + message)) {
			String repeat = Fonctions.getFieldFromString(logs.get(logs.size() - 1), "\\|", 1).replace("repeating", "")
					.replace("times", "").replaceAll(" ", "");
			Integer count = 0;
			if (repeat == null || "".equals(repeat)) {
				count = 2;
			} else {
				count = Integer.parseInt(repeat);
				count++;
			}
			logs.remove(logs.size() - 1);
			message = message + "| repeating " + count + " times";
		}
		logs.add(Fonctions.getDateFormat(new Date(), null) + " " + message);
		logger.log(new LogRecord(Level.INFO, message));
		if (logs.size() > maxLogLines) {
			List<String> subLogs = new ArrayList<String>();
			subLogs = logs.subList(1, logs.size() - 1);
			logs = subLogs;
		}
	}

	public String getDescription() {
		return this.T.jd_plugins_optional_pimpmyjdownloader_jdpimpmyjdownloader_description();
	}

	public AddonPanel<PimpMyJDownloaderExtension> getGUI() {
		return null;
	}

	protected void initExtension() throws StartException {
		logger = LogController.CL(PimpMyJDownloaderExtension.class);
		if (!Application.isHeadless()) {
			this.configPanel = new PimpMyJDownloaderConfigPanel(this);
		}
		if (instance == null) {
			instance = this;
		}
		pimpListener = new PimpMyJDownloaderListener();
		pimpListener.register();
	}

	public boolean isHeadlessRunnable() {
		return true;
	}

	public void onShutdown(ShutdownRequest request) {
		stop();
		setThread(null);
	}

	public void onShutdownVeto(ShutdownRequest request) {
	}

	public void onShutdownVetoRequest(ShutdownRequest request) {
	}

	public long getShutdownVetoPriority() {
		return 0L;
	}

	public static void main(String[] args) {
		// Some bullshit main
		PimpMyJDownloaderExtension pmjd = PimpMyJDownloaderExtension.getInstance();
		pmjd.startServer();
	}

	@Override
	public void run() {
	}

	public static PimpMyJDownloaderExtension getInstance() {
		if (instance == null) {
			try {
				instance = new PimpMyJDownloaderExtension();
			} catch (StartException e) {
				log("Couldn't instantiate PimpMyJDownloaderExtension " + e.getMessage());
			}
		}
		return instance;
	}

	public static void setInstance(PimpMyJDownloaderExtension instance) {
		PimpMyJDownloaderExtension.instance = instance;
	}

	public void addLink(String link, String force) {
		if (!linkCrawler.containsKey(link) || force != null) {
			LinkCollector lc = LinkCollector.getInstance();
			LinkCollectingJob job = new LinkCollectingJob(
					LinkOriginDetails.getInstance(LinkOrigin.EXTENSION, "HTTPAPI"), link);
			lc.addCrawlerJob(job);
			linkCrawler.put(link, lc);
			MonkeyDownload mdl = new MonkeyDownload();
			mdl.setSize(Long.MAX_VALUE);
			mdl.setDownloadedSize(0l);
			mdl.setProgression(0);
			mdl.setSpeed(0l);
			mdl.setUrl(link);
			mdl.setFileName("resolving...");
			mdl.setOutputPath("");
			monkeysDownload.put(link, mdl);
		} else {
			log("Link " + link + " already in queue list, use the force to override");
		}
	}

	public void removeLinkByLink(String link) {
		linkCrawler.remove(link);
		monkeysDownload.remove(link);
	}

	public MonkeyDownload getMonkeyDowloadByLink(String link) {
		return monkeysDownload.get(link);
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpmqtt() {
		return ipmqtt;
	}

	public void setIpmqtt(String ipmqtt) {
		this.ipmqtt = ipmqtt;
	}

	public void sendMqttMessage(String string) {
		if (PimpMyJDownloaderConfigPanel.getInstance().getServerIpMqtt().getText() != null
				&& !"".equals(PimpMyJDownloaderConfigPanel.getInstance().getServerIpMqtt().getText())) {
			MQTT mqtt = new MQTT();

			mqtt.setConnectAttemptsMax(1);

			BlockingConnection connection = null;
			try {
				mqtt.setHost(PimpMyJDownloaderConfigPanel.getInstance().getServerIpMqtt().getText(),
						new Integer(PimpMyJDownloaderConfigPanel.getInstance().getServerPortMqtt().getText()));
				connection = mqtt.blockingConnection();
				connection.connect();
				connection.publish("PimpMyJDownloader", string.getBytes(), QoS.AT_LEAST_ONCE, false);
				connection.disconnect();
			} catch (Exception e) {
				log("Couldn't send mqtt message " + e.getMessage());
			} finally {
				if (connection != null) {
					try {
						connection.disconnect();
					} catch (Exception e) {
						log("Couldn't disconnect from mqtt server " + e.getMessage());
					}
				}
			}
		}
	}

	public synchronized static List<String> getLogs() {
		return logs;
	}

	public static void setLogs(List<String> logs) {
		PimpMyJDownloaderExtension.logs = logs;
	}

	public synchronized Map<String, LinkCollector> getLinkCrawler() {
		return linkCrawler;
	}

	public void setLinkCrawler(Map<String, LinkCollector> linkCrawler) {
		this.linkCrawler = linkCrawler;
	}

	public synchronized Map<String, MonkeyDownload> getMonkeysDownload() {
		return monkeysDownload;
	}

	public void setMonkeysDownload(Map<String, MonkeyDownload> monkeysDownload) {
		this.monkeysDownload = monkeysDownload;
	}

}