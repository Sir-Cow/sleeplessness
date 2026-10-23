package sircow.sleeplessness;

import eu.pb4.trinkets.api.event.TrinketDropCallback;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import sircow.sleeplessness.compat.CuriosWellRestedCompat;
import sircow.sleeplessness.compat.TrinketsWellRestedCompat;
import sircow.sleeplessness.config.NeoForgeConfig;
import sircow.sleeplessness.item.NeoForgeModItemGroups;
import sircow.sleeplessness.platform.Services;

@Mod(Constants.MOD_ID)
public class NeoForgeSleeplessness {
    public NeoForgeSleeplessness(IEventBus modEventBus, ModContainer container) {
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            throw new IllegalStateException(String.valueOf(Component.translatable("info.sleeplessness.conflict", Constants.MOD_ID, "Preserved: Inferno")));
        }

        NeoForgeConfig.loadServer();
        CommonClass.init();
        NeoForgeModItemGroups.CREATIVE_MODE_TABS.register(modEventBus);

        if (Services.PLATFORM.isModLoaded("curios")) {
            NeoForge.EVENT_BUS.addListener(CuriosWellRestedCompat::onCurioDrops);
        }

        if (Services.PLATFORM.isModLoaded("trinkets_updated")) {
            TrinketDropCallback.EVENT.register(TrinketDropCallback.EVENT.defaultPhaseId(), TrinketsWellRestedCompat::onTrinketDrop);
        }
    }
}
