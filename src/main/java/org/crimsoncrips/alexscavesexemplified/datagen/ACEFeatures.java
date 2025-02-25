package org.crimsoncrips.alexscavesexemplified.datagen;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.feature.ACEFeatureRegistry;

import java.util.List;

public class ACEFeatures {
    //Props to Drull and TF for assistance//
    static final RegistrySetBuilder DATA_BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ACEFeatures::generateFeatureConfigurations)
            .add(Registries.PLACED_FEATURE, ACEFeatures::generateFeaturePlacements);


    public static final ResourceKey<ConfiguredFeature<?, ?>> PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "primordial_bonemeal"));
    public static final ResourceKey<PlacedFeature> PLACED_PRIMORDIAL_BONEMEAL = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(AlexsCavesExemplified.MODID, "placed_primordial_bonemeal"));


    static void generateFeatureConfigurations(BootstapContext<ConfiguredFeature<?, ?>> context) {
        SimpleWeightedRandomList.Builder<BlockState> bonemealPlacements = SimpleWeightedRandomList.<BlockState>builder()
                .add(Blocks.GRASS.defaultBlockState(), 5)
                .add(ACBlockRegistry.FLYTRAP.get().defaultBlockState(), 1);

        SimpleBlockConfiguration primordialBonemealConfig = new SimpleBlockConfiguration(new WeightedStateProvider(bonemealPlacements));
        context.register(PRIMORDIAL_BONEMEAL, new ConfiguredFeature<>(ACEFeatureRegistry.CRIMS_BETTER_SIMPLE_BLOCK.get(), primordialBonemealConfig));
    }

    static void generateFeaturePlacements(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> featureConfigLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(PLACED_PRIMORDIAL_BONEMEAL, new PlacedFeature(featureConfigLookup.getOrThrow(PRIMORDIAL_BONEMEAL), List.of(PlacementUtils.isEmpty())));
    }

}
