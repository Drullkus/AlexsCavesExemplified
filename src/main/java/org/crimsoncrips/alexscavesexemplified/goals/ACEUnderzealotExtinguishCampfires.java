//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

import static com.github.alexmodguy.alexscaves.server.block.DinosaurChopBlock.BITES;

public class ACEUnderzealotExtinguishCampfires extends MoveToBlockGoal {
    private final UnderzealotEntity underzealot;

    public ACEUnderzealotExtinguishCampfires(UnderzealotEntity entity, int range) {
        super(entity, (double)1.0F, range, range);
        this.underzealot = entity;
    }

    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(500 + this.underzealot.getRandom().nextInt(1000));
    }

    public boolean canUse() {
        return super.canUse() && !this.isTargetBlocked(this.blockPos.getCenter());
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        return this.mob.level().clip(new ClipContext(Vector3d, target, Block.COLLIDER, Fluid.NONE, this.mob)).getType() != Type.MISS;
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.underzealot.isCarrying();
    }

    public double acceptedDistance() {
        return (double)2.0F;
    }

    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        if (target != null) {
            this.underzealot.lookAt(Anchor.EYES, Vec3.atCenterOf(target));
            BlockState blockState = this.underzealot.level().getBlockState(target);
            if (this.isReachedTarget() && blockState.is(Blocks.CAMPFIRE)) {
                if (this.underzealot.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.underzealot.setAnimation(UnderzealotEntity.ANIMATION_BREAKTORCH);
                } else if (this.underzealot.getAnimation() == UnderzealotEntity.ANIMATION_BREAKTORCH && this.underzealot.getAnimationTick() == 10) {
                    underzealot.level().setBlock(blockPos, blockState.setValue(CampfireBlock.LIT, false), 3);
                    underzealot.playSound(SoundEvents.FIRE_EXTINGUISH, 1F, 1F);
                }
            }
            if (this.isReachedTarget() && blockState.is(Blocks.SOUL_CAMPFIRE)) {
                if (this.underzealot.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.underzealot.setAnimation(UnderzealotEntity.ANIMATION_BREAKTORCH);
                } else if (this.underzealot.getAnimation() == UnderzealotEntity.ANIMATION_BREAKTORCH && this.underzealot.getAnimationTick() == 10) {
                    underzealot.level().setBlock(blockPos, blockState.setValue(CampfireBlock.LIT, false), 3);
                    underzealot.playSound(SoundEvents.FIRE_EXTINGUISH, 1F, 1F);
                }
            }
        }

    }

    protected BlockPos getMoveToTarget() {
        return this.blockPos;
    }

    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        return pos != null && worldIn.getLightEmission(pos) > 0 && (blockState.is(Blocks.SOUL_CAMPFIRE) || blockState.is(Blocks.CAMPFIRE));
    }
}
