package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock;
import com.github.alexmodguy.alexscaves.server.block.ThinBoneBlock;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

import static com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock.BITES;


@Mixin(VallumraptorEntity.class)
public abstract class ACEVallumraptor extends DinosaurEntity {

    protected ACEVallumraptor(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"),remap = false)
    private void registerGoals(CallbackInfo ci) {
        VallumraptorEntity vallumraptor = (VallumraptorEntity)(Object)this;
        if (ACExemplifiedConfig.DINOSAUR_EGG_ANGER_ENABLED){
            vallumraptor.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(vallumraptor, LivingEntity.class, 100, true, false,livingEntity -> {
                return livingEntity.isHolding(Ingredient.of(ACBlockRegistry.VALLUMRAPTOR_EGG.get()));
            }){
                @Override
                public boolean canContinueToUse() {
                    return super.canContinueToUse() && !vallumraptor.isTame();
                }
            });
        }

        if (ACExemplifiedConfig.SCAVENGING_ENABLED){
            vallumraptor.goalSelector.addGoal(8, new MoveToBlockGoal(vallumraptor, 1.4, 20) {
                @Override
                public void tick() {
                    super.tick();

                    for (TremorsaurusEntity tremorsaurus : vallumraptor.level().getEntitiesOfClass(TremorsaurusEntity.class, new AABB(blockPos.offset(-5, -5, -5), blockPos.offset(5, 5, 5)))) {
                       stop();
                    }
                    vallumraptor.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
                    if (this.isReachedTarget()) {
                        vallumraptor.getNavigation().stop();

                        if (vallumraptor.getAnimation() == VallumraptorEntity.NO_ANIMATION) {
                            vallumraptor.setAnimation(VallumraptorEntity.ANIMATION_MELEE_BITE);
                        }

                        if (vallumraptor.getAnimation() == VallumraptorEntity.ANIMATION_MELEE_BITE && vallumraptor.getAnimationTick() >= 10 && vallumraptor.getAnimationTick() <= 15) {
                            if (isValidTarget(vallumraptor.level(), blockPos)) {
                                BlockState blockState = vallumraptor.level().getBlockState(blockPos);
                                vallumraptor.heal(1);
                                int i = blockState.getValue(BITES);
                                vallumraptor.level().destroyBlock(blockPos, false);
                                if (i < 3) {
                                    vallumraptor.level().setBlock(blockPos, blockState.setValue(BITES, i + 1), 3);
                                } else {
                                    vallumraptor.level().setBlock(blockPos, (ACBlockRegistry.THIN_BONE.get()).defaultBlockState().setValue(ThinBoneBlock.AXIS, blockState.getValue(DinosaurChopBlock.FACING).getAxis()), 4);
                                }
                                vallumraptor.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                            }
                            stop();
                        }
                    }
                }
                public void stop() {
                    super.stop();
                    this.blockPos = BlockPos.ZERO;
                }
                @Override
                protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
                    BlockState blockState = worldIn.getBlockState(pos);
                    return blockState.is(ACBlockRegistry.DINOSAUR_CHOP.get()) || blockState.is(ACBlockRegistry.COOKED_DINOSAUR_CHOP.get());
                }

                public double acceptedDistance() {
                    return 3F;
                }

                @Override
                protected int nextStartTick(PathfinderMob pCreature) {
                    return reducedTickDelay(100 + vallumraptor.getRandom().nextInt(100));
                }
            });


        }
    }

}
