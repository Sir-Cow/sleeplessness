package sircow.sleeplessness.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import sircow.sleeplessness.Constants;

public class ModItemIds {
    public static final ResourceKey<Item> DREAMCATCHER = create("dreamcatcher");
    public static final ResourceKey<Item> HOLLOW_TWINE = create("hollow_twine");

    private static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }
}
