package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.google.common.base.Predicates;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.server.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEVesperTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;


@Mixin(VesperEntity.class)
public abstract class ACEVesper extends Monster {


    @Shadow public int groundedFor;

    @Shadow public int timeHanging;

    @Shadow public abstract void setHanging(boolean hanging);

    protected ACEVesper(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        VesperEntity vesper = (VesperEntity)(Object)this;
        if (ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED){
            this.targetSelector.addGoal(3, new ACEVesperTarget<>(vesper, 32.0F, Player.class,livingEntity -> {
                return !CuriosCompat.hasLight(livingEntity);
            }));

            vesper.goalSelector.addGoal(1, new AvoidEntityGoal<>(vesper, LivingEntity.class, 4.0F, 1.5, 2, (livingEntity) -> {
                return vesper.getLastAttacker() != livingEntity && CuriosCompat.hasLight(livingEntity) ;
            }));

        }

        if (ACExemplifiedConfig.ANTI_SACRIFICE_ENABLED){
            this.targetSelector.addGoal(3, new ACEVesperTarget<>(vesper, 32.0F, UnderzealotEntity.class, livingEntity -> {
                if (livingEntity instanceof UnderzealotEntity underzealot){
                    return underzealot.getFirstPassenger() instanceof VesperEntity;
                } else return false;
            }));
        }

        if (ACExemplifiedConfig.VESPER_HUNT_ENABLED){
            this.targetSelector.addGoal(3, new ACEVesperTarget<>(vesper, 32.0F, LivingEntity.class, buildPredicateFromTag(ACExexmplifiedTagRegistry.VESPER_HUNT)));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED)
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

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.ARROW) && ACExemplifiedConfig.VESPER_SHOTDOWN_ENABLED){
            this.setHanging(false);
            groundedFor = 1000;
        }
        return super.hurt(pSource, pAmount);
    }


    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 7))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED;
    }

    //Taken from AM
    private static Predicate<LivingEntity> buildPredicateFromTag(TagKey<EntityType<?>> entityTag) {
        return entityTag == null ? Predicates.alwaysFalse() : (e) -> e.isAlive() && e.getType().is(entityTag);
    }

}
