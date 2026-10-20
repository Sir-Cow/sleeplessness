package sircow.sleeplessness.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sircow.sleeplessness.Constants;

public class FabricModItemGroups {
    public static final ResourceKey<CreativeModeTab> SLEEPLESSNESS_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.id("sleeplessness"));
    public static CreativeModeTab SLEEPLESSNESS_GROUP;

    public static void register() {
        SLEEPLESSNESS_GROUP = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemgroup.sleeplessness.items"))
                .icon(() -> new ItemStack(ModItems.DREAMCATCHER.get()))
                .displayItems((displayContext, entries) -> {
                    entries.accept(ModItems.DREAMCATCHER.get());
                    entries.accept(ModItems.HOLLOW_TWINE.get());
                })
                .build();
        registerCreativeTab(SLEEPLESSNESS_GROUP);
    }

    private static void registerCreativeTab(CreativeModeTab tab){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, FabricModItemGroups.SLEEPLESSNESS_TAB_KEY, tab);
    }

    public static void registerItemGroups() {
        register();
    }
}
