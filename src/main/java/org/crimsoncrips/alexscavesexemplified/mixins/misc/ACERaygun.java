package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.item.RaygunItem;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACECreatureAITargetItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(RaygunItem.class)
public abstract class ACERaygun extends Item {


    public ACERaygun(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void registerGoals(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        int i = getUseDuration(stack) - timeUsing;
        float time = i < 15 ? i / (float) 15 : 1F;
        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(living, Entity::canBeHitByProjectile, 25.0F * time);

        if(stack.getEnchantmentLevel(ACEnchantmentRegistry.X_RAY.get()) <= 0){
            if (realHitResult instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos();
                if (stack.getEnchantmentLevel(ACEnchantmentRegistry.GAMMA_RAY.get()) > 0) {

                    if (level.random.nextDouble() < 0.2){
                        if (!level.getBlockState(pos).is(BlockTags.WITHER_IMMUNE)) {
                            level.destroyBlock(pos, false, living);
                        }

                        setAdjascentBlock(pos.north(),level);
                        setAdjascentBlock(pos.east(),level);
                        setAdjascentBlock(pos.south(),level);
                        setAdjascentBlock(pos.west(),level);
                        setAdjascentBlock(pos.above(),level);
                        setAdjascentBlock(pos.below(),level);

                    }
                }
                else {
                    if (!level.getBlockState(pos).is(Blocks.AIR) && !level.getBlockState(pos.above()).is(BlockTags.WITHER_IMMUNE))
                        level.setBlock(pos.above(), Blocks.FIRE.defaultBlockState(),2);
                }
            }
        }
    }

    @Unique
    public void setAdjascentBlock(BlockPos pos, Level level){
        Block[] block = {Blocks.BASALT,Blocks.NETHERRACK,Blocks.MAGMA_BLOCK, ACBlockRegistry.PRIMAL_MAGMA.get()};

        if (!level.getBlockState(pos).is(Blocks.AIR) && level.random.nextDouble() < 0.2 && !level.getBlockState(pos).is(BlockTags.WITHER_IMMUNE))
            level.setBlockAndUpdate(pos, block[level.random.nextInt(0, 4)].defaultBlockState());
    }

}
