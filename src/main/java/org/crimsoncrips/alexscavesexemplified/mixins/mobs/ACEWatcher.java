package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.crimsoncrips.alexsmobsinteraction.enchantment.AMIEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.compat.AMICompat;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WatcherEntity.class)
public abstract class ACEWatcher {

    @WrapOperation(method = "attemptPossession", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/WatcherEntity;canPossessTargetEntity(Lnet/minecraft/world/entity/Entity;)Z"),remap = false)
    private boolean attemptPossesion(WatcherEntity instance, Entity playerData, Operation<Boolean> original) {
        boolean returning = true;
        if (ModList.get().isLoaded("alexsmobsinteraction")){
            if (ACExemplifiedConfig.STABILIZER_COMPATIBILITY_ENABLED && playerData instanceof Player player && AMICompat.watcherBoolean(player)) {
                returning = false;
            }
        }

        return returning;
    }
}
