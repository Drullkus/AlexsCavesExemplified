package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
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


@Mixin(SauropodBaseEntity.class)
public abstract class ACESauropod {

    @Inject(method = "crushBlocksInRing", at = @At("TAIL"),remap = false)
    private void crush(int width, int ringStartX, int ringStartZ, float dropChance, CallbackInfo ci) {
        SauropodBaseEntity sauropodBase = (SauropodBaseEntity)(Object)this;
        if (ACExemplifiedConfig.STOMP_DAMAGE_ENABLED){
            for (LivingEntity entity : sauropodBase.level().getEntitiesOfClass(LivingEntity.class, sauropodBase.getBoundingBox().inflate(width))) {
                if (entity != sauropodBase && entity.getBbHeight() <= 3F) {
                    entity.hurt(sauropodBase.damageSources().mobAttack(sauropodBase), 6.0F);
                }
            }
        }
    }
}
