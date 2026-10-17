package com.example.examplemod;

import net.fabricmc.api.ModInitializer;

public class FabricExampleMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
