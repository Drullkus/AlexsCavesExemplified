package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.block.BloodFluid;
import biomesoplenty.init.ModTags;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.FrostmintSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.simibubi.create.AllFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;


@Mixin(FrostmintSpearEntity.class)
public abstract class ACEFrostmintSpear extends AbstractArrow {


    @Shadow private boolean exploded;

    @Shadow @Nullable public abstract Entity getOwner();

    protected ACEFrostmintSpear(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        Level level = this.level();
        if (this.isInFluidType()){
            if (ACExemplifiedConfig.AMPLIFIED_FROSTMINT_ENABLED && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !level.isClientSide) {
                FrostmintExplosion explosion = new FrostmintExplosion(level, this, this.getX() + 0.5F, this.getY() + 0.5F, this.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                explosion.explode();
                explosion.finalizeExplosion(true);
                if (this.getOwner() instanceof Player) {
                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(this.getOwner());
                }
                this.discard();
            }
        }

        if (ACExemplifiedConfig.RADIANT_WRATH_ENABLED && this.tickCount > 200){
            this.discard();
        }
    }

    @Inject(method = "onHit", at = @At("HEAD"))
    private void hit(HitResult hitResult, CallbackInfo ci) {

        Vec3 hitLocation = hitResult.getLocation();

        BlockPos blockPos = new BlockPos((int) hitLocation.x, (int) hitLocation.y - 1, (int) hitLocation.z);

        Level level = this.level();


        if (!this.exploded && ACExemplifiedConfig.SOLIDIFIED_ENABLED) {
            solidifyBlock(Blocks.WATER,Blocks.ICE,level,blockPos);
            solidifyBlock(Blocks.LAVA,Blocks.BASALT,level,blockPos);
            solidifyBlock(ACBlockRegistry.PURPLE_SODA.get(), ACBlockRegistry.SUGAR_GLASS.get(), level,blockPos);
            if (ModList.get().isLoaded("create")) {
                if (AllFluids.CHOCOLATE.getBlock().isPresent()){
                    solidifyBlock(AllFluids.CHOCOLATE.getBlock().get(), ACBlockRegistry.BLOCK_OF_CHOCOLATE.get(), level,blockPos);
                }
                if (AllFluids.HONEY.getBlock().isPresent()){
                    solidifyBlock(AllFluids.HONEY.getBlock().get(), Blocks.HONEY_BLOCK, level,blockPos);
                }
            }
            if (ModList.get().isLoaded("biomesoplenty")) {
                solidifyBlock(BOPBlocks.BLOOD, BOPBlocks.FLESH, level, blockPos);
            }
        }




    }

    @Unique
    public void solidifyBlock(Block block, Block output, Level level, BlockPos blockPos){
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos icePos = new BlockPos(blockPos.getX() + x - 2, blockPos.getY() + y , blockPos.getZ() + z - 2);
                    BlockState blockState = level.getBlockState(icePos);
                    if (blockState.is(block)) {
                        if(blockState.getFluidState().isSource()) {
                            level.setBlock(icePos, output.defaultBlockState(), 2);
                            level.scheduleTick(icePos, blockState.getBlock(), 2);
                            this.discard();
                            explode();
                        } else if (random.nextDouble() < 0.3) {
                            level.setBlock(icePos, output.defaultBlockState(), 2);
                            level.scheduleTick(icePos, blockState.getBlock(), 2);
                            this.discard();
                            explode();
                        }
                    }
                }
            }
        }
    }

    private void explode() {
        FrostmintExplosion explosion = new FrostmintExplosion(level(), this.getOwner(), this.getX(), this.getY(0.5), this.getZ(), 2.0F, Explosion.BlockInteraction.KEEP, true);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

}
