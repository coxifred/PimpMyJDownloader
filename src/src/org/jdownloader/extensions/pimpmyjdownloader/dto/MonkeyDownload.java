package org.jdownloader.extensions.pimpmyjdownloader.dto;

import java.util.Date;

import org.jdownloader.extensions.pimpmyjdownloader.PimpMyJDownloaderExtension;

import com.google.gson.Gson;

public class MonkeyDownload {

	String url = "";

	Long size;
	Long downloadedSize;
	String fileName="resolving...";
	String outputPath;
	Long speed;
	Integer progression;
	Long startTime=new Date().getTime();
	Long stopTime;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getDownloadedSize() {
		return downloadedSize;
	}

	public void setDownloadedSize(Long downloadedSize) {

		setProgression(new Long(downloadedSize * 100 / size).intValue());
		this.downloadedSize=downloadedSize;
		if ( progression == 100)
		{
			stopTime=new Date().getTime();
		}
		statSpeed();
	}

	public void statSpeed() {
		Long current=new Date().getTime();
		Long elapsed=(current - startTime) / 1000;
		if ( stopTime != null && stopTime != 0)
		{
		elapsed=(stopTime - startTime) / 1000;
		}
		if ( elapsed > 0)
		{
		setSpeed(downloadedSize/1024/elapsed);
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

	public Integer getProgression() {
		return progression;
	}

	public void setProgression(Integer progression) {
		this.progression = progression; 
	}

	public String toJson() {
		Gson aGson = new Gson();
		return aGson.toJson(this);
	}

	public Boolean isFinished() {
		statSpeed();
		if (progression >= 100) {
			return true;
		}
		return false;
	}
	
	public void clearMe()
	{
		PimpMyJDownloaderExtension.getInstance().getLinkCrawler().remove(url);
		PimpMyJDownloaderExtension.getInstance().getMonkeysDownload().remove(url);
	}

	public Long getStopTime() {
		return stopTime;
	}

	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	
	
}
