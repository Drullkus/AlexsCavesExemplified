package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexmodguy.alexscaves.server.item.GalenaGauntletItem;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Debug(export = true)
@Mixin(GalenaGauntletItem.class)
public abstract class ACEGauntletItemMixin extends Item {


    public ACEGauntletItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean onlyFlyIfAllowed(boolean original,@Local Player player) {
        Entity itemLook = getClosestLookingAtEntityFor(player);
        return original || ((itemLook instanceof ItemEntity item && item.getItem().is(ACTagRegistry.MAGNETIC_ITEMS) || (itemLook instanceof MagneticWeaponEntity magneticWeaponEntity && magneticWeaponEntity.getController() instanceof TeletorEntity)) && AlexsCavesExemplified.COMMON_CONFIG.GALENA_GRAB_ENABLED.get());
    }

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;)I"))
    private void alexsCavesExemplified$onUseTick(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci, @Local(ordinal = 1) ItemStack otherStack, @Local boolean otherMagneticWeaponsInUse) {
        Entity itemlook = null;
        if (living instanceof Player player){
            itemlook = getClosestLookingAtEntityFor(player);
        }
        Iterator<MagneticWeaponEntity> magneticNear = level.getEntitiesOfClass(MagneticWeaponEntity.class, living.getBoundingBox().inflate(64, 64, 64)).iterator();

        if (!(magneticNear.next().getController() instanceof Player) && AlexsCavesExemplified.COMMON_CONFIG.GALENA_GRAB_ENABLED.get()) {
            if (itemlook instanceof MagneticWeaponEntity magneticWeaponEntity && magneticWeaponEntity.getController() instanceof TeletorEntity teletor){
                otherMagneticWeaponsInUse = true;
                magneticWeaponEntity.setControllerUUID(living.getUUID());
                teletor.setWeaponUUID(null);
            }
            if (itemlook instanceof ItemEntity item && grabableItems(item.getItem(),stack) && !grabableItems(otherStack,stack) && !level.isClientSide) {
                for(MagneticWeaponEntity magneticWeapon : level.getEntitiesOfClass(MagneticWeaponEntity.class, living.getBoundingBox().inflate(64, 64, 64))){
                    Entity controller = magneticWeapon.getController();
                    if(controller != null && controller.is(living)){
                        otherMagneticWeaponsInUse = true;
                        break;
                    }
                }
                if(!otherMagneticWeaponsInUse) {
                    ItemStack copy = item.getItem().copy();
                    item.discard();
                    MagneticWeaponEntity magneticWeapon = ACEntityRegistry.MAGNETIC_WEAPON.get().create(level);

                    if(magneticWeapon != null){
                        magneticWeapon.setItemStack(copy);
                        magneticWeapon.setPos(item.position().add(0, 1, 0));
                        magneticWeapon.setControllerUUID(living.getUUID());
                        level.addFreshEntity(magneticWeapon);
                    }

                }
            }
        }
    }

    private static Entity getClosestLookingAtEntityFor(Player player) {
        Entity closestValid = null;
        HitResult hitresult = ProjectileUtil.getHitResultOnViewVector(player, Entity::isAlive, 32);
        if (hitresult instanceof EntityHitResult) {
            Entity entity = ((EntityHitResult) hitresult).getEntity();
            if (!entity.equals(player) && player.hasLineOfSight(entity)) {
                closestValid = entity;
            }
        }
        return closestValid;
    }

    public boolean grabableItems(ItemStack item, ItemStack gauntlet){
        boolean crystallization = gauntlet.getEnchantmentLevel(ACEnchantmentRegistry.CRYSTALLIZATION.get()) > 0;
        return item.is(crystallization ? ACTagRegistry.GALENA_GAUNTLET_CRYSTALLIZATION_ITEMS : ACTagRegistry.MAGNETIC_ITEMS);
    }
}