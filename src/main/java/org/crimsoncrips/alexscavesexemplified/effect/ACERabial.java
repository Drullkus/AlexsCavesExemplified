package org.crimsoncrips.alexscavesexemplified.effect;

import com.crimsoncrips.alexsmobsinteraction.config.AMInteractionConfig;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACEConfigList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;

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


}