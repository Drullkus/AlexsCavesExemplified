package org.crimsoncrips.alexscavesexemplified.mixins.mobs.underzealot;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMobTargetClosePlayers;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEUnderzealotExtinguishCampfires;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;


@Mixin(UnderzealotEntity.class)
public abstract class ACEUnderzealot extends Monster {


    protected ACEUnderzealot(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        UnderzealotEntity underzealot = (UnderzealotEntity)(Object)this;
        underzealot.targetSelector.addGoal(2, new ACEMobTargetClosePlayers(underzealot, 40, 12.0F,livingEntity -> {
            boolean light = (!CuriosCompat.hasLight(livingEntity)) || !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED;
            boolean respect = (!livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get())) || !ACExemplifiedConfig.UNDERZEALOT_RESPECT_ENABLED ;
            return light && respect;
        }) {
            public boolean canUse() {
                return !underzealot.isTargetingBlocked() && super.canUse();
            }
        });

        if (ACExemplifiedConfig.EXTINGUISH_CAMPFIRES_ENABLED){
            this.goalSelector.addGoal(7, new ACEUnderzealotExtinguishCampfires(underzealot, 32));
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



    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 12))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED && !ACExemplifiedConfig.UNDERZEALOT_RESPECT_ENABLED;
    }


}
