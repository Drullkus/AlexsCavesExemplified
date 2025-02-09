package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.server.enchantment.ACEEnchants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.alexmodguy.alexscaves.server.item.ResistorShieldItem.isScarlet;


@Mixin(ResistorShieldItem.class)
public class ACEResistorShield extends ShieldItem {


    public ACEResistorShield(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "onUseTick", at = @At("TAIL"))
    private void getMaxLoadTime(Level level, LivingEntity living, ItemStack stack, int timeUsing, CallbackInfo ci) {
        if (AlexsCavesExemplified.COMMON_CONFIG.RESISTOR_MAGNETISM_ENABLED.get()) {
            int i = this.getUseDuration(stack) - timeUsing;
            boolean scarlet = isScarlet(stack);
            for (ItemEntity entity : living.level().getEntitiesOfClass(ItemEntity.class, living.getBoundingBox().inflate(5, 1, 5))) {
                if (entity.distanceTo(living) <= 4 && (i >= 10 && i % 5 == 0) && (entity.getItem().is(ACTagRegistry.MAGNETIC_ITEMS) || stack.getEnchantmentLevel(ACEEnchants.ATOMIC_MAGNETISM.get()) > 0)) {
                    Vec3 arcVec = living.position().add(0, 0.65F * living.getBbHeight(), 0).subtract(entity.position());
                    if (scarlet) {
                        if (arcVec.length() > living.getBbWidth()) {
                            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.3F).add(arcVec.normalize().scale(0.7F)));
                        }
                    } else {
                        if (arcVec.length() > living.getBbWidth()) {
                            entity.setDeltaMovement(entity.getDeltaMovement().scale(-0.3F).add(arcVec.normalize().scale(-0.7F)));
                        }
                    }
                }
            }
        }
    }
}