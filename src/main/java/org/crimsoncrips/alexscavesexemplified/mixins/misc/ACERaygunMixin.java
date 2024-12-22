package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.item.RaygunItem;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;


@Mixin(RaygunItem.class)
public abstract class ACERaygunMixin extends Item {


    public ACERaygunMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void registerGoals(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        int i = getUseDuration(stack) - timeUsing;
        float time = i < 15 ? i / (float) 15 : 1F;
        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(living, Entity::canBeHitByProjectile, 25.0F * time);

        if(stack.getEnchantmentLevel(ACEnchantmentRegistry.X_RAY.get()) <= 0 && ACExemplifiedConfig.RERAYGUNNED_ENABLED){
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

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",ordinal = 0),locals = LocalCapture.CAPTURE_FAILHARD)
    private void burnItems(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci, int i, int realStart, float time, float maxDist, boolean xRay, HitResult realHitResult, HitResult blockOnlyHitResult, Vec3 xRayVec, Vec3 vec3, Vec3 vec31, float deltaX, float deltaY, float deltaZ, boolean gamma, ParticleOptions particleOptions) {
        if (!level.isClientSide && (i - realStart) % 3 == 0 && ACExemplifiedConfig.RERAYGUNNED_ENABLED) {
            AABB hitBox = new AABB(vec31.add((double)-1.0F, (double)-1.0F, (double)-1.0F), vec31.add((double)1.0F, (double)1.0F, (double)1.0F));

            for(ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, hitBox.inflate(1))) {
                if (!itemEntity.isInvulnerable() && !itemEntity.fireImmune() && gamma) {
                    itemEntity.discard();
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
              if (!livingEntity.getType().is(ACTagRegistry.RESISTS_RADIATION) && livingEntity.addEffect(new MobEffectInstance((MobEffect) ACEffectRegistry.IRRADIATED.get(), 800 / (hazmatLevel > 0 ? (hazmatLevel + 1) - radiationLevel : 1),radiationLevel - hazmatLevel + (gamma ? 2 : 0)))) {
                  AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.getId(), living.getId(), gamma && hazmatLevel < 3 ? 4 : 0, 800 / (hazmatLevel > 0 ? (hazmatLevel + 1) - radiationLevel : 1)));
              }
          }


    }



}
