package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VesperTargetUnderneathEntities;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACEHurtByTargetGoal;
import org.crimsoncrips.alexscavesexemplified.goals.ACEVesperTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Objects;


@Mixin(VesperEntity.class)
public abstract class ACEVesper extends Monster {


    protected ACEVesper(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        VesperEntity vesper = (VesperEntity)(Object)this;
        if (ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED){
            this.targetSelector.addGoal(3, new ACEVesperTarget<>(vesper, 32.0F, Player.class,livingEntity -> {
                return !livingEntity.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) && !(livingEntity instanceof Player player && curiosLight(player));
            }));

            this.targetSelector.addGoal(3, new ACEVesperTarget<>(vesper, 32.0F, UnderzealotEntity.class, livingEntity -> {
                if (livingEntity instanceof UnderzealotEntity underzealot){
                    return underzealot.getFirstPassenger() instanceof VesperEntity;
                } else return false;
            }));

            vesper.goalSelector.addGoal(1, new AvoidEntityGoal<>(vesper, LivingEntity.class, 4.0F, 1.5, 2, (livingEntity) -> {
                return vesper.getLastAttacker() != livingEntity && (livingEntity.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) || (livingEntity instanceof Player player && curiosLight(player))) ;
            }));

        }

        if (ACExemplifiedConfig.VESPER_CANNIBALIZE_ENABLED){
            this.targetSelector.addGoal(3, new ACEVesperTarget<>(vesper, 32.0F, Bat.class, Objects::nonNull));
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
        if (target.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) || target instanceof Player player && curiosLight(player)) {
            this.setTarget(null);
        }
    }

    public boolean curiosLight(Player player){
        if (ModList.get().isLoaded("curiouslanterns")) {
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player " + player.getName() + " has no curios inventory!"));
            return handler.getStacksHandler("belt").orElseThrow().getStacks().getStackInSlot(0).is(ACExexmplifiedTagRegistry.LIGHT);
        } else return false;
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 7))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED;
    }

}
