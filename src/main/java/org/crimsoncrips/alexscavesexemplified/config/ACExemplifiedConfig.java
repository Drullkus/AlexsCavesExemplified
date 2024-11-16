package org.crimsoncrips.alexscavesexemplified.config;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ACExemplifiedConfig {

    public static boolean REDOABLE_SPELUNKY_ENABLED;
    public static boolean GLUTTONY_ENABLED;
    public static int SPELUNKY_ATTEMPTS_AMOUNT;


    public static void bake() {
        try {
            SPELUNKY_ATTEMPTS_AMOUNT = ACEConfigHolder.EXEMPLIFIED.SPELUNKY_ATTEMPTS_AMOUNT.get();
            REDOABLE_SPELUNKY_ENABLED = ACEConfigHolder.EXEMPLIFIED.REDOABLE_SPELUNKY_ENABLED.get();
            GLUTTONY_ENABLED = ACEConfigHolder.EXEMPLIFIED.GLUTTONY_ENABLED.get();





        } catch (Exception e) {
            LOGGER.warn("An exception was caused trying to load the config for Alex's Caves Exemplified.");
            e.printStackTrace();
        }
    }

}
