//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TephraExplosion;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;
import org.crimsoncrips.alexscavesexemplified.misc.GammaExplosion;

public class GammaBlock extends TephraEntity {

    private int lSteps;
    private double lx;
    private double ly;
    private double lz;
    private double lyr;
    private double lxr;
    private float prevScale;
    private boolean playedSpawnSound;
    private int dieIn;
    private int clipFor;

    public GammaBlock(EntityType entityType, Level level) {
        super(entityType, level);
        this.playedSpawnSound = false;
        this.dieIn = -1;
        this.clipFor = 5;
    }

    public GammaBlock(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEEntityRegistry.GAMMA_BLOCK.get(), level);
    }

    public GammaBlock(Level level, LivingEntity shooter) {
        this(ACEEntityRegistry.GAMMA_BLOCK.get(), level);
        float f = shooter instanceof Player ? 0.3F : 0.1F;
        this.setPos(shooter.getX(), shooter.getEyeY() - (double)f, shooter.getZ());
        this.setOwner(shooter);
    }

    public void tick() {
        this.prevScale = this.getScale();
        if (!this.playedSpawnSound) {
            this.playedSpawnSound = true;
        }

        if (!this.level().isClientSide) {
            Entity arcTowards = this.getArcingTowards();
            if (arcTowards != null && this.tickCount > 3 && this.dieIn == -1 && this.distanceTo(arcTowards) > 1.5F && this.tickCount < 20) {
                Vec3 arcVec = arcTowards.position().add((double)0.0F, 0.3 * (double)arcTowards.getBbHeight(), (double)0.0F).subtract(this.position()).normalize();
                this.setDeltaMovement(this.getDeltaMovement().add(arcVec.scale((double)0.1F)));
            }

            this.setScale(Mth.approach(this.getScale(), this.getMaxScale(), 0.1F));
        } else {
            this.level().addAlwaysVisibleParticle(ACEParticleRegistry.GAMMA_PROTON.get(),getX(),getY(),getZ(),this.getId(),0,0);
        }

        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir) && !this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale((double)0.9F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add((double)0.0F, (double)-0.1F, (double)0.0F));
            }

            if (this.level().isClientSide) {
                if (this.lSteps > 0) {
                    double d5 = this.getX() + (this.lx - this.getX()) / (double)this.lSteps;
                    double d6 = this.getY() + (this.ly - this.getY()) / (double)this.lSteps;
                    double d7 = this.getZ() + (this.lz - this.getZ()) / (double)this.lSteps;
                    this.setYRot(Mth.wrapDegrees((float)this.lyr));
                    this.setXRot(this.getXRot() + (float)(this.lxr - (double)this.getXRot()) / (float)this.lSteps);
                    --this.lSteps;
                    this.setPos(d5, d6, d7);
                } else {
                    this.reapplyPosition();
                }
            } else {
                this.setPos(d0, d1, d2);
            }
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, (x$0) -> this.canHitEntity(x$0));
        if (hitresult.getType() != Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        if (this.dieIn > 0) {
            --this.dieIn;
            if (this.dieIn == 0) {
                this.discard();
            }
        }

        if (this.clipFor > 0) {
            --this.clipFor;
            this.noPhysics = true;
        } else {
            this.noPhysics = false;
        }

    }


    protected void onHitEntity(EntityHitResult hitResult) {
        if (!this.level().isClientSide && !this.ownedBy(hitResult.getEntity()) && !this.noPhysics) {
            this.explode();
        }
    }
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double)yr;
        this.lxr = (double)xr;
        this.lSteps = steps;
    }


    private void explode() {
        Explosion.BlockInteraction blockinteraction = BlockInteraction.KEEP;
        GammaExplosion explosion = new GammaExplosion(this.level(), this, this.getX(), this.getY((double)0.5F), this.getZ(), 1.0F + this.getMaxScale(),true,blockinteraction);
        explosion.explode();
        explosion.finalizeExplosion(true);
        this.discard();
    }

    protected void onHitBlock(BlockHitResult hitResult) {
        if (!this.level().isClientSide && !this.noPhysics) {
            this.explode();
        }
    }
    public float getLerpedScale(float partialTicks) {
        return this.prevScale + (this.getScale() - this.prevScale) * partialTicks;
    }

}
