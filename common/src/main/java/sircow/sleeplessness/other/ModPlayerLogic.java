package sircow.sleeplessness.other;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sircow.sleeplessness.config.ConfigManager;
import sircow.sleeplessness.effect.ModEffects;
import sircow.sleeplessness.item.ModItems;
import sircow.sleeplessness.platform.Services;

import java.util.Objects;

public abstract class ModPlayerLogic {
    // only allow sleeping if holding a dreamcatcher
    public static Player.BedSleepingProblem allowSleep(Player player, BlockPos pos) {
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        boolean holdingDreamcatcher = mainHandItem.getItem() == ModItems.DREAMCATCHER.get()
                || offHandItem.getItem() == ModItems.DREAMCATCHER.get()
                || Services.CURIOS.hasCurio(player, stack -> stack.is(ModItems.DREAMCATCHER.get()))
                || Services.TRINKETS.hasTrinket(player, stack -> stack.is(ModItems.DREAMCATCHER.get()));

        if (!holdingDreamcatcher) {
            player.sendOverlayMessage(Component.translatable("block.minecraft.bed.no_dreamcatcher"));
            return Player.BedSleepingProblem.OTHER_PROBLEM;
        }

        return null;
    }

    public static void damageDreamcatcher(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        if (main.is(ModItems.DREAMCATCHER.get())) main.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        else if (off.is(ModItems.DREAMCATCHER.get())) off.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
        else if (Services.CURIOS.hasCurio(player, stack -> stack.is(ModItems.DREAMCATCHER.get()))) {
            Services.CURIOS.damageCurio(player, stack -> stack.is(ModItems.DREAMCATCHER.get()), 1);
        }
        else Services.TRINKETS.damageTrinket(player, stack -> stack.is(ModItems.DREAMCATCHER.get()), 1);
    }

    public static void applyWellRested(Player player) {
        if (player.isSleepingLongEnough()) {
            MinecraftServer server = player.level().getServer();
            if (server != null) {
                boolean applyToAll = ConfigManager.getServer().wellRestedAppliesToAllPlayers;

                for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                    if (!applyToAll && player.getUUID() != serverPlayer.getUUID()) continue;

                    serverPlayer.addEffect(new MobEffectInstance(ModEffects.wellRestedHolder(), ConfigManager.getServer().wellRestedDuration, 0, false, false, true));

                    if (player.getUUID() == serverPlayer.getUUID()) serverPlayer.sendOverlayMessage(Component.translatable("effect.sleeplessness.well_rested_awake"));
                    else serverPlayer.sendOverlayMessage(Component.translatable("effect.sleeplessness.well_rested_awake_not_sleeping", player.getName()));
                }
            }
        }
    }

    public static boolean allowDeath(LivingEntity entity, DamageSource source) {
        if (entity instanceof Player player) TempInventoryStorage.savePlayerInventory(player);
        return true;
    }

    public static void handleRespawn(ServerPlayer newPlayer) {
        boolean hadWellRestedEffectOnDeath = TempInventoryStorage.restorePlayerInventory(newPlayer);

        // display message if player had well rested effect
        if (hadWellRestedEffectOnDeath) {
            Objects.requireNonNull(newPlayer.level().getServer()).execute(() -> newPlayer.sendSystemMessage(Component.translatable("effect.sleeplessness.well_rested_consume"), true));
        }
    }
}
