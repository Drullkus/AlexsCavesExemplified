package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.TripodfishEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TripodfishEntity.class)
public abstract class ACETripodFishMixin  extends WaterAnimal{


    protected ACETripodFishMixin(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        TripodfishEntity tripodfishEntity = (TripodfishEntity)(Object)this;
        if (ACExemplifiedConfig.ABYSSAL_LIGHT_CHECK_ENABLED){
            tripodfishEntity.goalSelector.addGoal(1, new AvoidEntityGoal<>(tripodfishEntity, LivingEntity.class, 5F, 2.0, 2.8, CuriosCompat::hasLight){
                @Override
                protected int adjustedTickDelay(int pAdjustment) {
                    return 1;
                }
            });
        }


    }


}
