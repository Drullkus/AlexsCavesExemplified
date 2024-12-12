//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.goals;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.google.common.base.Predicate;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class ACEPickupWasteBarrels<T extends ItemEntity> extends TargetGoal {

    //just a copy of AM's CreatureAITargetItems goal//
    protected final Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected int executionChance;
    protected boolean mustUpdate;
    protected ItemEntity targetEntity;
    private final int tickThreshold;
    private float radius;
    private BrainiacEntity brainiac;
    private int walkCooldown;


    public ACEPickupWasteBarrels(BrainiacEntity creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
        this(creature, 10, checkSight, onlyNearby, (Predicate)null, tickThreshold);
        this.radius = (float)radius;
    }

    public ACEPickupWasteBarrels(BrainiacEntity creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> targetSelector, int ticksExisted) {
        super(creature, checkSight, onlyNearby);
        this.radius = 9.0F;
        this.walkCooldown = 0;
        this.brainiac = creature;
        this.executionChance = chance;
        this.tickThreshold = ticksExisted;
        this.theNearestAttackableTargetSorter = new Sorter(creature);
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            public boolean apply(@Nullable ItemEntity item) {
                ItemStack stack = item.getItem();
                return !stack.isEmpty() && stack.is(ACBlockRegistry.WASTE_DRUM.get().asItem()) && item.tickCount > ACEPickupWasteBarrels.this.tickThreshold;
            }
        };
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.mob.isPassenger() && (!this.mob.isVehicle() || this.mob.getControllingPassenger() == null)) {
            if (brainiac.hasBarrel()) {
                return false;
            } else {
                if (!this.mustUpdate) {
                    long worldTime = this.mob.level().getGameTime() % 10L;
                    if (this.mob.getNoActionTime() >= 100 && worldTime != 0L) {
                        return false;
                    }

                    if (this.mob.getRandom().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }

                List<ItemEntity> list = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (ItemEntity)list.get(0);
                    this.mustUpdate = false;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    protected double getFollowDistance() {
        return 16.0;
    }

    protected AABB getTargetableArea(double targetDistance) {
        Vec3 renderCenter = new Vec3(this.mob.getX() + 0.5, this.mob.getY() + 0.5, this.mob.getZ() + 0.5);
        AABB aabb = new AABB((double)(-this.radius), (double)(-this.radius), (double)(-this.radius), (double)this.radius, (double)this.radius, (double)this.radius);
        return aabb.move(renderCenter);
    }

    public void start() {
        this.moveTo();
        super.start();
    }

    protected void moveTo() {
        if (this.walkCooldown > 0) {
            --this.walkCooldown;
        } else {
            this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1.0);
            this.walkCooldown = 30 + this.mob.getRandom().nextInt(40);
        }

    }

    public void stop() {
        super.stop();
        this.mob.getNavigation().stop();
        this.targetEntity = null;
    }

    public void tick() {
        super.tick();
        if (this.targetEntity != null && (this.targetEntity == null || this.targetEntity.isAlive())) {
            this.moveTo();
        } else {
            this.stop();
            this.mob.getNavigation().stop();
        }

        if (this.targetEntity != null && this.mob.hasLineOfSight(this.targetEntity) && (double)this.mob.getBbWidth() > 2.0 && this.mob.onGround()) {
            this.mob.getMoveControl().setWantedPosition(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1.0);
        }

        if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.distanceToSqr(this.targetEntity) < 3 && !brainiac.hasBarrel()) {
            brainiac.setHasBarrel(true);
            targetEntity.kill();
            this.stop();
        }

    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public boolean canContinueToUse() {
        boolean path = (double)this.mob.getBbWidth() > 2.0 || !this.mob.getNavigation().isDone();
        return path && this.targetEntity != null && this.targetEntity.isAlive();
    }

    public static record Sorter(Entity theEntity) implements Comparator<Entity> {
        public Sorter(Entity theEntity) {
            this.theEntity = theEntity;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.distanceToSqr(p_compare_1_);
            double d1 = this.theEntity.distanceToSqr(p_compare_2_);
            return Double.compare(d0, d1);
        }

        public Entity theEntity() {
            return this.theEntity;
        }
    }
}
