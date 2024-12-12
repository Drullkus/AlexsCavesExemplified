package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TremorsaurusEntity.class)
public abstract class ACETremorsaurus extends DinosaurEntity {

    protected ACETremorsaurus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"),remap = false)
    private void registerGoals(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        if (ACExemplifiedConfig.DINOSAUR_EGG_ANGER_ENABLED){
            tremorsaurus.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(tremorsaurus, LivingEntity.class, 100, true, false,livingEntity -> {
                return livingEntity.isHolding(Ingredient.of(ACBlockRegistry.TREMORSAURUS_EGG.get()));
            }));
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorsaurusEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"),remap = false)
    private void onStep(CallbackInfo ci) {
        TremorsaurusEntity tremorsaurus = (TremorsaurusEntity)(Object)this;
        for (LivingEntity entity : tremorsaurus.level().getEntitiesOfClass(LivingEntity.class, tremorsaurus.getBoundingBox().expandTowards(1, -2, 1))) {
            if (entity != tremorsaurus && entity.getBbHeight() <= 0.5F) {
                entity.hurt(tremorsaurus.damageSources().mobAttack(tremorsaurus), 4.0F);
            }
        }
    }
}
