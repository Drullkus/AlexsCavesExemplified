package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AtlatitanEntity.class)
public abstract class ACEAtlatitan extends SauropodBaseEntity {

    protected ACEAtlatitan(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "onStep", at = @At("TAIL"),remap = false)
    private void registerGoals(CallbackInfo ci) {

        AtlatitanEntity atlatitan = (AtlatitanEntity)(Object)this;

        for (LivingEntity entity : atlatitan.level().getEntitiesOfClass(LivingEntity.class, atlatitan.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != atlatitan && entity.getBbHeight() <= 1.2F) {
                entity.hurt(atlatitan.damageSources().mobAttack(atlatitan), 4.0F);
            }
        }
    }
}
