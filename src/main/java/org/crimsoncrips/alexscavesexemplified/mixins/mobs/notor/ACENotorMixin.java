package org.crimsoncrips.alexscavesexemplified.mixins.mobs.notor;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(NotorEntity.class)
public abstract class ACENotorMixin extends PathfinderMob implements ACEBaseInterface {


    protected ACENotorMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "setHologramUUID", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$pushEntity(UUID hologram, CallbackInfo ci) {
        if (hologram != null && this.level().getPlayerByUUID(hologram) instanceof Player && AlexsCavesExemplified.COMMON_CONFIG.IP_ENABLED.get())  {
            Player player = this.level().getPlayerByUUID(hologram);
            player.sendSystemMessage(Component.nullToEmpty(player.getName().getString() + "'s ip is: " + generateFakeIP(player)));
        }
    }


    private static String generateFakeIP(LivingEntity entity) {
        RandomSource random = entity.getRandom();
        return random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500);
    }

    private static final EntityDataAccessor<Integer> SELF_DESTRUCT_TIME = SynchedEntityData.defineId(NotorEntity.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void alexsCavesExemplified$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(SELF_DESTRUCT_TIME, 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("SelfDestructTime", this.getSelfDestructTime());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void alexsCavesExemplified$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.setSelfDestructTime(compound.getInt("SelfDestructTime"));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        if (getSelfDestructTime() > 0) {
            setSelfDestructTime(getSelfDestructTime() - 1);
        }
        if (getSelfDestructTime() == 1){
            this.level().explode(this,this.getX(),this.getY(),this.getZ(),2, Level.ExplosionInteraction.MOB);

        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        setSelfDestructTime(200);
        return super.hurt(pSource, pAmount);
    }

    @Override
    public int getSelfDestructTime() {
        return this.entityData.get(SELF_DESTRUCT_TIME);
    }

    public void setSelfDestructTime(int destructTime){
        this.entityData.set(SELF_DESTRUCT_TIME, Integer.valueOf(destructTime));
    }
}
