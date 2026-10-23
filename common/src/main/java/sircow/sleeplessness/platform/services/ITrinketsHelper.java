package sircow.sleeplessness.platform.services;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface ITrinketsHelper {
    /**
     * Checks if the entity has an active (non-cosmetic) trinket matching the predicate.
     */
    default boolean hasTrinket(LivingEntity entity, Predicate<ItemStack> predicate) {
        return false;
    }

    /**
     * Damages the first active trinket matching the predicate, with trinkets break behavior.
     */
    default void damageTrinket(LivingEntity entity, Predicate<ItemStack> predicate, int amount) {
    }
}
