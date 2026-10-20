package sircow.sleeplessness;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sircow.sleeplessness.config.ForgeConfig;
import sircow.sleeplessness.item.ForgeModItemGroups;
import sircow.sleeplessness.platform.Services;

@Mod(Constants.MOD_ID)
public class ForgeSleeplessless {
    public ForgeSleeplessless(FMLJavaModLoadingContext context) {
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            throw new IllegalStateException(String.valueOf(Component.translatable("info.sleeplessness.conflict", Constants.MOD_ID, "Preserved: Inferno")));
        }

        ForgeConfig.loadServer();
        CommonClass.init();
        ForgeModItemGroups.CREATIVE_MODE_TABS.register(context.getModBusGroup());
    }
}
