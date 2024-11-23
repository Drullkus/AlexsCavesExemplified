package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.loot.ACEAddItemModifier;

public class ACELootModifierProvider extends GlobalLootModifierProvider {

    public ACELootModifierProvider(PackOutput output) {
        super(output, AlexsCavesExemplified.MODID);
    }


    @Override
    protected void start() {
    }
}
