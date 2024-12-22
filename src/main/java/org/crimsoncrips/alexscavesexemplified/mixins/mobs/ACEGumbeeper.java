package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GumbeeperEntity;
import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.crimsoncrips.alexscavesexemplified.compat.AMCompat.AMmob;


@Mixin(GumbeeperEntity.class)
public abstract class ACEGumbeeper extends Monster {


    protected ACEGumbeeper(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        GumbeeperEntity gumbeeper = (GumbeeperEntity)(Object)this;
        if (ACExemplifiedConfig.REGULAR_REFERENCE_ENABLED &&  ModList.get().isLoaded("alexsmobs")) {
            Class mob = AMmob(level());
            gumbeeper.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(gumbeeper, mob, 100, true, false,null));
            gumbeeper.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(gumbeeper, mob, 100, true, false,null));

        }
    }

}
