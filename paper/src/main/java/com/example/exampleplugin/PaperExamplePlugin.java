package com.example.exampleplugin;

import com.example.examplemod.Constants;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        Constants.LOG.info("hi");
    }

    @Override
    public void onDisable() {
        Constants.LOG.info("bye");
    }
}
