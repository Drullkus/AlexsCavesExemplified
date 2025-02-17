package org.crimsoncrips.alexscavesexemplified.misc;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.CreateCompat;

import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.getEntityMagneticDelta;
import static com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil.setEntityMagneticDelta;

public class ACEUtils {
    public static void dropMagneticItem(Player player, ItemStack item){
        ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getEyeY(), player.getZ(), item);
        itementity.setPickUpDelay(60);
        itementity.setDeltaMovement(Vec3.ZERO);
        player.level().addFreshEntity(itementity);
        setEntityMagneticDelta(itementity,getEntityMagneticDelta(player));
        setEntityMagneticDelta(player,Vec3.ZERO);
    }

    public static int getDivingAmount(LivingEntity entity) {
        int i = 0;
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is((Item)ACItemRegistry.DIVING_HELMET.get())) {
            ++i;
        }
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is((Item)ACItemRegistry.DIVING_CHESTPLATE.get())) {
            ++i;
        }
        if (entity.getItemBySlot(EquipmentSlot.LEGS).is((Item)ACItemRegistry.DIVING_LEGGINGS.get())) {
            ++i;
        }
        if (entity.getItemBySlot(EquipmentSlot.FEET).is((Item)ACItemRegistry.DIVING_BOOTS.get())) {
            ++i;
        }
        i = i + CreateCompat.createDivingSuit(entity);
        return i;
    }

}
