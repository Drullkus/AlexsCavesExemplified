package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.CaramelCubeEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.crimsoncrips.alexscavesexemplified.server.ACESoundRegistry;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ACEAssimilateCaramel<T extends CaramelCubeEntity> extends Goal {

    protected final CaramelCubeEntity mob;
    protected Class<T> targetType;
    private final double speedModifier;
    private final float areaSize;
    private final PathNavigation navigation;
    private int timeToRecalcPath;

    public ACEAssimilateCaramel(CaramelCubeEntity mob, Class<T> targetType, float areaSize, double speedModifier) {
        this.mob = mob;
        this.targetType = targetType;
        this.speedModifier = speedModifier;
        this.areaSize = areaSize;
        this.navigation = mob.getNavigation();
    }

    @Override
    public boolean canUse() {
        return getTarget() != null;
    }

    public void tick() {
        var target = getTarget();
        if (target != null && !mob.isLeashed()) {
            mob.getLookControl().setLookAt(target, 10.0F, (float) this.mob.getMaxHeadXRot());
            if (--timeToRecalcPath <= 0) {
                timeToRecalcPath = adjustedTickDelay(10);
                navigation.moveTo(target, speedModifier);
            }
        } else {
            timeToRecalcPath = adjustedTickDelay(10);
            navigation.stop();
        }
        if (target != null && this.mob.distanceToSqr(target) < 4) {
            mob.level().playSound(null, mob.getBlockX(), mob.getBlockY(), mob.getBlockZ(), ACESoundRegistry.CARAMEL_EAT.get(), SoundSource.AMBIENT, 2.0F, 1.0F);
            mob.setSlimeSize(mob.getSlimeSize() + 1,true);
            target.discard();
            stop();
        }
    }

    @Nullable
    private LivingEntity getTarget() {
        var level = this.mob.level();
        var targetList = level.getEntitiesOfClass(targetType, mob.getBoundingBox().inflate(areaSize));
        if (targetList.get(0).getSlimeSize() < mob.getSlimeSize() && mob.getSlimeSize() <= 2) {
            return targetList.get(0);
        }
        return null;
    }
}