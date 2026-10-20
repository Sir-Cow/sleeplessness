package sircow.sleeplessness.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sircow.sleeplessness.Constants;

public class NeoForgeModItemGroups {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SLEEPLESSNESS_GROUP =
            CREATIVE_MODE_TABS.register("sleeplessness", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemgroup.sleeplessness.items"))
                            .icon(() -> new ItemStack(ModItems.DREAMCATCHER.get()))
                            .displayItems((displayContext, entries) -> {
                                entries.accept(ModItems.DREAMCATCHER.get());
                                entries.accept(ModItems.HOLLOW_TWINE.get());
                            })
                            .build()
            );
}
