package org.jdownloader.extensions.pimpmyjdownloader;

import org.appwork.storage.config.annotations.AboutConfig;
import org.appwork.storage.config.annotations.DefaultIntValue;
import org.appwork.storage.config.annotations.DescriptionForConfigEntry;

import jd.plugins.ExtensionConfigInterface;

/**
 * Monkey Business
 *
 * @author Gorille
 *
 */
public interface PimpMyJDownloaderConfig extends ExtensionConfigInterface {
    @AboutConfig
    @DefaultIntValue(8080)
    @DescriptionForConfigEntry("Port for webserver")
    Integer getServerPort();
    void setServerPort(Integer port);
    
    
    @AboutConfig
    @DescriptionForConfigEntry("Ip for webserver")
    String getServerIp();
    void setServerIp(String ip);
    
}