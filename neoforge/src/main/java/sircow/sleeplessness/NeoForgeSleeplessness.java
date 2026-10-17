package sircow.sleeplessness;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgeSleeplessness {
    public NeoForgeSleeplessness(IEventBus eventBus) {
        CommonClass.init();
    }
}
