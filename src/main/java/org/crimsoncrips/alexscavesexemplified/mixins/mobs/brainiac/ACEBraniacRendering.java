package org.crimsoncrips.alexscavesexemplified.mixins.mobs.brainiac;

import com.github.alexmodguy.alexscaves.client.model.BrainiacModel;
import com.github.alexmodguy.alexscaves.client.render.entity.BrainiacRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(BrainiacRenderer.class)
public abstract class ACEBraniacRendering extends MobRenderer<BrainiacEntity, BrainiacModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/brainiac.png");
    private static final ResourceLocation POWERED_TEXTURE = new ResourceLocation("alexscavesexemplified:textures/entity/powered_brainiac.png");


    public ACEBraniacRendering(EntityRendererProvider.Context pContext, BrainiacModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(BrainiacEntity entity) {
        if (ACExemplifiedConfig.WASTE_POWERUP_ENABLED){
            return entity.getPersistentData().getBoolean("WastePowered") ? POWERED_TEXTURE : TEXTURE;
        } else {
            return TEXTURE;
        }
    }

}
