package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.item.RaygunItem;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.message.UpdateItemTagMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACECreatureAITargetItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;


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

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getType()Lnet/minecraft/world/entity/EntityType;"),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void hazmatReduce(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci, int i, int realStart, float time, float maxDist, boolean xRay, HitResult realHitResult, HitResult blockOnlyHitResult, Vec3 xRayVec, Vec3 vec3, Vec3 vec31, float deltaX, float deltaY, float deltaZ, boolean gamma, ParticleOptions particleOptions, Direction blastHitDirection, Vec3 blastHitPos, AABB hitBox, int radiationLevel, Iterator var24, Entity entity, boolean flag, LivingEntity livingEntity) {
          if (ACExemplifiedConfig.HAZMAT_AMPLIFIED_ENABLED){
              ci.cancel();
              int hazmatLevel = HazmatArmorItem.getWornAmount(livingEntity);
              if (!livingEntity.getType().is(ACTagRegistry.RESISTS_RADIATION) && livingEntity.addEffect(new MobEffectInstance((MobEffect) ACEffectRegistry.IRRADIATED.get(), 800 / (hazmatLevel > 0 ? hazmatLevel : 1),radiationLevel - hazmatLevel))) {
                  AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.getId(), living.getId(), gamma && hazmatLevel < 3 ? 4 : 0, 800 / (hazmatLevel > 0 ? hazmatLevel : 1)));
              }
          }


    }



}
