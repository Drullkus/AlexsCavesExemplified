package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(DinosaurChopBlock.class)
public abstract class ACEDinosaurChops extends Block {

    @Shadow @javax.annotation.Nullable public abstract BlockState getStateForPlacement(BlockPlaceContext context);

    public ACEDinosaurChops(Properties pProperties) {
        super(pProperties);
    }



}
