package org.jdownloader.extensions.pimpmyjdownloader;

import org.appwork.storage.config.ConfigUtils;
import org.appwork.storage.config.JsonConfig;
import org.appwork.storage.config.handler.BooleanKeyHandler;
import org.appwork.storage.config.handler.StorageHandler;
import org.appwork.utils.Application;

public class CFG_PIMPMYJDOWNLOADER {
    public static void main(String[] args) {
        ConfigUtils.printStaticMappings(PimpMyJDownloaderConfig.class);
    }

    public static final PimpMyJDownloaderConfig                 CFG           = JsonConfig.create(Application.getResource("cfg/" + PimpMyJDownloaderExtension.class.getName()), PimpMyJDownloaderConfig.class);
    @SuppressWarnings("unchecked")
	public static final StorageHandler<PimpMyJDownloaderConfig> SH            = (StorageHandler<PimpMyJDownloaderConfig>) CFG._getStorageHandler();
    public static final BooleanKeyHandler                       FRESH_INSTALL = SH.getKeyHandler("FreshInstall", BooleanKeyHandler.class);
    public static final BooleanKeyHandler                       GUI_ENABLED   = SH.getKeyHandler("GuiEnabled", BooleanKeyHandler.class);
    public static final BooleanKeyHandler                       ENABLED       = SH.getKeyHandler("Enabled", BooleanKeyHandler.class);
}
