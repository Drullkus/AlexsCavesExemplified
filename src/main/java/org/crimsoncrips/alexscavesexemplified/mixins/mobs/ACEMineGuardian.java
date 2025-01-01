package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMineGuardianHurtBy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(MineGuardianEntity.class)
public class ACEMineGuardian extends Monster implements MineGuardianXtra {

    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> NUCLEAR = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> NOON = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);

    protected ACEMineGuardian(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        MineGuardianEntity mineGuardian = (MineGuardianEntity)(Object)this;
        mineGuardian.targetSelector.addGoal(1, new ACEMineGuardianHurtBy(mineGuardian, new Class[0]));
    }


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.entityData.define(OWNER, "-1");
        this.entityData.define(NUCLEAR, false);
        this.entityData.define(NOON , false);
    }

    @Override
    public boolean alexscavesexemplified$isNoon() {
        return this.entityData.get(NOON);
    }

    @Override
    public boolean alexscavesexemplified$isNuclear() {
        return this.entityData.get(NUCLEAR);
    }

    public String alexscavesexemplified$getOwner() {
        return this.entityData.get(OWNER);
    }

    @Override
    public void alexscavesexemplified$setNuclear(boolean nuclear) {
        this.entityData.set(NUCLEAR, Boolean.valueOf(nuclear));
    }

    @Override
    public void alexscavesexemplified$setOwner(String playerUUID) {
        this.entityData.set(OWNER, String.valueOf(playerUUID));
    }

    @Unique
    public void alexscavesexemplified$setNoon(boolean noon) {
        this.entityData.set(NOON, Boolean.valueOf(noon));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Nuclear", this.alexscavesexemplified$isNuclear());
        compound.putBoolean("Noon", this.alexscavesexemplified$isNoon());
        compound.putString("MineOwner", this.alexscavesexemplified$getOwner());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.alexscavesexemplified$setNoon(compound.getBoolean("Noon"));
        this.alexscavesexemplified$setNuclear(compound.getBoolean("Nuclear"));
        this.alexscavesexemplified$setOwner(compound.getString("MineOwner"));
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"),cancellable = true,remap = false)
    private void isValidTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(ACExemplifiedConfig.REMINEDING_ENABLED && entity instanceof Player player){
            cir.setReturnValue(canAttack(player) && !Objects.equals(player.getUUID().toString(), alexscavesexemplified$getOwner()));
        }
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean hurtAttacker(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.REMINEDING_ENABLED;
    }


}
