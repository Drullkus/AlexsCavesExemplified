package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.GuanoEntity;
import com.github.alexmodguy.alexscaves.server.potion.SugarRushEffect;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.effect.ACEEffects;
import org.crimsoncrips.alexscavesexemplified.misc.ACEDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SugarRushEffect.class)
public abstract class ACESugarRush extends MobEffect {
    @Unique
    private int lastDuration = -1;


    public ACESugarRush(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Inject(method = "applyEffectTick", at = @At("HEAD"), remap = false)
    private void getMaxLoadTime(LivingEntity entity, int amplifier, CallbackInfo ci) {
        if (lastDuration <= 1 && ACExemplifiedConfig.SUGAR_CRASH_ENABLED) {
            int sugarcrashLevel = amplifier + 1;
            entity.addEffect(new MobEffectInstance(ACEEffects.SUGAR_CRASH.get(), 400, amplifier));
            entity.hurt(ACEDamageTypes.causeSugarCrash(entity.level().registryAccess()), sugarcrashLevel * 2);
            entity.playSound(SoundEvents.GENERIC_EXPLODE, 0.5F, 0F);

        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0;
    }


}
