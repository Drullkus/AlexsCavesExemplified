package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;


@Mixin(Creeper.class)
public abstract class ACECreeper extends Entity {


    public ACECreeper(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyVariable(method = "explodeCreeper", at = @At(value = "STORE"), ordinal = 0)
    private float explode(float original) {
        Creeper creeper = (Creeper)(Object)this;
        return AlexsCavesExemplified.COMMON_CONFIG.IRRADIATED_CREEPER_ENABLED.get() ? (float) (original * (creeper.hasEffect(ACEffectRegistry.IRRADIATED.get()) ? 1.3 * Objects.requireNonNull(creeper.getEffect(ACEffectRegistry.IRRADIATED.get())).getAmplifier() + 1 : 1)) : original;
    }


}
