package org.crimsoncrips.alexscavesexemplified.enchantment;


import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexthe666.alexsmobs.entity.util.TendonWhipUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemPigshoes;
import com.github.alexthe666.alexsmobs.item.ItemShieldOfTheDeep;
import com.github.alexthe666.alexsmobs.item.ItemTendonWhip;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
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

        ATOMIC_MAGNETISM = DEF_REG.register("atomic_magnetism", () -> new ACEBasicEnchantment(Enchantment.Rarity.UNCOMMON, ACEnchantmentRegistry.RESISTOR_SHIELD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    }
}
