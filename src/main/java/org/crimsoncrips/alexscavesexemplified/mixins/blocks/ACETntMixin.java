package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.server.blockentity.TntBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(TntBlock.class)
public abstract class ACETntMixin extends Block {


    public ACETntMixin(Properties pProperties) {
        super(pProperties);
    }


}
