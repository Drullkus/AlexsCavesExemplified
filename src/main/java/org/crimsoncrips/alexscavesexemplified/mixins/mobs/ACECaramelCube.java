package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.crimsoncrips.alexsmobsinteraction.goal.AMIFollowNearestGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CaramelCubeEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEAssimilateCaramel;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACECaniacBedwars;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACECaniacExplode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(CaramelCubeEntity.class)
public abstract class ACECaramelCube extends Monster {

    protected ACECaramelCube(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        CaramelCubeEntity caramelCube = (CaramelCubeEntity)(Object)this;
        caramelCube.targetSelector.addGoal(2, new ACEAssimilateCaramel<>(caramelCube, CaramelCubeEntity.class,true));

    }

}
