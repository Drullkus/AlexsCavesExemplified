package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AMEConfigList {



    public final ForgeConfigSpec.BooleanValue TUSKLIN_STRUCK_ENABLED;
    



    public AMEConfigList(final ForgeConfigSpec.Builder builder) {
        builder.push("General");
         this.TUSKLIN_STRUCK_ENABLED = buildBoolean(builder, "TUSKLIN_STRUCK_ENABLED", " ", true, "Tusklins like their brethren flee from warped fungus");

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
