package org.jdownloader.extensions.pimpmyjdownloader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdownloader.controlling.download.DownloadControllerListener;
import org.jdownloader.extensions.pimpmyjdownloader.dto.MonkeyDownload;

import jd.controlling.downloadcontroller.DownloadController;
import jd.controlling.downloadcontroller.DownloadLinkCandidate;
import jd.controlling.downloadcontroller.DownloadLinkCandidateResult;
import jd.controlling.downloadcontroller.DownloadWatchDog;
import jd.controlling.downloadcontroller.DownloadWatchDogProperty;
import jd.controlling.downloadcontroller.SingleDownloadController;
import jd.controlling.downloadcontroller.event.DownloadWatchdogListener;
import jd.controlling.linkcollector.LinkCollectingJob;
import jd.controlling.linkcollector.LinkCollector;
import jd.controlling.linkcollector.LinkCollectorCrawler;
import jd.controlling.linkcollector.LinkCollectorEvent;
import jd.controlling.linkcollector.LinkCollectorListener;
import jd.controlling.linkcrawler.CrawledLink;
import jd.controlling.packagecontroller.AbstractNode;
import jd.plugins.DownloadLink;
import jd.plugins.DownloadLinkProperty;
import jd.plugins.FilePackage;
import jd.plugins.FilePackageProperty;

public class PimpMyJDownloaderListener implements ActionListener, ChangeListener {

	private static PimpMyJDownloaderListener instance;
	private DownloadWatchdogListener downloadWatchDogListener = new DownloadWatchdogListener() {

		@Override
		public void onDownloadWatchdogDataUpdate() {
			// log("downloadWatchDogListener.onDownloadWatchdogDataUpdate");

		}

		@Override
		public void onDownloadWatchdogStateIsIdle() {
			// log("downloadWatchDogListener.onDownloadWatchdogStateIsIdle");

		}

		@Override
		public void onDownloadWatchdogStateIsPause() {
			// log("downloadWatchDogListener.onDownloadWatchdogStateIsPause");

		}

		@Override
		public void onDownloadWatchdogStateIsRunning() {
			// log("downloadWatchDogListener.onDownloadWatchdogStateIsRunning");

		}

		@Override
		public void onDownloadWatchdogStateIsStopped() {
			// log("downloadWatchDogListener.onDownloadWatchdogStateIsStopped");

		}

		@Override
		public void onDownloadWatchdogStateIsStopping() {
			// log("downloadWatchDogListener.onDownloadWatchdogStateIsStopping");

		}

		@Override
		public void onDownloadControllerStart(SingleDownloadController downloadController,
				DownloadLinkCandidate candidate) {
		}

		@Override
		public void onDownloadControllerStopped(SingleDownloadController downloadController,
				DownloadLinkCandidate candidate, DownloadLinkCandidateResult result) {
		}

		@Override
		public void onDownloadWatchDogPropertyChange(DownloadWatchDogProperty propertyChange) {
			// log("downloadWatchDogListener.onDownloadWatchDogPropertyChange");

		}

	};

	private LinkCollectorListener linkCollectorListener = new LinkCollectorListener() {

		@Override
		public void onLinkCollectorAbort(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorAbort");

		}

		@Override
		public void onLinkCollectorFilteredLinksAvailable(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorFilteredLinksAvailable");

		}

		@Override
		public void onLinkCollectorFilteredLinksEmpty(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorFilteredLinksEmpty");

		}

		@Override
		public void onLinkCollectorDataRefresh(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorDataRefresh");

		}

		@Override
		public void onLinkCollectorStructureRefresh(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorStructureRefresh");

		}

		@Override
		public void onLinkCollectorContentRemoved(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorContentRemoved");

		}

		@Override
		public void onLinkCollectorContentAdded(LinkCollectorEvent event) {
			// log("downloadWatchDogListener.onLinkCollectorContentAdded");

		}

		@Override
		public void onLinkCollectorLinkAdded(LinkCollectorEvent event, CrawledLink link) {
		}

		@Override
		public void onLinkCollectorDupeAdded(LinkCollectorEvent event, CrawledLink link) {
			// log("downloadWatchDogListener.onLinkCollectorDupeAdded");
		}

		@Override
		public void onLinkCrawlerAdded(LinkCollectorCrawler crawler) {
		}

		@Override
		public void onLinkCrawlerStarted(LinkCollectorCrawler crawler) {
		}

		@Override
		public void onLinkCrawlerStopped(LinkCollectorCrawler crawler) {
			// log("downloadWatchDogListener.onLinkCrawlerStopped");

		}

		@Override
		public void onLinkCrawlerFinished() {
		}

		@Override
		public void onLinkCrawlerNewJob(LinkCollectingJob job) {
			// log("downloadWatchDogListener.onLinkCrawlerNewJob");

		}

	};

