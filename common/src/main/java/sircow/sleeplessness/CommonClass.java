package sircow.sleeplessness;

import sircow.sleeplessness.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOG.info("Initialising {}", Constants.MOD_NAME);
        }
    }
}
