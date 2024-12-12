package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.crimsoncrips.alexsmobsinteraction.config.AMInteractionConfig;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(Creeper.class)
public abstract class ACECreeper extends Entity {


    public ACECreeper(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyVariable(method = "explodeCreeper", at = @At(value = "STORE"), ordinal = 0)
    private float modifyPotion(float original) {
        Creeper creeper = (Creeper)(Object)this;
        return ACExemplifiedConfig.IRRADIATED_CREEPER_ENABLED ? (float) (original * (creeper.hasEffect(ACEffectRegistry.IRRADIATED.get()) ? 1.3 * Objects.requireNonNull(creeper.getEffect(ACEffectRegistry.IRRADIATED.get())).getAmplifier() + 1 : 1)) : original;
    }


}
