package org.crimsoncrips.alexscavesexemplified.enchantment;


import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACEEnchants {


    public static final DeferredRegister<Enchantment> DEF_REG;

    public static final RegistryObject<Enchantment> ATOMIC_MAGNETISM;

    public ACEEnchants() {
    }


    static {
        DEF_REG = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "alexscavesexemplified");

        ATOMIC_MAGNETISM = DEF_REG.register("atomic_magnetism", () -> new ACEAtomicMagnetism(Enchantment.Rarity.UNCOMMON, ACEnchantmentRegistry.RESISTOR_SHIELD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    }
}
