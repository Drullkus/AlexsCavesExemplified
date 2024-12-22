package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.crimsoncrips.alexscavesexemplified.compat.AMCompat.amberReset;


@Mixin(LivingEntity.class)
public abstract class ACELivingEntity extends Entity {


    public ACELivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapOperation(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
    private int bypassExpensiveCalculationIfNecessary(LivingEntity instance, float f, float v, Operation<Integer> original) {
        if (ACExemplifiedConfig.HEAVY_GRAVITY_ENABLED) {
            return original.call(instance, f, v * 1.5F);
        } else {
            return original.call(instance, f, v);
        }
    }
    //Props to Drullkus for assistance

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        Block block = livingEntity.level().getBlockState(livingEntity.blockPosition()).getBlock();
        if (ACExemplifiedConfig.PRESERVED_AMBER_ENABLED && block != ACBlockRegistry.AMBER.get()) {
            if (ModList.get().isLoaded("alexsmobs")) {
                amberReset(livingEntity);
            }
            if (livingEntity instanceof Frog frog && frog.isNoAi()) {
                frog.setNoAi(false);
                frog.setInvulnerable(false);
                frog.setSilent(false);
                frog.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1,false,false));
            }
            if (livingEntity instanceof Tadpole tadpole && tadpole.isNoAi()) {
                tadpole.setNoAi(false);
                tadpole.setInvulnerable(false);
                tadpole.setSilent(false);
                tadpole.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1,false,false));
            }
        }
    }
}
