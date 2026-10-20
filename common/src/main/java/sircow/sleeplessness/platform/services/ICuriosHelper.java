package sircow.sleeplessness.platform.services;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface ICuriosHelper {
    /**
     * Checks if the entity has a non-cosmetic curio matching the predicate.
     */
    default boolean hasCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        return false;
    }

    /**
     * Damages the first non-cosmetic curio matching the predicate, with curio break behavior.
     */
    default void damageCurio(LivingEntity entity, Predicate<ItemStack> predicate, int amount) {
    }
}
