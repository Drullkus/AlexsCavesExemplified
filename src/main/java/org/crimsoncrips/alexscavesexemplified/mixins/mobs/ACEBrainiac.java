package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACECreatureAITargetItems;
import org.crimsoncrips.alexscavesexemplified.goals.ACEPickupDroppedBarrels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BrainiacEntity.class)
public abstract class ACEBrainiac extends Monster {

    protected ACEBrainiac(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"),remap = false)
    private void registerGoals(CallbackInfo ci) {
        BrainiacEntity brainiac = (BrainiacEntity)(Object)this;
        if (ACExemplifiedConfig.WASTE_PICKUP_ENABLED){
            brainiac.targetSelector.addGoal(1, new ACEPickupDroppedBarrels<>(brainiac,true,true,1,30));

        }
    }

    @Inject(method = "postAttackEffect", at = @At("HEAD"),cancellable = true,remap = false)
    private void postAttack(LivingEntity entity, CallbackInfo ci) {
        ci.cancel();
        if (entity != null && entity.isAlive()) {
            entity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 400,ACExemplifiedConfig.WASTE_POWERUP_ENABLED && this.getPersistentData().getBoolean("WastePowered") ? 1 : 0));
        }
        if (ACExemplifiedConfig.WASTE_POWERUP_ENABLED && this.getPersistentData().getBoolean("WastePowered")){
            this.getPersistentData().putBoolean("WastePowered", false);
        }
    }
}
