package sircow.sleeplessness.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    public static final LinkedHashMap<ResourceKey<Item>, Supplier<Item>> ITEMS = new LinkedHashMap<>();

    public static final Supplier<Item> DREAMCATCHER = registerItem(ModItemIds.DREAMCATCHER, () -> new Item.Properties()
            .durability(0)
            .stacksTo(1)
    );
    public static final Supplier<Item> HOLLOW_TWINE = registerItem(ModItemIds.HOLLOW_TWINE, Item.Properties::new);

    private static Supplier<Item> registerItem(ResourceKey<Item> id, Supplier<Item.Properties> propertiesSupplier) {
        return registerItem(id, Item::new, propertiesSupplier);
    }

    private static Supplier<Item> registerItem(ResourceKey<Item> id, Function<Item.Properties, Item> factory, Supplier<Item.Properties> propertiesSupplier) {
        Supplier<Item> memoizedSupplier = new Supplier<>() {
            private Item instance;

            @Override
            public Item get() {
                if (instance == null) instance = factory.apply(propertiesSupplier.get().setId(id));
                return instance;
            }
        };

        ITEMS.put(id, memoizedSupplier);
        return memoizedSupplier;
    }

    public static Map<ResourceKey<Item>, Supplier<Item>> getItems() {
        return ITEMS;
    }

    public static void registerModItems() {}
}
