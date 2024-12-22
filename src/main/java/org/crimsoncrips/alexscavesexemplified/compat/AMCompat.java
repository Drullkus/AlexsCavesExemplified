package org.crimsoncrips.alexscavesexemplified.compat;

import com.crimsoncrips.alexsmobsinteraction.enchantment.AMIEnchantmentRegistry;
import com.github.alexthe666.alexsmobs.entity.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AMCompat {
    public static void amberReset(LivingEntity entity) {
        if (entity instanceof EntityCockroach roach) {
            roach.setNoAi(false);
            roach.setInvulnerable(false);
            roach.setSilent(false);
            roach.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1, false, false));
        }
        if (entity instanceof EntityFly fly) {
            fly.setNoAi(false);
            fly.setInvulnerable(false);
            fly.setSilent(false);
            fly.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 1, false, false));
        }
    }
    public static LivingEntity createAmberAM(Level level){
        if (level.random.nextBoolean()){
            EntityCockroach cockroach = AMEntityRegistry.COCKROACH.get().create(level);
            if (cockroach != null) {
                cockroach.setNoAi(true);
                cockroach.setBaby(level.random.nextDouble() < 0.5);
            }
            return cockroach;
        } else {
            EntityFly fly = AMEntityRegistry.FLY.get().create(level);
            if (fly != null) {
                fly.setNoAi(true);
                fly.setBaby(level.random.nextDouble() < 0.5);
            }
            return fly;
        }
    }
    public static Class AMmob(Level level){
        return level.random.nextBoolean() ? EntityRaccoon.class : EntityBlueJay.class;
    }
    public static boolean fly (LivingEntity livingEntity){
        return livingEntity instanceof EntityFly;
    }
}