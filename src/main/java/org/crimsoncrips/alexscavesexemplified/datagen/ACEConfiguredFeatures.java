package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEConfiguredFeatures {
    static final RegistrySetBuilder DATA_BUILDER = new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, ACEConfiguredFeatures::generateFeatureConfigurations);


    public static final ResourceKey<ConfiguredFeature<?, ?>> PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_bonemeal"));


    static void generateFeatureConfigurations(BootstapContext<ConfiguredFeature<?, ?>> context) {

        context.register(PRIMORDIAL_BONEMEAL, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.GRASS.defaultBlockState()))));
    }
}
