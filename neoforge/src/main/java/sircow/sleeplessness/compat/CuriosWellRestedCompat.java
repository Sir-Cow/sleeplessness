package sircow.sleeplessness.compat;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sircow.sleeplessness.Constants;
import sircow.sleeplessness.effect.ModEffects;
import sircow.sleeplessness.other.TempInventoryStorage;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

public final class CuriosWellRestedCompat {
    private CuriosWellRestedCompat() {}

    public static void onCurioDrops(CurioDropsEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModEffects.wellRestedHolder())) {
            event.setCanceled(true);
        }
    }

    public static void saveCurios(ServerPlayer player) {
        if (!player.hasEffect(ModEffects.wellRestedHolder())) return;

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            ListTag curiosTag = new ListTag();
            var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());

            handler.getCurios().forEach((id, stacksHandler) -> {
                saveStacks(curiosTag, ops, id, stacksHandler.getStacks(), false);
                saveStacks(curiosTag, ops, id, stacksHandler.getCosmeticStacks(), true);
            });

            TempInventoryStorage.saveCurios(player.getUUID(), curiosTag);
        });
    }

    private static void saveStacks(ListTag curiosTag, RegistryOps<Tag> ops, String id, IDynamicStackHandler stacks, boolean cosmetic) {
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            CompoundTag tag = new CompoundTag();
            tag.putString("Id", id);
            tag.putInt("Index", i);
            if (cosmetic) tag.putBoolean("Cosmetic", true);
            ItemStack.CODEC.encodeStart(ops, stack)
                    .resultOrPartial(error -> Constants.LOG.error("Failed to encode curio stack: {}", error))
                    .ifPresent(encoded -> tag.put("Item", encoded));
            curiosTag.add(tag);
        }
    }

    public static void restoreCurios(ServerPlayer player) {
        ListTag curiosTag = TempInventoryStorage.takeCurios(player.getUUID());
        if (curiosTag == null || curiosTag.isEmpty()) return;

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            var ops = RegistryOps.create(NbtOps.INSTANCE, player.level().registryAccess());

            for (Tag value : curiosTag) {
                if (!(value instanceof CompoundTag tag)) continue;

                String id = tag.getStringOr("Id", "");
                int index = tag.getIntOr("Index", 0);
                boolean cosmetic = tag.getBooleanOr("Cosmetic", false);

                Optional<ItemStack> stackOpt = Optional.ofNullable(tag.get("Item"))
                        .flatMap(itemTag -> ItemStack.CODEC.decode(ops, itemTag).result())
                        .map(Pair::getFirst);

                if (stackOpt.isEmpty() || stackOpt.get().isEmpty()) continue;

                ItemStack stack = stackOpt.get();
                if (cosmetic) {
                    handler.getStacksHandler(id).ifPresent(stacksHandler -> stacksHandler.getCosmeticStacks().setStackInSlot(index, stack));
                }
                else {
                    handler.setEquippedCurio(id, index, stack);
                }
            }
        });
    }
}
