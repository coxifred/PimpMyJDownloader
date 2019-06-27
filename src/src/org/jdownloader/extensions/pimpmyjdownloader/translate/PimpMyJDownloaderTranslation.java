package org.jdownloader.extensions.pimpmyjdownloader.translate;

import org.appwork.txtresource.Default;
import org.appwork.txtresource.Defaults;
import org.appwork.txtresource.TranslateInterface;

/**
 * MonkeyBusiness
 *
 * @author Gorille
 *
 */
@Defaults(lngs = { "en" })
public interface PimpMyJDownloaderTranslation extends TranslateInterface {
	/*
	 * Title Extension
	 */
	@Default(lngs = { "en" }, values = { "Simply open a webserver to receive paste link from ChromeExtension" })
    String jd_plugins_optional_pimpmyjdownloader_jdpimpmyjdownloader_description();

	/*
	 * Extension name
	 */
    @Default(lngs = { "en" }, values = { "PimpMyJDownloader" })
    String jd_plugins_optional_pimpmyjdownloader_jdpimpmyjdownloader();

    /*
     * Extension server port
     */
    @Default(lngs = { "en" }, values = { "Enter a monkey web port (default is 8080)" })
    String settings_port();
    
    /*
     * Extension server ip
     */
    @Default(lngs = { "en" }, values = { "Enter an ip for binding (or let blank for default)" })
    String settings_ip();
    
    /*
     * Extension server mqtt
     */
    @Default(lngs = { "en" }, values = { "Enter an ip for Mqtt reporting (let blank for disable)" })
    String settings_mqtt();
}