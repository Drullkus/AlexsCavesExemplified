package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConverstionAmplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACENucleeperMelee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(NucleeperEntity.class)
public abstract class ACENucleeperMixin extends Monster implements NucleeperXtra {

    @Shadow public abstract void setTriggered(boolean triggered);

    protected ACENucleeperMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(2, new ACENucleeperMelee((NucleeperEntity)(Object)this));
    }

    private static final EntityDataAccessor<Boolean> RUSTED = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEFUSED = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(RUSTED, false);
        this.entityData.define(DEFUSED, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Rusted", this.alexsCavesExemplified$isRusted());
        compound.putBoolean("Defused", this.alexsCavesExemplified$isDefused());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.alexsCavesExemplified$setRusted(compound.getBoolean("Rusted"));
        this.alexsCavesExemplified$setDefused(compound.getBoolean("Defused"));
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void alexsCavesExemplified$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getItemInHand(hand).is(Items.SHEARS) && !alexsCavesExemplified$isDefused() && AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get()){
            this.setTriggered(false);
            this.playSound(SoundEvents.SHEEP_SHEAR);
            alexsCavesExemplified$setDefused(true);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!AlexsCavesExemplified.COMMON_CONFIG.RUSTED_NUCLEEPER_ENABLED.get() && alexsCavesExemplified$isRusted()){
            alexsCavesExemplified$setRusted(false);
        }
        if (!AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get() && alexsCavesExemplified$isDefused()){
            alexsCavesExemplified$setDefused(false);
        }
    }

    @ModifyArg(method = "explode", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/NuclearExplosionEntity;setSize(F)V"),remap = false)
    private float alexsCavesExemplified$explode(float f) {
        return alexsCavesExemplified$isRusted() ? f / 2 : f;
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
        return !AlexsCavesExemplified.COMMON_CONFIG.DEFUSION_ENABLED.get();
    }
}
