package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetClosePlayers;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetItemGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTargetUntamedGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.TargetsDroppedItems;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACETremorTempt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;


@Mixin(TremorsaurusEntity.class)
public abstract class ACETremorsaurus extends DinosaurEntity implements TargetsDroppedItems {

    private boolean sniffed = false;


    protected ACETremorsaurus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void registerGoals(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        if (ACExemplifiedConfig.DINOSAUR_EGG_ANGER_ENABLED){
            tremorsaurus.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(tremorsaurus, LivingEntity.class, 100, true, false,livingEntity -> {
                return livingEntity.isHolding(Ingredient.of(ACBlockRegistry.TREMORSAURUS_EGG.get()));
            }));
        }

        if (ACExemplifiedConfig.TREMOR_V_TREMOR_ENABLED){
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        }

        if (ACExemplifiedConfig.SEETHED_TAMING_ENABLED) {
            this.goalSelector.addGoal(2, new ACETremorTempt(tremorsaurus, 1.1, Ingredient.of(new ItemLike[]{(ItemLike) ACBlockRegistry.COOKED_DINOSAUR_CHOP.get(), ACBlockRegistry.DINOSAUR_CHOP.get()}), false){
                @Override
                public boolean canUse() {
                    return super.canUse() && isSeethed();
                }
            });


            this.targetSelector.addGoal(2, new MobTargetClosePlayers(tremorsaurus, 50, 8.0F) {
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !isSeethed();
                }
            });

