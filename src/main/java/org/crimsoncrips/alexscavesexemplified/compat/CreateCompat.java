package org.crimsoncrips.alexscavesexemplified.compat;

import biomesoplenty.api.block.BOPBlocks;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.simibubi.create.AllFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Unique;

public class CreateCompat {
    public static boolean isHoney(){
       return AllFluids.CHOCOLATE.getBlock().isPresent();
    }
    public static boolean isChocolate(){
       return AllFluids.HONEY.getBlock().isPresent();
    }

    public static void solidifyCreateLiquid(FrostmintSpearEntity frostmintSpear, Level level, BlockPos blockPos){
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    BlockPos icePos = new BlockPos(blockPos.getX() + x, blockPos.getY() + y , blockPos.getZ() + z);
                    BlockState blockState = level.getBlockState(icePos);
                    if (blockState.getFluidState().getFluidType().equals(AllFluids.CHOCOLATE.getType())) {
                        level.setBlock(icePos, ACBlockRegistry.BLOCK_OF_CHOCOLATE.get().defaultBlockState(), 3);
                        level.scheduleTick(icePos, blockState.getBlock(), 2);
                        frostmintSpear.discard();
                        explode(level,frostmintSpear);
                    } else if (blockState.getFluidState().getFluidType().equals(AllFluids.HONEY.getType())) {
                        level.setBlock(icePos, Blocks.HONEY_BLOCK.defaultBlockState(), 3);
                        level.scheduleTick(icePos, blockState.getBlock(), 2);
                        frostmintSpear.discard();
                        explode(level,frostmintSpear);
                    }

                }
            }
        }
    }

    private static void explode(Level level, FrostmintSpearEntity frostmintSpear) {
        FrostmintExplosion explosion = new FrostmintExplosion(level, frostmintSpear.getOwner(), frostmintSpear.getX(), frostmintSpear.getY(0.5), frostmintSpear.getZ(), 2.0F, Explosion.BlockInteraction.KEEP, true);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

}