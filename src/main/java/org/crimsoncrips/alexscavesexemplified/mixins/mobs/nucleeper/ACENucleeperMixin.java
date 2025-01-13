package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConverstionAmplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACECaniacBedwars;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACECaniacExplode;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACENucleeperMelee;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(NucleeperEntity.class)
public abstract class ACENucleeperMixin extends Monster implements NucleeperXtra {

    @Shadow public abstract void setTriggered(boolean triggered);

    protected ACENucleeperMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(2, new ACENucleeperMelee((NucleeperEntity)(Object)this));
    }

    private static final EntityDataAccessor<Boolean> RUSTED = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEFUSED = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$define(CallbackInfo ci) {
        this.entityData.define(RUSTED, false);
        this.entityData.define(DEFUSED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Rusted", this.alexsCavesExemplified$isRusted());
        compound.putBoolean("Defused", this.alexsCavesExemplified$isDefused());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$read(CompoundTag compound, CallbackInfo ci) {
        this.alexsCavesExemplified$setRusted(compound.getBoolean("Rusted"));
        this.alexsCavesExemplified$setDefused(compound.getBoolean("Defused"));
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void alexsCavesExemplified$test(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getMainHandItem().is(Items.DIAMOND)){
            alexsCavesExemplified$setDefused(false);
            alexsCavesExemplified$setRusted(false);
        }
        if (player.getItemInHand(hand).is(Items.SHEARS)){
            this.setTriggered(false);
            alexsCavesExemplified$setDefused(true);
        }
        if (player.getItemInHand(hand).is(Items.STICK)){
            alexsCavesExemplified$setRusted(true);
        }
    }

    @Override
    public void alexsCavesExemplified$setRusted(boolean val) {
        this.entityData.set(RUSTED, Boolean.valueOf(val));
    }

    @Override
    public void alexsCavesExemplified$setDefused(boolean val) {
        this.entityData.set(DEFUSED, Boolean.valueOf(val));
    }

    @Override
    public boolean alexsCavesExemplified$isDefused() {
        return this.entityData.get(DEFUSED);
    }

    @Override
    public boolean alexsCavesExemplified$isRusted() {
        return this.entityData.get(RUSTED);
    }

    @WrapWithCondition(method = "mobInteract", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;setTriggered(Z)V"))
    private boolean alexsCavesExemplified$tick(NucleeperEntity instance, boolean triggered) {
        return !alexsCavesExemplified$isDefused();
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean nearestAttack(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.DEFUSION_ENABLED;
    }
}
