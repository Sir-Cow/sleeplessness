package sircow.sleeplessness.effect;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import sircow.sleeplessness.Constants;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEffects {
    private static final Map<Identifier, MobEffect> EFFECTS = new LinkedHashMap<>();
    private static final Map<Identifier, Holder<MobEffect>> HOLDERS = new LinkedHashMap<>();

    public static final MobEffect WELL_RESTED = register("well_rested", () -> new SleeplessnessEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));

    private static MobEffect register(String name, Supplier<MobEffect> factory) {
        MobEffect effect = factory.get();
        EFFECTS.put(Constants.id(name), effect);
        return effect;
    }

    public static Holder<MobEffect> wellRestedHolder() {
        return HOLDERS.get(Constants.id("well_rested"));
    }

    public static Map<Identifier, MobEffect> getEffects() {
        return EFFECTS;
    }

    public static Map<Identifier, Holder<MobEffect>> getHolders() {
        return HOLDERS;
    }
}