	private DownloadControllerListener downloadControllerListener = new DownloadControllerListener() {

		@Override
		public void onDownloadControllerAddedPackage(FilePackage pkg) {
			// log("DownloadControllerListener.onDownloadControllerAddedPackage");

		}

		@Override
		public void onDownloadControllerStructureRefresh(FilePackage pkg) {
		}

		@Override
		public void onDownloadControllerStructureRefresh() {
		}

		@Override
		public void onDownloadControllerStructureRefresh(AbstractNode node, Object param) {
		}

		@Override
		public void onDownloadControllerRemovedPackage(FilePackage pkg) {
		}

		@Override
		public void onDownloadControllerRemovedLinklist(List<DownloadLink> list) {
		}

		@Override
		public void onDownloadControllerUpdatedData(DownloadLink downloadlink, DownloadLinkProperty property) {
			MonkeyDownload mdl = PimpMyJDownloaderExtension.getInstance()
					.getMonkeyDowloadByLink(downloadlink.getView().getDisplayUrl());
			if (mdl != null) {
				mdl.setDownloadedSize(property.getDownloadLink().getView().getBytesLoaded());
				mdl.setSize(property.getDownloadLink().getView().getBytesTotal());
				mdl.setFileName(downloadlink.getFinalFileName());
				mdl.setOutputPath(downloadlink.getDownloadDirectory());
			} else {
				log("Could'nt find download with link " + downloadlink.getView().getDisplayUrl());
			}
		}

		@Override
		public void onDownloadControllerUpdatedData(FilePackage pkg, FilePackageProperty property) {
		}

		@Override
		public void onDownloadControllerUpdatedData(DownloadLink downloadlink) {
			// log("DownloadControllerListener.onDownloadControllerUpdatedData");
		}

		@Override
		public void onDownloadControllerUpdatedData(FilePackage pkg) {
			// log("DownloadControllerListener.onDownloadControllerUpdatedData");
		}
	};

	private synchronized void log(String message) {
		PimpMyJDownloaderExtension.log(message);
	}

	public void register() {
		DownloadController.getInstance().getEventSender().addListener(downloadControllerListener);
		DownloadWatchDog.getInstance().getEventSender().addListener(downloadWatchDogListener);
		LinkCollector.getInstance().getEventsender().addListener(linkCollectorListener);
	}

	public DownloadWatchdogListener getDownloadWatchDogListener() {
		return downloadWatchDogListener;
	}

	public void setDownloadWatchDogListener(DownloadWatchdogListener downloadWatchDogListener) {
		this.downloadWatchDogListener = downloadWatchDogListener;
	}

	public LinkCollectorListener getLinkCollectorListener() {
		return linkCollectorListener;
	}

	public void setLinkCollectorListener(LinkCollectorListener linkCollectorListener) {
		this.linkCollectorListener = linkCollectorListener;
	}

	public DownloadControllerListener getDownloadControllerListener() {
		return downloadControllerListener;
	}

	public void setDownloadControllerListener(DownloadControllerListener downloadControllerListener) {
		this.downloadControllerListener = downloadControllerListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log("Push on " + e.getActionCommand());
		if (e.getActionCommand().equals("Restart Server")) {
			log("Please wait 1 minute, while stopping jetty, could hang a little sometimes with loopback misconfiguration");
			refreshLog();
			PimpMyJDownloaderExtension.getInstance().stopServer();
			log("Please wait 1 minute, while starting jetty,could hang a little sometimes with loopback misconfiguration");
			refreshLog();
			PimpMyJDownloaderExtension.getInstance().startServer();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				log("Error while sleeping " + e1.getMessage());
			}
			
			refreshLog();
		}
		if (e.getActionCommand().equals("Test Mqtt settings")) {
			PimpMyJDownloaderExtension.getInstance().sendMqttMessage("This is a test");
			refreshLog();
		}
		if (e.getActionCommand().equals("RefreshLog")) {
			refreshLog();
		}
		if (e.getActionCommand().equals("Save config")) {
			PimpMyJDownloaderConfigPanel.getInstance().save();
			refreshLog();
		}
		if (e.getActionCommand().equals("Load config")) {
			PimpMyJDownloaderConfigPanel.getInstance().load();
			refreshLog();
		}
		if (e.getActionCommand().equals("Debug/Undebug Jetty")) {
			if ( JettyLogger.debug)
			{
				JettyLogger.debug=false;
			}else
			{
				JettyLogger.debug=true;
			}
			log("Jetty debug is now " + JettyLogger.debug);
			refreshLog();
		}
	}

	public synchronized void refreshLog() {
		PimpMyJDownloaderConfigPanel.getInstance().getLogs().setText("");
		for (String aLogLine : PimpMyJDownloaderExtension.getLogs()) {
			PimpMyJDownloaderConfigPanel.getInstance().getLogs().append(aLogLine + "\n");
		}
		PimpMyJDownloaderConfigPanel.getInstance().getLogs()
				.setCaretPosition(PimpMyJDownloaderConfigPanel.getInstance().getLogs().getText().length());
	}

	public static PimpMyJDownloaderListener getInstance() {
		if (instance == null) {
			instance = new PimpMyJDownloaderListener();
		}
		return instance;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider jslide = (JSlider) e.getSource();
		log("Change on slider " + jslide.getValue());
		PimpMyJDownloaderConfigPanel.getInstance().setFrequencyLabel(jslide.getValue());

	}

}
