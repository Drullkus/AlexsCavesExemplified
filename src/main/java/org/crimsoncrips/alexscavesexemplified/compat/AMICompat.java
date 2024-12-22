package org.crimsoncrips.alexscavesexemplified.compat;

import com.crimsoncrips.alexsmobsinteraction.enchantment.AMIEnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class AMICompat {
    public static boolean watcherBoolean(Player entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(AMIEnchantmentRegistry.STABILIZER.get()) > 0;
    }
}