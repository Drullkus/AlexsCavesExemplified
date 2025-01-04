package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.LicowitchEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LicowitchEntity.class)
public abstract class ACELicowitchMixin extends Monster {

    protected ACELicowitchMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        if (ACExemplifiedConfig.LICOWITCH_VENGEANCE_ENABLED) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Villager.class, true, false));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, IronGolem.class, true, false));
        }

    }
}
