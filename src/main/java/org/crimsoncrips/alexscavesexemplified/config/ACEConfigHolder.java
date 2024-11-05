package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ACEConfigHolder {

    public static final ACEConfigList EXEMPLIFIED;

    public static final ForgeConfigSpec EXEMPLIFIED_SPEC;

    static {
        {
            final Pair<ACEConfigList, ForgeConfigSpec> interact = new ForgeConfigSpec.Builder().configure(ACEConfigList::new);
            EXEMPLIFIED = interact.getLeft();
            EXEMPLIFIED_SPEC = interact.getRight();
        }
    }
}