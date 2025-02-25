package org.crimsoncrips.alexscavesexemplified.server.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEFeatureRegistry {
	public static final DeferredRegister<Feature<?>> DEF_REG = DeferredRegister.create(Registries.FEATURE, AlexsCavesExemplified.MODID);

	public static final RegistryObject<FlaggedSimpleBlockFeature> CRIMS_BETTER_SIMPLE_BLOCK = DEF_REG.register("better_simple_block", () -> new FlaggedSimpleBlockFeature(SimpleBlockConfiguration.CODEC));
}
