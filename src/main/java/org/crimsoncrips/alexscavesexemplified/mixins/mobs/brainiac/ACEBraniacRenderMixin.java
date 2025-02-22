package org.crimsoncrips.alexscavesexemplified.mixins.mobs.brainiac;

import com.github.alexmodguy.alexscaves.client.model.BrainiacModel;
import com.github.alexmodguy.alexscaves.client.render.entity.BrainiacRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(BrainiacRenderer.class)
public abstract class ACEBraniacRenderMixin extends MobRenderer<BrainiacEntity, BrainiacModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/brainiac.png");
    private static final ResourceLocation POWERED_TEXTURE = new ResourceLocation("alexscavesexemplified:textures/entity/powered_brainiac.png");


    public ACEBraniacRenderMixin(EntityRendererProvider.Context pContext, BrainiacModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(BrainiacEntity entity) {
        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_POWERUP_ENABLED.get()){
            ACEBaseInterface accesor = (ACEBaseInterface) entity;
            return accesor.isPowered() ? POWERED_TEXTURE : TEXTURE;
        } else {
            return TEXTURE;
        }
    }

}
