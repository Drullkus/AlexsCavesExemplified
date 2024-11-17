package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ACEConfigList {



    public final ForgeConfigSpec.BooleanValue GLUTTONY_ENABLED;
    public final ForgeConfigSpec.BooleanValue REGULAR_REFERENCE_ENABLED;

    public final ForgeConfigSpec.BooleanValue REDOABLE_SPELUNKY_ENABLED;

    public final ForgeConfigSpec.BooleanValue GINGERBREAD_AMPUTATION_ENABLED;

    public final ForgeConfigSpec.BooleanValue GINGERBREAD_HIVEMIND_ENABLED;


    public final ForgeConfigSpec.IntValue SPELUNKY_ATTEMPTS_AMOUNT;
    



    public ACEConfigList(final ForgeConfigSpec.Builder builder) {
        builder.push("General");
         this.REDOABLE_SPELUNKY_ENABLED = buildBoolean(builder, "REDOABLE_SPELUNKY_ENABLED", " ", true, "Whether it gives you back the tablet when exiting the spelunky table");
         this.SPELUNKY_ATTEMPTS_AMOUNT = buildInt(builder, "SPELUNKY_ATTEMPTS_AMOUNT", " ", 5, 1, Integer.MAX_VALUE, "Amount of tries you get for the spelunky table per tablet");
        builder.push("Candy Cavity");
        this.GLUTTONY_ENABLED = buildBoolean(builder, "GLUTTONY_ENABLED", " ", true, "Whether blocks inside candy cavity, can be eaten by right clicking");

        builder.push("Gingerbread Man");
        this.GINGERBREAD_AMPUTATION_ENABLED = buildBoolean(builder, "GINGERBREAD_AMPUTATION_ENABLED", " ", true, "Whether Gingerbread Men can be amputated with an axe");
        this.GINGERBREAD_HIVEMIND_ENABLED = buildBoolean(builder, "GINGERBREAD_HIVEMIND_ENABLED", " ", true, "Whether Gingerbread Men will attack you if you attack one of them.");

        builder.pop();
        builder.push("Gumbeeper");
        this.REGULAR_REFERENCE_ENABLED = buildBoolean(builder, "REGULAR_REFERENCE_ENABLED", " ", true, "Whether Gumbeepers will attack, Blue jays and Raccoons in Alexs Mobs");

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
