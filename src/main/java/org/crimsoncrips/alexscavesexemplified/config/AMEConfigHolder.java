package org.crimsoncrips.alexscavesexemplified.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class AMEConfigHolder {

    public static final AMEConfigList EXEMPLIFIED;

    public static final ForgeConfigSpec EXEMPLIFIED_SPEC;

    static {
        {
            final Pair<AMEConfigList, ForgeConfigSpec> interact = new ForgeConfigSpec.Builder().configure(AMEConfigList::new);
            EXEMPLIFIED = interact.getLeft();
            EXEMPLIFIED_SPEC = interact.getRight();
        }
    }
}