package org.crimsoncrips.alexscavesexemplified.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.ACEDamageTypes;

import java.util.UUID;

public class ACESugarCrash extends MobEffect {

    public ACESugarCrash() {
        super(MobEffectCategory.HARMFUL, 0Xfc3df9);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.020000000, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    public String getDescriptionId() {
        if (ACExemplifiedConfig.SUGAR_CRASH_ENABLED) {
            return "alexscavesexemplified.potion.sugar_crash";
        } else {
            return "alexscavesexemplified.feature_disabled";
        }
    }

}