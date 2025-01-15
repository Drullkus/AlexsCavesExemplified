package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMineGuardianHurtBy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(MineGuardianEntity.class)
public abstract class ACEMineGuardian extends Monster implements MineGuardianXtra {

    @Shadow public abstract boolean isExploding();

    @Shadow private float explodeProgress;
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> NUCLEAR = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);

    protected ACEMineGuardian(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void alexsCavesExemplified$finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        alexsCavesExemplified$setNuclear(true);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void alexsCavesExemplified$registerGoals(CallbackInfo ci) {
        MineGuardianEntity mineGuardian = (MineGuardianEntity)(Object)this;
        mineGuardian.targetSelector.addGoal(1, new ACEMineGuardianHurtBy(mineGuardian, new Class[0]));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (!ACExemplifiedConfig.REMINEDING_ENABLED && !Objects.equals(alexsCavesExemplified$getOwner(), "-1")){
            alexsCavesExemplified$setOwner("-1");
        }
    }



    @Override
    public boolean alexsCavesExemplified$isNoon() {
        return this.getName().getString().equals("Noon") && ACExemplifiedConfig.NOON_GUARDIAN_ENABLED && Objects.equals(alexsCavesExemplified$getOwner(), "-1");
    }

    @Override
    public boolean alexsCavesExemplified$isNuclear() {
        return this.entityData.get(NUCLEAR);
    }

    public String alexsCavesExemplified$getOwner() {
        return this.entityData.get(OWNER);
    }

    @Override
    public void alexsCavesExemplified$setNuclear(boolean nuclear) {
        this.entityData.set(NUCLEAR, Boolean.valueOf(nuclear));
    }

    @Override
    public void alexsCavesExemplified$setOwner(String playerUUID) {
        this.entityData.set(OWNER, String.valueOf(playerUUID));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$define(CallbackInfo ci) {
        this.entityData.define(OWNER, "-1");
        this.entityData.define(NUCLEAR, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Nuclear", this.alexsCavesExemplified$isNuclear());
        compound.putString("MineOwner", this.alexsCavesExemplified$getOwner());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$read(CompoundTag compound, CallbackInfo ci) {
        this.alexsCavesExemplified$setNuclear(compound.getBoolean("Nuclear"));
        this.alexsCavesExemplified$setOwner(compound.getString("MineOwner"));
    }

    @Inject(method = "isValidTarget", at = @At("HEAD"),cancellable = true,remap = false)
    private void alexsCavesExemplified$isValidTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(ACExemplifiedConfig.REMINEDING_ENABLED && entity instanceof Player player){
            cir.setReturnValue(canAttack(player) && !Objects.equals(player.getUUID().toString(), alexsCavesExemplified$getOwner()));
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/MineGuardianEntity;isExploding()Z",ordinal = 2))
    private boolean alexsCavesExemplified$tick(MineGuardianEntity instance, Operation<Boolean> original) {
        return original.call(instance) && !alexsCavesExemplified$isNuclear();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick1(CallbackInfo ci) {
        if(this.alexsCavesExemplified$isNuclear()){
            if (this.isExploding()) {
                if (this.explodeProgress >= 10.0F) {
                    this.remove(RemovalReason.KILLED);
                    this.nukeExplode();
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.3F, (double)1.0F, (double)0.3F));
            }
        }
    }

    private void nukeExplode() {
        NuclearExplosionEntity explosion = (NuclearExplosionEntity)((EntityType) ACEntityRegistry.NUCLEAR_EXPLOSION.get()).create(this.level());
        explosion.copyPosition(this);
        explosion.setSize(AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get().floatValue());
        if (!this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            explosion.setNoGriefing(true);
        }

        this.level().addFreshEntity(explosion);
    }

    @WrapWithCondition(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V",ordinal = 2))
    private boolean alexsCavesExemplified$registerGoals(GoalSelector instance, int pPriority, Goal pGoal) {
        return !ACExemplifiedConfig.REMINEDING_ENABLED;
    }


}
