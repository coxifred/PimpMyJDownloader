package org.jdownloader.extensions.pimpmyjdownloader.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.appwork.utils.Application;
import org.jdownloader.extensions.pimpmyjdownloader.PimpMyJDownloaderConfigPanel;
import org.jdownloader.extensions.pimpmyjdownloader.PimpMyJDownloaderExtension;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Configuration {

	String serverIp = "";
	Integer serverPort = 8080;
	String serverIpMqtt = "";
	Integer serverPortMqtt = 1883;
	Integer frequency = 5000;

	transient static String configFile = Application.getHome() + "/pimpMyJDownloader.json";
	transient static Configuration instance;

	public void save() {
		PimpMyJDownloaderExtension.log("Saving configuration into " + configFile);
		Gson aGson = new Gson();
		setServerIp(PimpMyJDownloaderConfigPanel.getInstance().getServerIp().getText());
		setServerPort(Integer.parseInt(PimpMyJDownloaderConfigPanel.getInstance().getServerPort().getText()));
		setServerIpMqtt(PimpMyJDownloaderConfigPanel.getInstance().getServerIpMqtt().getText());
		setServerPortMqtt(Integer.parseInt(PimpMyJDownloaderConfigPanel.getInstance().getServerPortMqtt().getText()));
		setFrequency(PimpMyJDownloaderConfigPanel.getInstance().getFrequency().getValue());
		
		try {
			String content=aGson.toJson(Configuration.getInstance());
			FileWriter fw=new FileWriter(configFile);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			PimpMyJDownloaderExtension.log("Couldn't save configuration into " + configFile + " " + e.getMessage());
		}
	}

	public void load() {
		PimpMyJDownloaderExtension.log("Loading configuration from " + configFile);
		File aFile = new File(configFile);
		if (aFile.exists()) {
			Gson aGson = new Gson();
			
			try {
				JsonReader reader = new JsonReader(new FileReader(configFile));
				Configuration cfg = aGson.fromJson(reader, Configuration.class);
				if ( cfg != null)
				{
				PimpMyJDownloaderConfigPanel.getInstance().getServerIp().setText(cfg.getServerIp());
				PimpMyJDownloaderConfigPanel.getInstance().getServerPort().setText(cfg.getServerPort().toString());
				PimpMyJDownloaderConfigPanel.getInstance().getServerIpMqtt().setText(cfg.getServerIpMqtt());
				PimpMyJDownloaderConfigPanel.getInstance().getServerPortMqtt()
						.setText(cfg.getServerPortMqtt().toString());
				PimpMyJDownloaderConfigPanel.getInstance().getFrequency().setValue(cfg.getFrequency());
				PimpMyJDownloaderConfigPanel.getInstance().setFrequencyLabel(cfg.getFrequency());
				PimpMyJDownloaderConfigPanel.getInstance().refreshLinkLabel("http://" + PimpMyJDownloaderExtension.getInstance().getIp() + ":" + PimpMyJDownloaderExtension.getInstance().getPort());
				}else
				{
					PimpMyJDownloaderExtension.log("Couldn't read configuration " + configFile + " file must be empty or not json");
				}
			} catch (Exception e) {
				PimpMyJDownloaderExtension.log("Couldn't read configuration " + configFile + " " + e.getMessage());
			}
		} else {
			PimpMyJDownloaderExtension.log(configFile + " doesn't exist.");
		}
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerIpMqtt() {
		return serverIpMqtt;
	}

	public void setServerIpMqtt(String serverIpMqtt) {
		this.serverIpMqtt = serverIpMqtt;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public Integer getServerPortMqtt() {
		return serverPortMqtt;
	}

	public void setServerPortMqtt(Integer serverPortMqtt) {
		this.serverPortMqtt = serverPortMqtt;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

}
