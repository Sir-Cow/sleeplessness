package sircow.sleeplessness.platform;

import sircow.sleeplessness.Constants;
import sircow.sleeplessness.platform.services.ICuriosHelper;
import sircow.sleeplessness.platform.services.IPlatformHelper;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final ICuriosHelper CURIOS = loadCuriosHelper();

    private static ICuriosHelper loadCuriosHelper() {
        if (PLATFORM.isModLoaded("curios")) {
            return loadOptional(ICuriosHelper.class, () -> new ICuriosHelper() {});
        }
        return new ICuriosHelper() {};
    }

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    public static <T> T loadOptional(Class<T> clazz, Supplier<T> fallback) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseGet(fallback);
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}