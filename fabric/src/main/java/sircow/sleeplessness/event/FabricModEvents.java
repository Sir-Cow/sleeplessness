package sircow.sleeplessness.event;

import eu.pb4.trinkets.api.event.TrinketDropCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import sircow.sleeplessness.compat.TrinketsWellRestedCompat;
import sircow.sleeplessness.other.ModPlayerLogic;
import sircow.sleeplessness.platform.Services;

public class FabricModEvents {
    public static void modifySleeping() {
        EntitySleepEvents.ALLOW_SLEEPING.register(ModPlayerLogic::allowSleep);

        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) ModPlayerLogic.applyWellRested(player);
        });
    }

    public static void handleEntityDeath() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damageAmount) -> {
            if (livingEntity instanceof ServerPlayer serverPlayer && Services.PLATFORM.isModLoaded("trinkets_updated")) {
                TrinketsWellRestedCompat.saveTrinkets(serverPlayer);
            }
            return ModPlayerLogic.allowDeath(livingEntity, damageSource);
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            ModPlayerLogic.handleRespawn(newPlayer);
            if (Services.PLATFORM.isModLoaded("trinkets_updated")) {
                TrinketsWellRestedCompat.restoreTrinkets(newPlayer);
            }
        });
    }

    public static void registerTrinketsCompat() {
        if (Services.PLATFORM.isModLoaded("trinkets_updated")) {
            TrinketDropCallback.EVENT.register(TrinketDropCallback.EVENT.defaultPhaseId(), TrinketsWellRestedCompat::onTrinketDrop);
        }
    }

    public static void registerModEvents() {
        modifySleeping();
        handleEntityDeath();
        registerTrinketsCompat();
    }
}
