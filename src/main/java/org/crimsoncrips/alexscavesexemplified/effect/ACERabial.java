package org.crimsoncrips.alexscavesexemplified.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.ACEDamageTypes;

import java.util.UUID;

public class ACERabial extends MobEffect {

    public ACERabial() {
        super(MobEffectCategory.HARMFUL, 0Xe6b0ac);
        
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, UUID.randomUUID().toString(), -0.1000000596046448, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, UUID.randomUUID().toString(), -1, AttributeModifier.Operation.ADDITION);

    }

    public String getDescriptionId() {
        if (ACExemplifiedConfig.RABIES_ENABLED) {
            return "alexscavesexemplified.potion.rabial";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }

    private int lastDuration = -1;

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (lastDuration <= 1) {
            int rabialLevel = amplifier + 1;
            entity.hurt(ACEDamageTypes.causeEndRabialDamage(entity.level().registryAccess()), rabialLevel * 10);
        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0;
    }


}