package org.crimsoncrips.alexscavesexemplified.server.blocks;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.ACECauldron;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.AcidCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.MetalCauldronBlock;
import org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron.PurpleCauldronBlock;

import java.util.function.Supplier;

public class ACEBlockRegistry {

   public static final DeferredRegister<Block> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, AlexsCavesExemplified.MODID);

    public static final RegistryObject<Block> METAL_CAULDRON = registerBlock("metal_cauldron", () -> new MetalCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()));

    public static final RegistryObject<Block> ACID_CAULDRON = registerBlock("acid_cauldron", () -> new AcidCauldronBlock(BlockBehaviour.Properties.copy(METAL_CAULDRON.get()).lightLevel((p_50870_) -> 15)));

    public static final RegistryObject<Block> PURPLE_CAULDRON = registerBlock("purple_cauldron", () -> new PurpleCauldronBlock(BlockBehaviour.Properties.copy(METAL_CAULDRON.get())));


    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = DEF_REG.register(name, block);
        return blockObj;
    }

}
