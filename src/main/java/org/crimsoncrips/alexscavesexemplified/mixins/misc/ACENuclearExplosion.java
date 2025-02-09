package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(NuclearExplosionEntity.class)
public abstract class ACENuclearExplosion extends Entity {


    public ACENuclearExplosion(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean alexsCavesExemplified$tick(LivingEntity instance, DamageSource entity, float ev) {
        if (AlexsCavesExemplified.COMMON_CONFIG.TOUGH_ROACHES_ENABLED.get()){
            return !(instance instanceof GammaroachEntity) && !AMCompat.cockroach(instance);
        }
        return true;
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private boolean alexsCavesExemplified$tick2(LivingEntity instance, Vec3 vec3) {
        if (AlexsCavesExemplified.COMMON_CONFIG.TOUGH_ROACHES_ENABLED.get()){
            return !(instance instanceof GammaroachEntity) && !AMCompat.cockroach(instance);
        }
        return true;
    }




}
