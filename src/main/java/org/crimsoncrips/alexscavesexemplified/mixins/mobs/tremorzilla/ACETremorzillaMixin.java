package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEGammafied;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export=true)
@Mixin(TremorzillaEntity.class)
public abstract class ACETremorzillaMixin extends DinosaurEntity implements ACEGammafied {

    @Shadow public abstract void setMaxBeamBreakLength(float f);

    @Shadow public abstract Animation getAnimation();

    @Shadow public abstract void setAnimation(Animation animation);

    @Shadow public abstract int getAnimationTick();

    @Shadow public Vec3 beamServerTarget;

    @Shadow protected abstract Vec3 createInitialBeamVec();


    @Shadow @Final public static Animation ANIMATION_ROAR_2;

    @Shadow protected abstract void tickBreath();

    @Shadow private int beamTime;

    @Shadow public abstract void setCharge(int charge);

    @Shadow public abstract void setBeamEndPosition(@Nullable Vec3 vec3);

    @Shadow private float beamProgress;

    protected ACETremorzillaMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> GAMMA = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANIMATION_BEAMING = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isGamma() {
        return this.entityData.get(GAMMA);
    }

    public void setGamma(boolean variant) {
        this.entityData.set(GAMMA, Boolean.valueOf(variant));
    }

    public boolean isAnimationBeaming() {
        return this.entityData.get(ANIMATION_BEAMING);
    }

    public void setAnimationBeaming(boolean variant) {
        this.entityData.set(ANIMATION_BEAMING, Boolean.valueOf(variant));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.entityData.define(GAMMA, false);
        this.entityData.define(ANIMATION_BEAMING, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Gamma", this.isGamma());
        compound.putBoolean("AnimationBeaming", this.isAnimationBeaming());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compound, CallbackInfo ci) {
        this.setGamma(compound.getBoolean("Gamma"));
        this.setAnimationBeaming(compound.getBoolean("AnimationBeaming"));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/citadel/animation/AnimationHandler;updateAnimations(Lnet/minecraft/world/entity/Entity;)V"))
    private void tick(CallbackInfo ci) {
        if (!ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma()) {
            this.setGamma(false);
        }
        System.out.println(beamProgress);
        System.out.println(isAnimationBeaming());
        if (getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 20 && getAnimationTick() <= 45){
            setAnimationBeaming(true);
            tickBreath();
        } else if (isAnimationBeaming() && getAnimation() == ANIMATION_ROAR_2 ){
            beamTime = 0;
            this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
            this.beamServerTarget = null;
            this.setBeamEndPosition(null);
            this.setCharge(0);
            setAnimationBeaming(false);
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.is(ACItemRegistry.CARAMEL.get())) {
            this.setGamma(true);
            this.setMaxBeamBreakLength(150);
        }
        if (itemStack.is(Items.STICK)) {
            this.beamServerTarget = createInitialBeamVec();
            this.setMaxBeamBreakLength(100F);
            setAnimation(ANIMATION_ROAR_2);
        }
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 2),remap = false)
    private ParticleOptions protonAddition(ParticleOptions pParticleData) {
        return (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_PROTON.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_PROTON.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_PROTON.get()));
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 1),remap = false)
    private int beamTime(int amount) {
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? 250 : amount;
    }

    @ModifyConstant(method = "tick",constant = @Constant(floatValue = 100.0F,ordinal = 0),remap = false)
    private float beamLength(float constant) {
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? 180 : constant;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;isFiring()Z",ordinal = 0),remap = false)
    public boolean alterIsFiring0(boolean original) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED){
            return original || isAnimationBeaming();
        } else return original;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;isFiring()Z",ordinal = 1),remap = false)
    public boolean alterIsFiring1(boolean original) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED){
            return original && !isAnimationBeaming();
        } else return original;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;isFiring()Z",ordinal = 2),remap = false)
    public boolean alterIsFiring2(boolean original) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED){
            return original || isAnimationBeaming();
        } else return original;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;isFiring()Z",ordinal = 3),remap = false)
    public boolean alterIsFiring3(boolean original) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED){
            return original || isAnimationBeaming();
        } else return original;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;isFiring()Z",ordinal = 4),remap = false)
    public boolean alterIsFiring4(boolean original) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED){
            return original || isAnimationBeaming();
        } else return original;
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;isFiring()Z",ordinal = 5),remap = false)
    public boolean alterIsFiring5(boolean original) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED){
            return original && !isAnimationBeaming();
        } else return original;
    }

    @Inject(method = "travel", at = @At(value = "HEAD"),remap = false, cancellable = true)
    public void travelSuppress(Vec3 vec3d, CallbackInfo ci) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isAnimationBeaming()){
            ci.cancel();
        }
    }






}
