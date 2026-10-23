package sircow.sleeplessness.platform;

import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sircow.sleeplessness.platform.services.ITrinketsHelper;

import java.util.function.Predicate;

public class FabricTrinketsHelper implements ITrinketsHelper {
    @Override
    public boolean hasTrinket(LivingEntity entity, Predicate<ItemStack> predicate) {
        return TrinketsApi.getAttachment(entity).findFirst(predicate, true).isPresent();
    }

    @Override
    public void damageTrinket(LivingEntity entity, Predicate<ItemStack> predicate, int amount) {
        TrinketsApi.getAttachment(entity).findFirst(predicate, true)
                .ifPresent(access -> TrinketsApi.hurtAndBreakItemStack(access.get(), amount, entity, access));
    }
}
