package sircow.sleeplessness;

import net.fabricmc.api.ModInitializer;
import sircow.sleeplessness.config.FabricConfig;
import sircow.sleeplessness.effect.FabricModEffects;
import sircow.sleeplessness.event.FabricModEvents;
import sircow.sleeplessness.item.FabricModItemGroups;
import sircow.sleeplessness.item.FabricModItems;
import sircow.sleeplessness.trigger.FabricModTriggers;

public class FabricSleeplessness implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricConfig.loadServer();
        CommonClass.init();
        FabricModItems.registerFabricModItems();
        FabricModItemGroups.registerItemGroups();
        FabricModEvents.registerModEvents();
        FabricModEffects.registerFabricModEffects();
        FabricModTriggers.registerFabricModTriggers();
    }
}
