package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.DreadbowItem;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.alexmodguy.alexscaves.server.item.ACItemRegistry.PRIMORDIAL_ARMOR_MATERIAL;
import static com.github.alexmodguy.alexscaves.server.item.DreadbowItem.*;


@Mixin(ACItemRegistry.class)
public abstract class test  {
    private static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(ForgeRegistries.ITEMS, "alexscaves");;

    private static final RegistryObject<Item> TOUGH_HIDE = DEF_REG.register("tough_hide", () -> new Item(new Item.Properties()));;

    @Inject(method = "", at = @At("HEAD"),cancellable = true,remap = false)
    private static void getMaxLoadTime(CallbackInfoReturnable<Item> cir) {
        PRIMORDIAL_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemLike[]{(ItemLike)TOUGH_HIDE.get()}));

    }


}
