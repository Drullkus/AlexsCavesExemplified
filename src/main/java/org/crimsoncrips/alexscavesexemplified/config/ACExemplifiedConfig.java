package org.crimsoncrips.alexscavesexemplified.config;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ACExemplifiedConfig {
    public static boolean ANTI_SACRIFICE_ENABLED;

    public static boolean REDOABLE_SPELUNKY_ENABLED;
    public static boolean RESISTOR_MAGNETISM;
    public static boolean PURPLE_LEATHERED_ENABLED;
    public static boolean STICKY_SODA_ENABLED;
    public static int EXEMPLIFIED_IRRADIATION_AMOUNT;
    public static boolean GLUTTONY_ENABLED;
    public static boolean GAMMAROACH_FOODING_ENABLED;
    public static boolean REGULAR_REFERENCE_ENABLED;
    public static boolean JELLYBEAN_CHANGES_ENABLED;;
    public static boolean AMPUTATION_ENABLED;
    public static boolean CANIAC_SENSITIVITY_ENABLED;
    public static boolean FLY_TRAP_ENABLED;
    public static boolean HIVE_MIND_ENABLED;
    public static boolean NUCLEAR_PISTONATION_ENABLED;
    public static boolean FORLORN_LIGHT_EFFECT_ENABLED;
    public static boolean VESPER_CANNIBALIZE_ENABLED;
    public static boolean STABILIZER_COMPATIBILITY_ENABLED;
    public static boolean SOLIDIFIED_ENABLED;
    public static boolean CORRODENT_CONVERSION_ENABLED;
    public static boolean VANILLA_ADAPTIONS_ENABLED;
    public static boolean AMPLIFIED_FROSTMINT_ENABLED;
    public static boolean IRRADIATION_WASHOFF_ENABLED;

    public static boolean BURST_OUT_ENABLED;

    public static boolean RATATATATATA_ENABLED;


    public static int SPELUNKY_ATTEMPTS_AMOUNT;

    public static double CHARGED_CAVE_CREEPER_CHANCE;

    public static boolean RABIES_ENABLED;

    public static boolean GEOTHERMAL_EFFECTS_ENABLED;



    public static void bake() {
        try {
            JELLYBEAN_CHANGES_ENABLED = ACEConfigHolder.EXEMPLIFIED.JELLYBEAN_CHANGES_ENABLED.get();
            RESISTOR_MAGNETISM = ACEConfigHolder.EXEMPLIFIED.RESISTOR_MAGNETISM.get();

            CHARGED_CAVE_CREEPER_CHANCE = ACEConfigHolder.EXEMPLIFIED.CHARGED_CAVE_CREEPER_CHANCE.get();
            FLY_TRAP_ENABLED = ACEConfigHolder.EXEMPLIFIED.FLY_TRAP_ENABLED.get();

            GAMMAROACH_FOODING_ENABLED = ACEConfigHolder.EXEMPLIFIED.GAMMAROACH_FOODING_ENABLED.get();
            EXEMPLIFIED_IRRADIATION_AMOUNT = ACEConfigHolder.EXEMPLIFIED.EXEMPLIFIED_IRRADIATION_AMOUNT.get();
            PURPLE_LEATHERED_ENABLED = ACEConfigHolder.EXEMPLIFIED.PURPLE_LEATHERED_ENABLED.get();
            STICKY_SODA_ENABLED = ACEConfigHolder.EXEMPLIFIED.STICKY_SODA_ENABLED.get();

            NUCLEAR_PISTONATION_ENABLED = ACEConfigHolder.EXEMPLIFIED.NUCLEAR_PISTONATION_ENABLED.get();
            VANILLA_ADAPTIONS_ENABLED = ACEConfigHolder.EXEMPLIFIED.VANILLA_ADAPTIONS_ENABLED.get();
            AMPLIFIED_FROSTMINT_ENABLED = ACEConfigHolder.EXEMPLIFIED.AMPLIFIED_FROSTMINT_ENABLED.get();
            SOLIDIFIED_ENABLED = ACEConfigHolder.EXEMPLIFIED.SOLIDIFIED_ENABLED.get();
            CANIAC_SENSITIVITY_ENABLED = ACEConfigHolder.EXEMPLIFIED.CANIAC_SENSITIVITY_ENABLED.get();
            IRRADIATION_WASHOFF_ENABLED = ACEConfigHolder.EXEMPLIFIED.IRRADIATION_WASHOFF_ENABLED.get();

            BURST_OUT_ENABLED = ACEConfigHolder.EXEMPLIFIED.BURST_OUT_ENABLED.get();
            STABILIZER_COMPATIBILITY_ENABLED = ACEConfigHolder.EXEMPLIFIED.STABILIZER_COMPATIBILITY_ENABLED.get();
            VESPER_CANNIBALIZE_ENABLED = ACEConfigHolder.EXEMPLIFIED.VESPER_CANNIBALIZE_ENABLED.get();
            CORRODENT_CONVERSION_ENABLED = ACEConfigHolder.EXEMPLIFIED.CORRODENT_CONVERSION_ENABLED.get();
            RATATATATATA_ENABLED = ACEConfigHolder.EXEMPLIFIED.RATATATATATA_ENABLED.get();
            RABIES_ENABLED = ACEConfigHolder.EXEMPLIFIED.RABIES_ENABLED.get();

            GEOTHERMAL_EFFECTS_ENABLED = ACEConfigHolder.EXEMPLIFIED.GEOTHERMAL_EFFECTS_ENABLED.get();




            SPELUNKY_ATTEMPTS_AMOUNT = ACEConfigHolder.EXEMPLIFIED.SPELUNKY_ATTEMPTS_AMOUNT.get();
            REDOABLE_SPELUNKY_ENABLED = ACEConfigHolder.EXEMPLIFIED.REDOABLE_SPELUNKY_ENABLED.get();
            GLUTTONY_ENABLED = ACEConfigHolder.EXEMPLIFIED.GLUTTONY_ENABLED.get();
            REGULAR_REFERENCE_ENABLED = ACEConfigHolder.EXEMPLIFIED.REGULAR_REFERENCE_ENABLED.get();
            AMPUTATION_ENABLED = ACEConfigHolder.EXEMPLIFIED.AMPUTATION_ENABLED.get();
            HIVE_MIND_ENABLED = ACEConfigHolder.EXEMPLIFIED.HIVE_MIND_ENABLED.get();
            FORLORN_LIGHT_EFFECT_ENABLED = ACEConfigHolder.EXEMPLIFIED.FORLORN_LIGHT_EFFECT_ENABLED.get();
            ANTI_SACRIFICE_ENABLED = ACEConfigHolder.EXEMPLIFIED.ANTI_SACRIFICE_ENABLED.get();






        } catch (Exception e) {
            LOGGER.warn("An exception was caused trying to load the config for Alex's Caves Exemplified.");
            e.printStackTrace();
        }
    }

}
