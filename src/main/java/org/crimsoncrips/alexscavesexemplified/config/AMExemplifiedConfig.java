package org.crimsoncrips.alexscavesexemplified.config;

import static com.mojang.text2speech.Narrator.LOGGER;

public class AMExemplifiedConfig {

    public static boolean TUSKLIN_STRUCK_ENABLED;

    public static void bake() {
        try {

            TUSKLIN_STRUCK_ENABLED = AMEConfigHolder.EXEMPLIFIED.TUSKLIN_STRUCK_ENABLED.get();




        } catch (Exception e) {
            LOGGER.warn("An exception was caused trying to load the config for Alex's Interaction.");
            e.printStackTrace();
        }
    }

}
