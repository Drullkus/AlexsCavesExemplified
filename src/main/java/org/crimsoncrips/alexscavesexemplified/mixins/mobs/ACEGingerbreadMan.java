package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACEHurtByTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GingerbreadManEntity.class)
public abstract class ACEGingerbreadMan extends Monster {


    protected ACEGingerbreadMan(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GingerbreadManEntity gingerbreadMan = (GingerbreadManEntity)(Object)this;
        if (ACExemplifiedConfig.HIVE_MIND_ENABLED) gingerbreadMan.targetSelector.addGoal(1, (new ACEHurtByTargetGoal(gingerbreadMan)).setAlertOthers(new Class[0]));
    }

    @Override
    public boolean isSensitiveToWater() {
        return ACExemplifiedConfig.GINGER_DISINTEGRATE_ENABLED;
    }


}
