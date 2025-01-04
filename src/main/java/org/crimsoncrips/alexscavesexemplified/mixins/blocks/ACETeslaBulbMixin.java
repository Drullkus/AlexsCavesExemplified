package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import com.github.alexmodguy.alexscaves.server.block.GalenaSpireBlock;
import com.github.alexmodguy.alexscaves.server.block.TeslaBulbBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.ACESoundRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.github.alexmodguy.alexscaves.server.block.RebarBlock.CONNECT_Y;


@Mixin(TeslaBulbBlock.class)
public abstract class ACETeslaBulbMixin extends BaseEntityBlock {


    protected ACETeslaBulbMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelAccessor, BlockPos blockPos) {
        BlockState above = levelAccessor.getBlockState(blockPos.above());
        BlockState below = levelAccessor.getBlockState(blockPos.below());

        if (ACExemplifiedConfig.SHOCKING_THERAPY_ENABLED && (above.is(ACBlockRegistry.METAL_REBAR.get()) || below.is(ACBlockRegistry.METAL_REBAR.get()) )){
            return state.getValue(TeslaBulbBlock.DOWN) ? above.getValue(CONNECT_Y) : below.getValue(CONNECT_Y);
        } else {
            if ((Boolean)state.getValue(TeslaBulbBlock.DOWN)) {
                return above.isFaceSturdy(levelAccessor, blockPos.above(), Direction.UP) || GalenaSpireBlock.isGalenaSpireConnectable(above, true);
            } else {
                return below.isFaceSturdy(levelAccessor, blockPos.below(), Direction.DOWN) || GalenaSpireBlock.isGalenaSpireConnectable(below, false);
            }
        }
    }



    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/TeslaBulbBlockEntity;explode()V"))
    private void alexsCavesExemplified$attack(BlockState blockState, Level level, BlockPos blockPos, Player player, CallbackInfo ci) {
        if (ACExemplifiedConfig.TESLA_COILED_ENABLED) {
            level.playLocalSound(blockPos, ACESoundRegistry.TESLA_EXPLODING.get(), SoundSource.AMBIENT, 2, 1, false);
        }
    }

    @Inject(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/TeslaBulbBlockEntity;explode()V"))
    private void alexsCavesExemplified$projectileHit(Level level, BlockState blockState, BlockHitResult hitResult, Projectile projectile, CallbackInfo ci) {
        if (ACExemplifiedConfig.TESLA_COILED_ENABLED) {
            level.playLocalSound(Objects.requireNonNull(level.getBlockEntity(hitResult.getBlockPos())).getBlockPos(), ACESoundRegistry.TESLA_EXPLODING.get(), SoundSource.AMBIENT, 2, 1, false);
        }
    }
}
