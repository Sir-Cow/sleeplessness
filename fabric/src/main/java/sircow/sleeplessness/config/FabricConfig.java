package sircow.sleeplessness.config;

import net.fabricmc.loader.api.FabricLoader;

public final class FabricConfig {
    private FabricConfig() {}

    public static void loadServer() {
        ConfigManager.loadServer(FabricLoader.getInstance().getConfigDir());
    }

    public static void saveServer() {
        ConfigManager.saveServer(FabricLoader.getInstance().getConfigDir());
    }
}