            this.targetSelector.addGoal(3, new MobTargetUntamedGoal(this, GrottoceratopsEntity.class, 100, true, false, (Predicate)null){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !isSeethed();
                }
            });
            this.targetSelector.addGoal(4, new MobTargetUntamedGoal(this, SubterranodonEntity.class, 50, true, false, (Predicate)null){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !isSeethed();
                }
            });
            this.targetSelector.addGoal(5, new MobTargetUntamedGoal(this, RelicheirusEntity.class, 250, true, false, (Predicate)null){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !isSeethed();
                }
            });
        }


        if (ACExemplifiedConfig.SCAVENGING_ENABLED){
            tremorsaurus.goalSelector.addGoal(3, new MoveToBlockGoal(tremorsaurus, 1, 30,3) {
                @Override
                public void tick() {
                    super.tick();
                    tremorsaurus.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
                    for (VallumraptorEntity vallumraptor : tremorsaurus.level().getEntitiesOfClass(VallumraptorEntity.class, new AABB(blockPos.offset(-3, -3, -3), blockPos.offset(3, 3, 3)))) {
                        if (tremorsaurus.distanceToSqr(this.mob.position()) < 10){
                            tremorsaurus.tryRoar();
                        }
                    }
                    if (this.isReachedTarget()) {
                        tremorsaurus.getNavigation().stop();
                        tremorsaurus.setInSittingPose(true);
                        if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose() && !sniffed) {
                            tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_SNIFF);
                        }

                        if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_SNIFF && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                            if (isValidTarget(tremorsaurus.level(), blockPos)){
                                sniffed = true;
                            } else this.stop();
                        }

                        if (sniffed && tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose()){
                            tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
                        }

                        if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15){
                            if (isValidTarget(tremorsaurus.level(), blockPos)){
                                tremorsaurus.heal(4);
                                tremorsaurus.level().destroyBlock(blockPos, false);
                                tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                                if (ACExemplifiedConfig.SEETHED_TAMING_ENABLED && level().getRandom().nextDouble() < 0.2) {
                                    setSeethed(true);
                                }
                            }
                            this.stop();
                        }
                    }

                }

                @Override
                protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
                    BlockState blockState = worldIn.getBlockState(pos);
                    return blockState.is(ACBlockRegistry.DINOSAUR_CHOP.get()) || blockState.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get());
                }

                public void stop() {
                    super.stop();
                    sniffed = false;
                    tremorsaurus.setInSittingPose(false);
                    this.blockPos = BlockPos.ZERO;
                }

                public double acceptedDistance() {
                    return 4F;
                }

                protected int nextStartTick(PathfinderMob mob) {
                    return reducedTickDelay(200 + tremorsaurus.getRandom().nextInt(150));
                }
            });

            tremorsaurus.targetSelector.addGoal(2, new MobTargetItemGoal(tremorsaurus,true,true,200 + tremorsaurus.getRandom().nextInt(150),30){
                @Override
                public void tick() {
                    if (this.targetEntity != null && this.targetEntity.isAlive()) {
                        this.moveTo();
                    } else {
                        this.stop();
                        this.mob.getNavigation().stop();
                    }

                    if (this.targetEntity != null && this.mob.hasLineOfSight(this.targetEntity) && (double)this.mob.getBbWidth() > 2.0 && this.mob.onGround()) {
                        this.mob.getMoveControl().setWantedPosition(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1.0);
                    }

                    if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < 4) {
                        tremorsaurus.getNavigation().stop();
                        tremorsaurus.setInSittingPose(true);
                        if (tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose() && !sniffed) {
                            tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_SNIFF);
                        }
                        if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_SNIFF && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15) {
                            if (this.hunter.canTargetItem(targetEntity.getItem())){
                                sniffed = true;
                            } else this.stop();
                        }
                        if (sniffed && tremorsaurus.getAnimation() == TremorsaurusEntity.NO_ANIMATION && tremorsaurus.isInSittingPose()){
                            tremorsaurus.setAnimation(TremorsaurusEntity.ANIMATION_BITE);
                        }
                        if (tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && tremorsaurus.getAnimationTick() >= 10 && tremorsaurus.getAnimationTick() <= 15){
                            if (this.hunter.canTargetItem(targetEntity.getItem())){
                                tremorsaurus.heal(4);
                                tremorsaurus.playSound(ACSoundRegistry.TREMORSAURUS_BITE.get(), 1F, 1F);
                                targetEntity.kill();
                                if (ACExemplifiedConfig.SEETHED_TAMING_ENABLED && level().getRandom().nextDouble() < 0.1) {
                                    setSeethed(true);
                                }
                            }
                            this.stop();
                        }
                    }
                }

                public void stop() {
                    super.stop();
                    sniffed = false;
                    tremorsaurus.setInSittingPose(false);
                }
            });

        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorsaurusEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void stomp(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        for (LivingEntity entity : tremorsaurus.level().getEntitiesOfClass(LivingEntity.class, tremorsaurus.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != tremorsaurus && entity.getBbHeight() <= 0.8F) {
                entity.hurt(tremorsaurus.damageSources().mobAttack(tremorsaurus), 3.0F);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        if (ACExemplifiedConfig.SEETHED_TAMING_ENABLED){
            if (isSeethed()){
                if (tremorsaurus.level().isClientSide){
                    tremorsaurus.level().addParticle(ACParticleRegistry.HAPPINESS.get(), tremorsaurus.getX(), tremorsaurus.getEyeY() - (tremorsaurus.isOrderedToSit() ? 2 : 0), tremorsaurus.getZ(), ((double) this.random.nextFloat() - (double) 0.5F) * 0.2, ((double) this.random.nextFloat() - (double) 0.5F) * 0.2, ((double) this.random.nextFloat() - (double) 0.5F) * 0.2);
                }
                if (tremorsaurus.getPersistentData().getInt("LoseSeethe") < 2400){
                    tremorsaurus.getPersistentData().putInt("LoseSeethe", tremorsaurus.getPersistentData().getInt("LoseSeethe") + 1);
                } else {
                    setSeethed(false);
                    tremorsaurus.getPersistentData().putInt("LoseSeethe", 0);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof LivingEntity){
            setSeethed(false);
        }
        return super.hurt(pSource, pAmount);
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 6))
    private boolean tempt(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.SEETHED_TAMING_ENABLED;
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 10))
    private boolean hurtBy(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.TREMOR_V_TREMOR_ENABLED;
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 11))
    private boolean targetPlayer(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.SEETHED_TAMING_ENABLED;
    }

    @Inject(method = "registerGoals", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/ai/MobTargetUntamedGoal;<init>(Lnet/minecraft/world/entity/TamableAnimal;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V",ordinal = 0), cancellable = true)
    private void targetDinos(CallbackInfo ci) {
        if (ACExemplifiedConfig.SEETHED_TAMING_ENABLED) ci.cancel();

    }

    public boolean canTargetItem(ItemStack itemStack) {
        boolean isEdible = false;
        if (itemStack.getItem().isEdible()) {
            if (itemStack.getItem().getFoodProperties().isMeat()) {
                isEdible = true;
            }
        }
        return isEdible;
    }



    static {
        SEETHED = SynchedEntityData.defineId(TremorsaurusEntity.class, EntityDataSerializers.BOOLEAN);
    }

    private static final EntityDataAccessor<Boolean> SEETHED;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynched(CallbackInfo ci){
        this.entityData.define(SEETHED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditional(CompoundTag compound, CallbackInfo ci){
        compound.putBoolean("Seethed", this.isSeethed());
    }
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditional(CompoundTag compound, CallbackInfo ci){
        this.setSeethed(compound.getBoolean("Seethed"));
    }

    public boolean isSeethed() {
        return this.entityData.get(SEETHED);
    }

    public void setSeethed(boolean stunned) {
        this.entityData.set(SEETHED, stunned);
    }


}
