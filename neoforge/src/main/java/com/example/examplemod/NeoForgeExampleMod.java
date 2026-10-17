package com.example.examplemod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgeExampleMod {
    public NeoForgeExampleMod(IEventBus eventBus) {
        CommonClass.init();
    }
}
