package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEGammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TremorzillaModel.class)
public abstract class ACETremorzillaModel extends AdvancedEntityModel<TremorzillaEntity> {

    TremorzillaEntity tremorzilla;

    @Inject(method = "setupAnimForAnimation", at = @At(value = "HEAD"),remap = false)
    private void captureEntity(TremorzillaEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks, CallbackInfo ci) {
        tremorzilla = entity;
    }

    @ModifyVariable(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFFFF)V", at = @At(value = "STORE"), ordinal = 1,remap = false)
    private float explode(float original) {
        ACEGammafied myAccessor = (ACEGammafied) tremorzilla;
        return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && myAccessor.isAnimationBeaming() ? (float) (original * 0.1) : original;
    }




}
