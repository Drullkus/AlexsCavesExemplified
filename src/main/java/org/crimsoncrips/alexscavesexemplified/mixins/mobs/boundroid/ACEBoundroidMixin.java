package org.crimsoncrips.alexscavesexemplified.mixins.mobs.boundroid;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.BoundroidMagnetism;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEMineGuardianHurtBy;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BoundroidEntity.class)
public abstract class ACEBoundroidMixin extends Monster implements BoundroidMagnetism {

    @Shadow public int stopSlammingFor;

    @Shadow public abstract boolean isSlamming();

    private static final EntityDataAccessor<Boolean> MAGNETIZING = SynchedEntityData.defineId(BoundroidEntity.class, EntityDataSerializers.BOOLEAN);

    protected ACEBoundroidMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    int activateDelay;

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        BoundroidEntity boundroidEntity = (BoundroidEntity) (Object) this;
        if (alexsCavesExemplified$isMagnetizing() && boundroidEntity.getRandom().nextDouble() < 0.01){
            for (int i = 0;i < 10;i++){
                System.out.println("running");
                Vec3 vec3 = new Vec3((boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F, (boundroidEntity.getRandom().nextFloat() - 0.5) * 0.3F + boundroidEntity.getRandom().nextInt(-2,3), 2 * 0.5F + 2 * 0.5F * boundroidEntity.getRandom().nextFloat()).yRot((float) ((i / 10F) * Math.PI * 2)).add(boundroidEntity.position());
                boundroidEntity.level().addParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get(), vec3.x, vec3.y, vec3.z, boundroidEntity.getX(), boundroidEntity.getY(), boundroidEntity.getZ());
            }
        }


        if (ACExemplifiedConfig.BOUNDED_MAGNETISM_ENABLED) {

            LivingEntity target = boundroidEntity.getTarget();
            BoundroidWinchEntity boundroidWinch = (BoundroidWinchEntity) boundroidEntity.getWinch();
            if (alexsCavesExemplified$isMagnetizing() && target != null && MagnetUtil.isPulledByMagnets(target) && MagnetUtil.isPulledByMagnets(target) && boundroidWinch.distanceTo(target) < 3 && boundroidWinch.isLatched()) {
                activateDelay = 100;
                alexsCavesExemplified$setMagnetizing(false);
                stopSlammingFor = 110;
            }
            if (activateDelay > 0 && !alexsCavesExemplified$isMagnetizing()) {
                activateDelay--;
            }
            if (activateDelay <= 0 && !alexsCavesExemplified$isMagnetizing() && !boundroidEntity.level().isClientSide()) {
                alexsCavesExemplified$setMagnetizing(true);
            }


            for (Entity entity1 : boundroidEntity.level().getEntitiesOfClass(Entity.class, boundroidEntity.getBoundingBox().inflate(2.2))) {
                //Copy of how AC handles magnetism
                Vec3 pull = boundroidEntity.position().subtract(0, 1.8, 0).subtract(entity1.position());
                if (pull.length() > 1F) {
                    pull = pull.normalize();
                }
                if (MagnetUtil.isPulledByMagnets(entity1)) {
                    float strength = 0.32F;
                    if (Math.abs(pull.x) > Math.abs(pull.y) && Math.abs(pull.x) > Math.abs(pull.z)) {
                        pull = new Vec3(pull.x, 0, 0);
                    }
                    if (Math.abs(pull.y) > Math.abs(pull.x) && Math.abs(pull.y) > Math.abs(pull.z)) {
                        pull = new Vec3(0, pull.y, 0);
                    }
                    if (Math.abs(pull.z) > Math.abs(pull.x) && Math.abs(pull.z) > Math.abs(pull.y)) {
                        pull = new Vec3(0, 0, pull.z);
                    }
                    entity1.fallDistance = 0.0F;
                    if (!MagnetUtil.isEntityOnMovingMetal(entity1) && alexsCavesExemplified$isMagnetizing()) {
                        if (entity1 == boundroidEntity.getTarget()) {
                            stopSlammingFor = 30 + random.nextInt(20);
                        }
                        Vec3 center = entity1.position();
                        entity1.level().addParticle(ACParticleRegistry.SCARLET_MAGNETIC_ORBIT.get(), center.x, center.y + 1, center.z, center.x, center.y, center.z);
                        entity1.setDeltaMovement(entity1.getDeltaMovement().add(strength * pull.x, strength * pull.y, strength * pull.z));

                    }
                }
            }
        }
    }



    public boolean alexsCavesExemplified$isMagnetizing() {
        return this.entityData.get(MAGNETIZING);
    }

    @Unique
    public void alexsCavesExemplified$setMagnetizing(boolean val){
        this.entityData.set(MAGNETIZING, Boolean.valueOf(val));
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$define(CallbackInfo ci) {
        this.entityData.define(MAGNETIZING, true);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$add(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("Magnetizing", this.alexsCavesExemplified$isMagnetizing());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$read(CompoundTag compound, CallbackInfo ci) {
        this.alexsCavesExemplified$setMagnetizing(compound.getBoolean("Magnetizing"));
    }




}
