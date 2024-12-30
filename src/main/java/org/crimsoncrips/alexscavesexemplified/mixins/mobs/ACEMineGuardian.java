package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;


@Mixin(MineGuardianEntity.class)
public class ACEMineGuardian extends Monster implements MineGuardianXtra {

    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> NUCLEAR = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> NOON = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);

    protected ACEMineGuardian(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.entityData.define(OWNER, "-1");
        this.entityData.define(NUCLEAR, false);
        this.entityData.define(NOON , false);
    }

    @Override
    public boolean isNoon() {
        return this.entityData.get(NOON);
    }

    @Override
    public boolean isNuclear() {
        return this.entityData.get(NUCLEAR);
    }

    public String getOwner() {
        return this.entityData.get(OWNER);
    }

    @Override
    public void setNuclear(boolean nuclear) {
        this.entityData.set(NUCLEAR, Boolean.valueOf(nuclear));
    }

    @Override
    public void setOwner(String playerUUID) {
        this.entityData.set(OWNER, String.valueOf(playerUUID));
    }

    public void setNoon(boolean noon) {
        this.entityData.set(NOON, Boolean.valueOf(noon));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Nuclear", this.isNuclear());
        compound.putBoolean("Noon", this.isNoon());
        compound.putString("MineOwner", this.getOwner());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.setNoon(compound.getBoolean("Noon"));
        this.setNuclear(compound.getBoolean("Nuclear"));
        this.setOwner(compound.getString("MineOwner"));
    }


    @WrapWithCondition(method = "isValidTarget", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/MineGuardianEntity;canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"),remap = false)
    private boolean applyOwnerCondition(MineGuardianEntity instance, LivingEntity livingEntity) {
        return !ACExemplifiedConfig.FORLORN_LIGHT_EFFECT_ENABLED || (livingEntity instanceof Player player && !Objects.equals(player.getUUID().toString(), this.getOwner()));
    }
}
