package sircow.sleeplessness.config;

import net.neoforged.fml.loading.FMLPaths;

public final class NeoForgeConfig {
    private NeoForgeConfig() {}

    public static void loadServer() {
        ConfigManager.loadServer(FMLPaths.CONFIGDIR.get());
    }

    public static void saveServer() {
        ConfigManager.saveServer(FMLPaths.CONFIGDIR.get());
    }
}
