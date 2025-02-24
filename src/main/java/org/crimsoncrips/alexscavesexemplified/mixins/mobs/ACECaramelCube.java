package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.CaramelCubeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEAssimilateCaramel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CaramelCubeEntity.class)
public abstract class ACECaramelCube extends Monster {

    protected ACECaramelCube(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        CaramelCubeEntity caramelCube = (CaramelCubeEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.CARAMERGING_ENABLED.get()) {
            caramelCube.targetSelector.addGoal(2, new ACEAssimilateCaramel<>(caramelCube, CaramelCubeEntity.class, true));
        }
    }

}
