package org.crimsoncrips.alexscavesexemplified.server.blockentity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.EnigmaticEngineBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.blockentity.*;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEBlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AlexsCavesExemplified.MODID);

    public static final RegistryObject<BlockEntityType<TntBlockEntity>> TNT_BLOCK_ENTITY = DEF_REG.register("tnt_entity", () -> BlockEntityType.Builder.of(TntBlockEntity::new, Blocks.TNT).build(null));

}
