package sircow.sleeplessness.event;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import sircow.sleeplessness.Constants;
import sircow.sleeplessness.effect.ModEffects;
import sircow.sleeplessness.item.ModItems;
import sircow.sleeplessness.trigger.ModTriggers;

import java.util.Objects;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeRegisterEventHandler {
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.ITEM, helper ->
                ModItems.getItems().forEach((id, supplier) -> helper.register(id, supplier.get()))
        );
        event.register(Registries.MOB_EFFECT, helper -> {
            Registry<MobEffect> registry = Objects.requireNonNull(event.getRegistry(Registries.MOB_EFFECT));
            ModEffects.getEffects().forEach((id, effect) -> ModEffects.getHolders().put(id, Registry.registerForHolder(registry, id, effect)));
        });
        event.register(Registries.TRIGGER_TYPE, helper ->
                ModTriggers.getTriggers().forEach(helper::register)
        );
    }
}
