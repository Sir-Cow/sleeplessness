package sircow.sleeplessness.event;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.world.entity.player.Player;
import sircow.sleeplessness.other.ModPlayerLogic;

public class FabricModEvents {
    public static void modifySleeping() {
        EntitySleepEvents.ALLOW_SLEEPING.register(ModPlayerLogic::allowSleep);

        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) ModPlayerLogic.applyWellRested(player);
        });
    }

    public static void handleEntityDeath() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damageAmount) -> ModPlayerLogic.allowDeath(livingEntity, damageSource));

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> ModPlayerLogic.handleRespawn(newPlayer));
    }

    public static void registerModEvents() {
        modifySleeping();
        handleEntityDeath();
    }
}
