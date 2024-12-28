package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRarity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.ibm.icu.text.PluralRules.Operand.v;
import static org.crimsoncrips.alexscavesexemplified.compat.AMCompat.amberReset;


@Mixin(LivingEntity.class)
public abstract class ACELivingEntity extends Entity {


    public ACELivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapOperation(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
    private int causeFallDamage(LivingEntity instance, float f, float v, Operation<Integer> original) {
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

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getTicksFrozen()I"), cancellable = true)
    private void freezingAlterations(CallbackInfo ci) {
        if (ACExemplifiedConfig.CRYONIC_CAVITY_ENABLED){
            ci.cancel();
            int i = this.getTicksFrozen();
            if (this.isInPowderSnow && this.canFreeze()) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), i + 1));
            } else if (!this.level().getBiome(this.blockPosition()).is(ACBiomeRegistry.CANDY_CAVITY)) {
                this.setTicksFrozen(Math.max(0, i - 2));
            }
        }
        if (this.level().getBiome(this.blockPosition()).is(ACBiomeRegistry.CANDY_CAVITY) && this.level().random.nextDouble() < 0.01 && !this.isOnFire() && !this.getType().is(ACTagRegistry.CANDY_MOBS)){
            this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() + 1));
        }
    }

}
