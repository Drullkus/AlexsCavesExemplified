package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TremorzillaLegSolver;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

import static net.minecraft.world.entity.EntityType.LIGHTNING_BOLT;

@Debug(export=true)
@Mixin(TremorzillaEntity.class)
public abstract class ACETremorzillaMixin extends DinosaurEntity implements Gammafied {

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

    @Shadow public abstract void setFiring(boolean firing);

    @Shadow public TremorzillaLegSolver legSolver;

    @Shadow private double lx;

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
    boolean sound;
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

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/DinosaurEntity;tick()V"))
    private void tick(CallbackInfo ci) {
        Level level = level();
        if (!ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma()) {
            this.setGamma(false);
        }
        if (!sound && getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() == 15 && isAnimationBeaming()){
            this.playSound(ACSoundRegistry.TREMORZILLA_ROAR.get(), 8.0F, 1.0F);
            sound = true;
        }
        if (getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 20 && getAnimationTick() <= 45 && isAnimationBeaming()){
            setFiring(true);
        } else if (isAnimationBeaming() && getAnimation() == ANIMATION_ROAR_2 && getAnimationTick() >= 45){
            sound = false;
            setFiring(false);
            this.playSound(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
            if (getAnimationTick() >= 55) {
                int rotate = random.nextInt(0, 361);
                for (int i = 0;i < 7;i++) {
                    rotate = rotate + 51;
                    Vec3 vec3 = new Vec3(this.getX() + Math.cos(rotate * 10) * 18, this.getY(), this.getZ() + Math.sin(rotate * 10) * 18);

                    LightningBolt lightningBolt = LIGHTNING_BOLT.create(level);
                    if (lightningBolt != null) {
                        lightningBolt.setPos(vec3);
                        level.addFreshEntity(lightningBolt);
                    }

                }
                beamTime = 0;
                this.beamServerTarget = null;
                this.setBeamEndPosition(null);
                setAnimationBeaming(false);
                this.setCharge(0);
            }
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
            setAnimationBeaming(true);
            this.beamServerTarget = createInitialBeamVec();
            this.setMaxBeamBreakLength(180F);
            setAnimation(ANIMATION_ROAR_2);
        }
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 2))
    private ParticleOptions protonAddition(ParticleOptions pParticleData) {
        return (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_PROTON.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_PROTON.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_PROTON.get()));
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 0))
    private ParticleOptions explosionAddition(ParticleOptions pParticleData) {
        return (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_EXPLOSION.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_EXPLOSION.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_EXPLOSION.get()));
    }

    @ModifyArg(method = "tickBreath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addAlwaysVisibleParticle(Lnet/minecraft/core/particles/ParticleOptions;ZDDDDDD)V",ordinal = 1))
    private ParticleOptions lightningAddition(ParticleOptions pParticleData) {
        return (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? ACEParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get() : this.getAltSkin() == 2 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_TECTONIC_LIGHTNING.get() : (this.getAltSkin() == 1 ? (ParticleOptions)ACParticleRegistry.TREMORZILLA_RETRO_LIGHTNING.get() : (ParticleOptions)ACParticleRegistry.TREMORZILLA_LIGHTNING.get()));
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 1))
    private int beamTime(int amount) {
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? 250 : amount;
    }

    @ModifyConstant(method = "tick",constant = @Constant(floatValue = 100.0F,ordinal = 0))
    private float beamLength(float constant) {
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? 180 : constant;
    }

    @ModifyConstant(method = "hurtEntitiesAround",constant = @Constant(intValue = 2),remap = false)
    private int modifyIrradiation(int amount) {
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma() ? 6 : amount;
    }

    @Inject(method = "hurtEntitiesAround", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;knockbackTarget(Lnet/minecraft/world/entity/Entity;DDDZ)V"),locals = LocalCapture.CAPTURE_FAILSOFT,remap = false)
    private void mobInteract(Vec3 center, float radius, float damageAmount, float knockbackAmount, boolean radioactive, boolean hurtsOtherKaiju, boolean stretchY, CallbackInfoReturnable<Boolean> cir, AABB aabb, boolean flag, DamageSource damageSource, Iterator var11, LivingEntity living) {
        if (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && isGamma()){
            living.setRemainingFireTicks(1000);
        }
    }









}
