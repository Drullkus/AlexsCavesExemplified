package org.crimsoncrips.alexscavesexemplified.config;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ACExemplifiedConfig {

    public static boolean REDOABLE_SPELUNKY_ENABLED;
    public static boolean GLUTTONY_ENABLED;
    public static boolean REGULAR_REFERENCE_ENABLED;
    public static boolean GINGERBREAD_AMPUTATION_ENABLED;
    public static boolean CANIAC_SENSITIVITY;
    public static boolean GINGERBREAD_HIVEMIND_ENABLED;
    public static boolean FORLORN_LIGHT_EFFECT_ENABLED;
    public static boolean VESPER_CANNIBALIZE_ENABLED;
    public static boolean STABILIZER_COMPATIBILITY_ENABLED;
    public static boolean FROSTMINT_SOLIDIFIED_ENABLED;
    public static boolean CORRODENT_CONVERSION_ENABLED;
    public static boolean VANILLA_ADAPTIONS_ENABLED;
    public static boolean AMPLIFIED_FROSTMINT_EXPLOSION_ENABLED;
    public static boolean IRRADIATION_WASHOFF_ENABLED;

    public static boolean BURST_OUT_ENABLED;

    public static boolean RATATATATATA_ENABLED;


    public static boolean PIERCE_DARK_ARROWS_ENABLED;


    public static int SPELUNKY_ATTEMPTS_AMOUNT;


    public static void bake() {
        try {
            VANILLA_ADAPTIONS_ENABLED = ACEConfigHolder.EXEMPLIFIED.VANILLA_ADAPTIONS_ENABLED.get();
            AMPLIFIED_FROSTMINT_EXPLOSION_ENABLED = ACEConfigHolder.EXEMPLIFIED.AMPLIFIED_FROSTMINT_EXPLOSION_ENABLED.get();
            FROSTMINT_SOLIDIFIED_ENABLED = ACEConfigHolder.EXEMPLIFIED.FROSTMINT_SOLIDIFIED_ENABLED.get();
            CANIAC_SENSITIVITY = ACEConfigHolder.EXEMPLIFIED.CANIAC_SENSITIVITY.get();
            IRRADIATION_WASHOFF_ENABLED = ACEConfigHolder.EXEMPLIFIED.IRRADIATION_WASHOFF_ENABLED.get();

            BURST_OUT_ENABLED = ACEConfigHolder.EXEMPLIFIED.BURST_OUT_ENABLED.get();
            STABILIZER_COMPATIBILITY_ENABLED = ACEConfigHolder.EXEMPLIFIED.STABILIZER_COMPATIBILITY_ENABLED.get();
            VESPER_CANNIBALIZE_ENABLED = ACEConfigHolder.EXEMPLIFIED.VESPER_CANNIBALIZE_ENABLED.get();
            CORRODENT_CONVERSION_ENABLED = ACEConfigHolder.EXEMPLIFIED.CORRODENT_CONVERSION_ENABLED.get();
            RATATATATATA_ENABLED = ACEConfigHolder.EXEMPLIFIED.RATATATATATA_ENABLED.get();

            PIERCE_DARK_ARROWS_ENABLED = ACEConfigHolder.EXEMPLIFIED.PIERCE_DARK_ARROWS_ENABLED.get();



            SPELUNKY_ATTEMPTS_AMOUNT = ACEConfigHolder.EXEMPLIFIED.SPELUNKY_ATTEMPTS_AMOUNT.get();
            REDOABLE_SPELUNKY_ENABLED = ACEConfigHolder.EXEMPLIFIED.REDOABLE_SPELUNKY_ENABLED.get();
            GLUTTONY_ENABLED = ACEConfigHolder.EXEMPLIFIED.GLUTTONY_ENABLED.get();
            REGULAR_REFERENCE_ENABLED = ACEConfigHolder.EXEMPLIFIED.REGULAR_REFERENCE_ENABLED.get();
            GINGERBREAD_AMPUTATION_ENABLED = ACEConfigHolder.EXEMPLIFIED.GINGERBREAD_AMPUTATION_ENABLED.get();
            GINGERBREAD_HIVEMIND_ENABLED = ACEConfigHolder.EXEMPLIFIED.GINGERBREAD_HIVEMIND_ENABLED.get();
            FORLORN_LIGHT_EFFECT_ENABLED = ACEConfigHolder.EXEMPLIFIED.FORLORN_LIGHT_EFFECT_ENABLED.get();






        } catch (Exception e) {
            LOGGER.warn("An exception was caused trying to load the config for Alex's Caves Exemplified.");
            e.printStackTrace();
        }
    }

}
