package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ACEConfigList {



    public final ForgeConfigSpec.BooleanValue GLUTTONY_ENABLED;
    public final ForgeConfigSpec.BooleanValue REGULAR_REFERENCE_ENABLED;

    public final ForgeConfigSpec.BooleanValue REDOABLE_SPELUNKY_ENABLED;

    public final ForgeConfigSpec.BooleanValue GINGERBREAD_AMPUTATION_ENABLED;


    public final ForgeConfigSpec.BooleanValue FORLORN_LIGHT_EFFECT_ENABLED;

    public final ForgeConfigSpec.BooleanValue BURST_OUT_ENABLED;
    public final ForgeConfigSpec.BooleanValue CORRODENT_CONVERSION_ENABLED;






    public final ForgeConfigSpec.BooleanValue GINGERBREAD_HIVEMIND_ENABLED;

    public final ForgeConfigSpec.BooleanValue STABILIZER_COMPATIBILITY_ENABLED;

    public final ForgeConfigSpec.BooleanValue VESPER_CANNIBALIZE_ENABLED;

    public final ForgeConfigSpec.BooleanValue VANILLA_ADAPTIONS_ENABLED;

    public final ForgeConfigSpec.BooleanValue RATATATATATA_ENABLED;

    public final ForgeConfigSpec.BooleanValue PIERCE_DARK_ARROWS_ENABLED;

    public final ForgeConfigSpec.BooleanValue AMPLIFIED_FROSTMINT_EXPLOSION_ENABLED;
    public final ForgeConfigSpec.BooleanValue FROSTMINT_SOLIDIFIED_ENABLED;
    public final ForgeConfigSpec.BooleanValue CANIAC_SENSITIVITY;
    public final ForgeConfigSpec.BooleanValue IRRADIATION_WASHOFF_ENABLED;


    public final ForgeConfigSpec.IntValue SPELUNKY_ATTEMPTS_AMOUNT;
    



    public ACEConfigList(final ForgeConfigSpec.Builder builder) {
        builder.push("General");
         this.REDOABLE_SPELUNKY_ENABLED = buildBoolean(builder, "REDOABLE_SPELUNKY_ENABLED", " ", true, "Whether it gives you back the tablet when exiting the spelunky table");
         this.SPELUNKY_ATTEMPTS_AMOUNT = buildInt(builder, "SPELUNKY_ATTEMPTS_AMOUNT", " ", 5, 1, Integer.MAX_VALUE, "Amount of tries you get for the spelunky table per tablet");
        this.VANILLA_ADAPTIONS_ENABLED = buildBoolean(builder, "VANILLA_ADAPTIONS_ENABLED", " ", true, "Whether vanilla enchantments adapts to weapons (ex.Dreadbow, etc..)");



        builder.push("Candy Cavity");
        this.GLUTTONY_ENABLED = buildBoolean(builder, "GLUTTONY_ENABLED", " ", true, "Whether blocks inside candy cavity, can be eaten by right clicking");
        this.AMPLIFIED_FROSTMINT_EXPLOSION_ENABLED = buildBoolean(builder, "AMPLIFIED_FROSTMINT_EXPLOSION_ENABLED", " ", true, "Item and spear entity explodes as well in Purple Soda");
        this.FROSTMINT_SOLIDIFIED_ENABLED = buildBoolean(builder, "FROSTMINT_SOLIDIFIED_ENABLED", " ", true, "Frostmint Spears causes liquid blocks to freeze at the cost of the spear");

        builder.push("Caniac");
        this.CANIAC_SENSITIVITY = buildBoolean(builder, "CANIAC_SENSITIVITY", " ", true, "Whether Caniac dissolve in water and will actively avoid it");
         builder.pop();
        builder.push("Gingerbread Man");
        this.GINGERBREAD_AMPUTATION_ENABLED = buildBoolean(builder, "GINGERBREAD_AMPUTATION_ENABLED", " ", true, "Whether Gingerbread Men can be amputated with an axe");
        this.GINGERBREAD_HIVEMIND_ENABLED = buildBoolean(builder, "GINGERBREAD_HIVEMIND_ENABLED", " ", true, "Whether Gingerbread Men will attack you if you attack one of them.");
        builder.pop();
        builder.push("Gumbeeper");
        this.REGULAR_REFERENCE_ENABLED = buildBoolean(builder, "REGULAR_REFERENCE_ENABLED", " ", true, "Whether Gumbeepers will attack, Blue jays and Raccoons in Alexs Mobs");
        builder.pop();
        
        

        builder.pop();
        builder.push("Forlorn Hollows");
        this.FORLORN_LIGHT_EFFECT_ENABLED = buildBoolean(builder, "FORLORN_LIGHT_EFFECT_ENABLED", " ", true, "Whether most forlorn mammals are effected when a player holds light");
        this.BURST_OUT_ENABLED = buildBoolean(builder, "BURST_OUT_ENABLED", " ", true, "Whether breaking Forlorn Hollows blocks has a chance to burst out Underzealots or Corrodents");
        this.PIERCE_DARK_ARROWS_ENABLED = buildBoolean(builder, "NON_PIERCE_DARK_ARROWS_ENABLED", " ", true, "Whether darkness arrows pierce or not");
        builder.push("Corrodent");
        this.CORRODENT_CONVERSION_ENABLED = buildBoolean(builder, "CORRODENT_CONVERSION_ENABLED", " ", true, "Whether corrodents can be converted to underzealots through ritual");
        builder.pop();
        builder.push("Vesper");
        this.VESPER_CANNIBALIZE_ENABLED = buildBoolean(builder, "VESPER_CANNIBALIZE_ENABLED", " ", true, "Whether vespers attack bats wondering by");
        builder.pop();
        builder.push("Watcher");
        this.STABILIZER_COMPATIBILITY_ENABLED = buildBoolean(builder, "STABILIZER_COMPATIBILITY_ENABLED", " ", true, "Whether the Stabilizer enchantment from Alexs Mobs Interaction negates possession from the Watcher");
        builder.pop();
        builder.pop();
        builder.push("Toxic Caves");
        this.IRRADIATION_WASHOFF_ENABLED = buildBoolean(builder, "IRRADIATION_WASHOFF_ENABLED", " ", true, "Whether Irradiation wears off faster when in water");


        builder.pop();
        builder.push("Goofy Mode");
        this.RATATATATATA_ENABLED = buildBoolean(builder, "RATATATATATA_ENABLED", " ", false, "Whether Relentless Darkness dreadbow has no cooldown on its firing");


    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
