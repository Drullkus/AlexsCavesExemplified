package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.crimsoncrips.alexsmobsinteraction.AMInteractionTagRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Objects;


@Mixin(CorrodentEntity.class)
public abstract class ACECorrodent extends Monster implements UnderzealotSacrifice {

    private int sacrificeTime = 0;
    private boolean isBeingSacrificed = false;


    protected ACECorrodent(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        CorrodentEntity corrodent = (CorrodentEntity)(Object)this;
        if (ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED){
            corrodent.targetSelector.addGoal(2, new MobTarget3DGoal(corrodent, Player.class, false,10, livingEntity -> {
                return !livingEntity.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) && (livingEntity instanceof Player player && !curiosLight(player));
            }));
            
        }


    }

    public void triggerSacrificeIn(int time) {
        isBeingSacrificed = true;
        sacrificeTime = time;
    }

    @Override
    public boolean isValidSacrifice(int distanceFromGround) {
        return ACExemplifiedConfig.CORRODENT_CONVERSION_ENABLED && this.getHealth() <= 0.50F * this.getMaxHealth() ;
    }



    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (isBeingSacrificed && this.isPassenger() && !level().isClientSide && ACExemplifiedConfig.CORRODENT_CONVERSION_ENABLED) {
            sacrificeTime--;
            if (sacrificeTime < 10) {
                this.level().broadcastEntityEvent(this, (byte) 61);
            }
            if (sacrificeTime < 0) {
                if (this.isPassenger() && this.getVehicle() instanceof UnderzealotEntity underzealot) {
                    underzealot.postSacrifice(this);
                    underzealot.triggerIdleDigging();
                }
                this.stopRiding();
                UnderzealotEntity underzealot1 = this.convertTo(ACEntityRegistry.UNDERZEALOT.get(), true);
                if (underzealot1 != null) {
                    this.playSound(ACSoundRegistry.CORRODENT_HURT.get(), 8.0F, 1.0F);
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, underzealot1);
                    underzealot1.triggerIdleDigging();
                    underzealot1.stopRiding();
                }
            }
        }

        LivingEntity target = this.getTarget();
        if (ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED && target != this.getLastAttacker() && target != null) {
            if (target.isHolding(Ingredient.of(ACExexmplifiedTagRegistry.LIGHT)) || target instanceof Player player && curiosLight(player)) {
                this.setTarget(null);
            }
        }
    }

    public boolean curiosLight(Player player){
        if (ModList.get().isLoaded("curiouslanterns")) {
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(player).orElseThrow(() -> new IllegalStateException("Player " + player.getName() + " has no curios inventory!"));
            return handler.getStacksHandler("belt").orElseThrow().getStacks().getStackInSlot(0).is(ACExexmplifiedTagRegistry.LIGHT);
        } else return false;
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 8))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED;
    }

}
