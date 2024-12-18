package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACECreatureAITargetItems;
import org.crimsoncrips.alexscavesexemplified.goals.ACEPickupDroppedBarrels;
import org.crimsoncrips.alexscavesexemplified.misc.ACETargetsDroppedItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(TremorsaurusEntity.class)
public abstract class ACETremorsaurus extends DinosaurEntity implements ACETargetsDroppedItems {

    private boolean sniffed = false;

    protected ACETremorsaurus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"),remap = false)
    private void registerGoals(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        if (ACExemplifiedConfig.DINOSAUR_EGG_ANGER_ENABLED){
            tremorsaurus.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(tremorsaurus, LivingEntity.class, 100, true, false,livingEntity -> {
                return livingEntity.isHolding(Ingredient.of(ACBlockRegistry.TREMORSAURUS_EGG.get()));
            }));
        }

        if (ACExemplifiedConfig.SCAVENGING_ENABLED){
            tremorsaurus.goalSelector.addGoal(3, new MoveToBlockGoal(tremorsaurus, 1, 30,4) {
                @Override
                public void tick() {
                    super.tick();
                    tremorsaurus.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
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
                    return reducedTickDelay(200 + tremorsaurus.getRandom().nextInt(50));
                }
            });

            tremorsaurus.targetSelector.addGoal(2, new ACECreatureAITargetItems<>(tremorsaurus,true,true,10,30){
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

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorsaurusEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"),remap = false)
    private void onStep(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        for (LivingEntity entity : tremorsaurus.level().getEntitiesOfClass(LivingEntity.class, tremorsaurus.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != tremorsaurus && entity.getBbHeight() <= 0.5F) {
                entity.hurt(tremorsaurus.damageSources().mobAttack(tremorsaurus), 4.0F);
            }
        }
    }

    @Override
    public boolean canTargetItem(ItemStack itemStack) {
        boolean isEdible = false;
        if (itemStack.getItem().isEdible()){
            if (itemStack.getItem().getFoodProperties().isMeat()){
                isEdible = true;
            }

        }
        return itemStack.is(ACBlockRegistry.DINOSAUR_CHOP.get().asItem()) || itemStack.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get().asItem()) || isEdible;

    }


}
