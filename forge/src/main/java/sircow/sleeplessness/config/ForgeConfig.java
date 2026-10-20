package sircow.sleeplessness.config;

import net.minecraftforge.fml.loading.FMLPaths;

public final class ForgeConfig {
    private ForgeConfig() {}

    public static void loadServer() {
        ConfigManager.loadServer(FMLPaths.CONFIGDIR.get());
    }

    public static void saveServer() {
        ConfigManager.saveServer(FMLPaths.CONFIGDIR.get());
    }
}
