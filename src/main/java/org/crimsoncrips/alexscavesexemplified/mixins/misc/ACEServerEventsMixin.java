package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.event.CommonEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CommonEvents.class)
public abstract class ACEServerEventsMixin  {

    @Inject(method = "checkAndDestroyExploitItem", at = @At(value = "HEAD"),remap = false)
    private static void alexsCavesExemplified$checkAndDestroyExploitItem(Player player, EquipmentSlot slot, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.LOCATABLE_CAVES_ENABLED.get()){
            ci.cancel();
        }
    }


}
