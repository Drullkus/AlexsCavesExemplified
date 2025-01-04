package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.Gammafied;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export=true)
@Mixin(TremorzillaModel.class)
public abstract class ACETremorzillaModelMixin extends AdvancedEntityModel<TremorzillaEntity> {

    TremorzillaEntity tremorzilla;

    @Inject(method = "setupAnimForAnimation", at = @At(value = "HEAD"),remap = false)
    private void captureEntity(TremorzillaEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks, CallbackInfo ci) {
        tremorzilla = entity;
    }

    @ModifyExpressionValue(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/living/TremorzillaEntity;getBeamProgress(F)F"),remap = false)
    private float onlyFlyIfAllowed(float original) {
        Gammafied myAccessor = (Gammafied) tremorzilla;
        if (myAccessor != null) {
            return ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && myAccessor.isAnimationBeaming() ? (float) (original * 0.1) : original;
        } return original;
    }




}
