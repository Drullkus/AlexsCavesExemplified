package org.crimsoncrips.alexscavesexemplified.mixins.mobs.underzealot;

import com.github.alexmodguy.alexscaves.server.entity.ai.UnderzealotCaptureSacrificeGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMobTargetClosePlayers;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEUnderzealotExtinguishCampfires;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(UnderzealotEntity.class)
public abstract class ACEUnderzealot extends Monster {


    protected ACEUnderzealot(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;

        underzealot.targetSelector.addGoal(2, new ACEMobTargetClosePlayers(underzealot, 40, 12.0F,livingEntity -> {
            boolean light = (!CuriosCompat.hasLight(livingEntity)) || !AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get();
            boolean respect = (!livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get())) || !AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() ;
            return light && respect;
        }) {
            public boolean canUse() {
                return !underzealot.isTargetingBlocked() && super.canUse();
            }
        });

        if (AlexsCavesExemplified.COMMON_CONFIG.EXTINGUISH_CAMPFIRES_ENABLED.get()){
            this.goalSelector.addGoal(7, new ACEUnderzealotExtinguishCampfires(underzealot, 32));
        }



    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get())
            return;
        LivingEntity target = this.getTarget();
        if (target == null)
            return;
        if (this.getLastHurtByMob() == target)
            return;
        if (CuriosCompat.hasLight(target)) {
            this.setTarget(null);
        }
    }



    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 12))
    private boolean alexsCavesExemplified$nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !AlexsCavesExemplified.COMMON_CONFIG.FORLORN_LIGHT_EFFECT_ENABLED.get() && !AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get();
    }


}
