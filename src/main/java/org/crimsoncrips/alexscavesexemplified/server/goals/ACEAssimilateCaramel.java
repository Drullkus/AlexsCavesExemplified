package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.CaramelCubeEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.crimsoncrips.alexscavesexemplified.client.ACESoundRegistry;

import javax.annotation.Nullable;

public class ACEAssimilateCaramel<T extends CaramelCubeEntity> extends NearestAttackableTargetGoal {

    protected final CaramelCubeEntity caramelCube;

    public ACEAssimilateCaramel(CaramelCubeEntity pMob, Class pTargetType, boolean pMustSee) {
        super(pMob, pTargetType, pMustSee);
        caramelCube = pMob;
    }

    @Override
    public boolean canContinueToUse() {
        return target instanceof CaramelCubeEntity caramelTarget && caramelTarget.getSlimeSize() == caramelCube.getSlimeSize() && caramelCube.getSlimeSize() < 2 && super.canContinueToUse();
    }

    public void tick() {
        super.tick();
        if (target != null && this.caramelCube.distanceToSqr(target) < 5) {
            caramelCube.level().playSound(null, caramelCube.getBlockX(), caramelCube.getBlockY(), caramelCube.getBlockZ(), ACESoundRegistry.CARAMEL_EAT.get(), SoundSource.AMBIENT, 2.0F, 1.0F);
            caramelCube.setSlimeSize(caramelCube.getSlimeSize() + 1,true);
            caramelCube.setTarget(null);
            caramelCube.setLastHurtByMob(null);
            target.discard();
            stop();
        }
    }

    @Override
    protected void findTarget() {
        if (targetType == CaramelCubeEntity.class){
            this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), (p_148152_) -> {
                return p_148152_ instanceof CaramelCubeEntity caramelTarget && caramelTarget.getSlimeSize() == caramelCube.getSlimeSize() && caramelCube.getSlimeSize() < 2 && caramelTarget != caramelCube && caramelTarget.tickCount > 400;
            }), this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
    }

}