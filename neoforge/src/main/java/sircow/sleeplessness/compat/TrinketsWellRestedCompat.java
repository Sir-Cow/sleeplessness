package sircow.sleeplessness.compat;

import com.mojang.datafixers.util.Pair;
import eu.pb4.trinkets.api.TrinketDropRule;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sircow.sleeplessness.Constants;
import sircow.sleeplessness.effect.ModEffects;
import sircow.sleeplessness.other.TempInventoryStorage;

import java.util.Optional;

public final class TrinketsWellRestedCompat {
    private TrinketsWellRestedCompat() {}

    public static TrinketDropRule onTrinketDrop(TrinketDropRule rule, ItemStack stack, TrinketSlotAccess ref, LivingEntity entity) {
        if (entity instanceof Player player && player.hasEffect(ModEffects.wellRestedHolder())) {
            return TrinketDropRule.KEEP;
        }
        return rule;
    }

    public static void saveTrinkets(ServerPlayer player) {
        if (!player.hasEffect(ModEffects.wellRestedHolder())) return;

        ListTag trinketsTag = new ListTag();
        var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());

        TrinketsApi.getAttachment(player).forEach((ref, stack) -> {
            if (stack.isEmpty()) return;

            CompoundTag tag = new CompoundTag();
            tag.putString("Id", ref.slotType().getId());
            tag.putInt("Index", ref.index());
            if (ref.cosmetic()) tag.putBoolean("Cosmetic", true);
            ItemStack.CODEC.encodeStart(ops, stack)
                    .resultOrPartial(error -> Constants.LOG.error("Failed to encode trinket stack: {}", error))
                    .ifPresent(encoded -> tag.put("Item", encoded));
            trinketsTag.add(tag);
        });

        TempInventoryStorage.saveTrinkets(player.getUUID(), trinketsTag);
    }

    public static void restoreTrinkets(ServerPlayer player) {
        ListTag trinketsTag = TempInventoryStorage.takeTrinkets(player.getUUID());
        if (trinketsTag == null || trinketsTag.isEmpty()) return;

        var attachment = TrinketsApi.getAttachment(player);
        var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());

        for (Tag value : trinketsTag) {
            if (!(value instanceof CompoundTag tag)) continue;

            String slotId = tag.getStringOr("Id", "");
            int index = tag.getIntOr("Index", 0);
            boolean cosmetic = tag.getBooleanOr("Cosmetic", false);

            Optional<ItemStack> stackOpt = Optional.ofNullable(tag.get("Item"))
                    .flatMap(itemTag -> ItemStack.CODEC.decode(ops, itemTag).result())
                    .map(Pair::getFirst);

            if (stackOpt.isEmpty() || stackOpt.get().isEmpty()) continue;

            TrinketInventory inventory = attachment.getInventory(slotId);
            if (inventory == null || index < 0 || index >= inventory.getContainerSize()) continue;

            if (cosmetic) inventory.setCosmeticItem(index, stackOpt.get());
            else inventory.setItem(index, stackOpt.get());
        }
    }
}
