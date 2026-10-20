package sircow.sleeplessness.platform;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sircow.sleeplessness.platform.services.ICuriosHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.function.Predicate;

public class NeoForgeCuriosHelper implements ICuriosHelper {
    @Override
    public boolean hasCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(entity)
                .map(handler -> {
                    for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                        var stacks = entry.getValue().getStacks();

                        for (int i = 0; i < stacks.getSlots(); i++) {
                            ItemStack stack = stacks.getStackInSlot(i);
                            if (!stack.isEmpty() && predicate.test(stack)) return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public void damageCurio(LivingEntity entity, Predicate<ItemStack> predicate, int amount) {
        if (!(entity instanceof ServerPlayer serverPlayer)) return;

        CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                var stacks = entry.getValue().getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!stack.isEmpty() && predicate.test(stack)) {
                        SlotContext slotContext = new SlotContext(entry.getKey(), entity, i, false, true);
                        stack.hurtAndBreak(amount, serverPlayer.level(), serverPlayer, broken -> CuriosApi.broadcastCurioBreakEvent(slotContext));
                        return;
                    }
                }
            }
        });
    }
}
