package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
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
        add("test", new ACEAddItemModifier(new LootItemCondition[]{
//                (LootItemCondition) LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.JUNGLE)),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(1F).build()}, Items.ACACIA_LEAVES));
    }
}
