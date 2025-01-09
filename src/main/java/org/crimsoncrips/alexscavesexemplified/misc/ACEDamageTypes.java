package org.crimsoncrips.alexscavesexemplified.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEDamageTypes {

    public static final ResourceKey<DamageType> RABIAL_WATER = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "rabial_water"));
    public static final ResourceKey<DamageType> RABIAL_END = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "rabial_end"));
    public static final ResourceKey<DamageType> SUGAR_CRASH = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "sugar_crash"));
    public static final ResourceKey<DamageType> STOMACH_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "stomach_damage"));
    public static final ResourceKey<DamageType> DEPTH_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "depth_crush"));
    public static final ResourceKey<DamageType> CANDY_PUNISHMENT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AlexsCavesExemplified.MODID, "candy_punish"));


    public static DamageSource causeRabialWaterDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RABIAL_WATER), 1);
    }
    public static DamageSource causeCandyPunishDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(CANDY_PUNISHMENT), 1);
    }
    public static DamageSource causeEndRabialDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RABIAL_END), 1);
    }
    public static DamageSource causeSugarCrash(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(SUGAR_CRASH), 1);
    }
    public static DamageSource causeStomachDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(STOMACH_DAMAGE), 1);
    }
    public static DamageSource causeDepthDamage(RegistryAccess registryAccess) {
        return new DamageSourceRandomMessages(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(DEPTH_DAMAGE), 2);
    }

    private static class DamageSourceRandomMessages extends DamageSource {

        private int messageCount;

        public DamageSourceRandomMessages(Holder.Reference<DamageType> message, int messageCount) {
            super(message);
            this.messageCount = messageCount;
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity attacked) {
            int type = attacked.getRandom().nextInt(this.messageCount);
            String s = "death.attack." + this.getMsgId() + "_" + type;
            Entity entity = this.getDirectEntity() == null ? this.getEntity() : this.getDirectEntity();
            if (entity != null) {
                return Component.translatable(s + ".entity", attacked.getDisplayName(), entity.getDisplayName());
            }else{
                return Component.translatable(s, attacked.getDisplayName());
            }
        }
    }
}
