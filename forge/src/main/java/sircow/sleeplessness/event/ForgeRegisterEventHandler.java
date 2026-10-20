package sircow.sleeplessness.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import sircow.sleeplessness.Constants;
import sircow.sleeplessness.effect.ModEffects;
import sircow.sleeplessness.item.ModItems;
import sircow.sleeplessness.trigger.ModTriggers;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeRegisterEventHandler {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper ->
                ModItems.getItems().forEach((id, supplier) -> helper.register(id, supplier.get()))
        );
        event.register(Registries.MOB_EFFECT, helper -> {
            ModEffects.getEffects().forEach(helper::register);
            IForgeRegistry<MobEffect> registry = Objects.requireNonNull(event.getForgeRegistry());
            ModEffects.getEffects().keySet().forEach(id -> ModEffects.getHolders().put(id, registry.getHolder(id).orElseThrow(() -> new IllegalStateException("Missing mob effect holder for " + id))));
        });
        event.register(Registries.TRIGGER_TYPE, helper ->
                ModTriggers.getTriggers().forEach(helper::register)
        );
    }
}
