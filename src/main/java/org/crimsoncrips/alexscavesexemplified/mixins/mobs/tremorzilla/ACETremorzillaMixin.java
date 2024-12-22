package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEGammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;


@Mixin(TremorzillaEntity.class)
public abstract class ACETremorzillaMixin extends DinosaurEntity implements ACEGammafied {

    @Shadow public abstract void setMaxBeamBreakLength(float f);

    protected ACETremorzillaMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> GAMMA = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isGamma() {
        return this.entityData.get(GAMMA);
    }

    public void setGamma(boolean variant) {
        this.entityData.set(GAMMA, Boolean.valueOf(variant));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.entityData.define(GAMMA, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Gamma", this.isGamma());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.setGamma(compound.getBoolean("Gamma"));
    }


    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setGamma(false);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma()) {
            this.setGamma(false);
        }
    }

    @Inject(method = "mobInteract", at = @At("TAIL"))
    private void finalizeSpawn(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.is(ACItemRegistry.CARAMEL.get())) {
            this.setGamma(true);
            this.setMaxBeamBreakLength(150);
        }
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V"), index = 0,remap = false)
    private ParticleOptions protonAddition(ParticleOptions pParticleData) {
        return (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_PROTON.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_PROTON.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_PROTON.get()));
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 0),remap = false)
    private int modifyAmount(int amount) {
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? 250 : amount;
    }


}
