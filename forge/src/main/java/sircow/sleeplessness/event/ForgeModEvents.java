package sircow.sleeplessness.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sircow.sleeplessness.Constants;
import sircow.sleeplessness.other.ModPlayerLogic;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeModEvents {
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        ModPlayerLogic.applyWellRested(event.getEntity());
    }

    @SubscribeEvent
    public static boolean onLivingDeath(LivingDeathEvent event) {
        return !ModPlayerLogic.allowDeath(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ModPlayerLogic.handleRespawn((ServerPlayer) event.getEntity());
    }
}
