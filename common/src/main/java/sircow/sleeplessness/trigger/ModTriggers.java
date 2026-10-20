package sircow.sleeplessness.trigger;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.resources.Identifier;
import sircow.sleeplessness.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModTriggers {
    private static final Map<Identifier, CriterionTrigger<?>> TRIGGERS = new LinkedHashMap<>();

    public static final CustomTrigger BED_SPAWN_POINT = register("bed_spawn_point", new CustomTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        TRIGGERS.put(Constants.id(name), trigger);
        return trigger;
    }

    public static Map<Identifier, CriterionTrigger<?>> getTriggers() {
        return TRIGGERS;
    }
}
