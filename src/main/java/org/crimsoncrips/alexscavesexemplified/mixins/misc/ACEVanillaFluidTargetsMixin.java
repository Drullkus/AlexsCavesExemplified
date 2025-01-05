package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.simibubi.create.content.fluids.pipes.VanillaFluidTargets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.crimsoncrips.alexscavesexemplified.server.blocks.ACEBlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(VanillaFluidTargets.class)
public abstract class ACEVanillaFluidTargetsMixin{



    @Inject(method = "canProvideFluidWithoutCapability", at = @At("HEAD"), cancellable = true, remap = false)
    private static void alexsCavesExemplified$removeFluidFromSpace(BlockState state, CallbackInfoReturnable<Boolean> cir) {


        if (state.is(ACEBlockRegistry.ACID_CAULDRON.get())) {
            cir.setReturnValue(ACEBlockRegistry.ACID_CAULDRON.getHolder().isPresent());
        } else if (state.is(ACEBlockRegistry.PURPLE_SODA_CAULDRON.get())) {
            cir.setReturnValue(ACEBlockRegistry.PURPLE_SODA_CAULDRON.getHolder().isPresent());
        }
    }


    @Inject(method = "drainBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void alexsCavesExemplified$removeFluidFromSpace(Level level, BlockPos pos, BlockState state, boolean simulate, CallbackInfoReturnable<FluidStack> cir) {
        if (state.is(ACEBlockRegistry.ACID_CAULDRON.get())) {
            if (!simulate) {
                level.setBlock(pos, ACEBlockRegistry.METAL_CAULDRON.get().defaultBlockState(), 3);
            }
            cir.setReturnValue(new FluidStack(ACFluidRegistry.ACID_FLUID_SOURCE.get(), 1000));
        } else if (state.is(ACEBlockRegistry.PURPLE_SODA_CAULDRON.get())) {
            if (!simulate) {
                level.setBlock(pos, ACEBlockRegistry.METAL_CAULDRON.get().defaultBlockState(), 3);
            }
            cir.setReturnValue(new FluidStack(ACFluidRegistry.PURPLE_SODA_FLUID_SOURCE.get(), 1000));
        }
    }



}
