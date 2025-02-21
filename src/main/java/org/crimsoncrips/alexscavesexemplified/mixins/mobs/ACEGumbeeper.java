package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GumbeeperEntity;
import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GumbeeperEntity.class)
public abstract class ACEGumbeeper extends Monster {


    protected ACEGumbeeper(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GumbeeperEntity gumbeeper = (GumbeeperEntity)(Object)this;
        if (AlexsCavesExemplified.COMMON_CONFIG.REGULAR_REFERENCE_ENABLED.get() && ModList.get().isLoaded("alexsmobs")) {
            gumbeeper.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(gumbeeper, AMCompat.amMob(1), 100, true, false, AMCompat::gumbeeperCheck));
            gumbeeper.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(gumbeeper, AMCompat.amMob(2), 100, true, false, null));

        }
    }

}
