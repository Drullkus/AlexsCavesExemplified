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
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WatcherEntity.class)
public abstract class ACEWatcher extends Monster {


    protected ACEWatcher(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }




    @WrapOperation(method = "attemptPossession", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/WatcherEntity;canPossessTargetEntity(Lnet/minecraft/world/entity/Entity;)Z"),remap = false)
    private boolean bypassExpensiveCalculationIfNecessary(WatcherEntity instance, Entity playerData, Operation<Boolean> original) {
        if (ACExemplifiedConfig.STABILIZER_COMPATIBILITY_ENABLED && ModList.get().isLoaded("alexsmobsinteraction") && playerData instanceof Player player && player.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(AMIEnchantmentRegistry.STABILIZER.get()) > 0){
           return false;
        } else return original.call(instance,playerData);





    }
}
