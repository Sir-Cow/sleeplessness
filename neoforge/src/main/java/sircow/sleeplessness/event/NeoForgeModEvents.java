package sircow.sleeplessness.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import sircow.sleeplessness.Constants;
import sircow.sleeplessness.compat.CuriosWellRestedCompat;
import sircow.sleeplessness.compat.TrinketsWellRestedCompat;
import sircow.sleeplessness.other.ModPlayerLogic;
import sircow.sleeplessness.platform.Services;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeModEvents {
    @SubscribeEvent
    public static void onSleep(CanPlayerSleepEvent event) {
        if (event.getProblem() != null) return;

        Player.BedSleepingProblem problem = ModPlayerLogic.allowSleep(event.getEntity(), event.getPos());
        if (problem != null) event.setProblem(problem);
    }

    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        ModPlayerLogic.applyWellRested(event.getEntity());
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (Services.PLATFORM.isModLoaded("curios")) CuriosWellRestedCompat.saveCurios(serverPlayer);
            if (Services.PLATFORM.isModLoaded("trinkets_updated")) TrinketsWellRestedCompat.saveTrinkets(serverPlayer);
        }
        if (!ModPlayerLogic.allowDeath(event.getEntity(), event.getSource())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ModPlayerLogic.handleRespawn(player);
        if (Services.PLATFORM.isModLoaded("curios")) CuriosWellRestedCompat.restoreCurios(player);
        if (Services.PLATFORM.isModLoaded("trinkets_updated")) TrinketsWellRestedCompat.restoreTrinkets(player);
    }
}
