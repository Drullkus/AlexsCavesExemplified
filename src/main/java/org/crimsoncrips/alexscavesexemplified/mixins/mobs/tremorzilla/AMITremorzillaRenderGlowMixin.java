package org.crimsoncrips.alexscavesexemplified.mixins.mobs.tremorzilla;

import com.crimsoncrips.alexsmobsinteraction.config.AMInteractionConfig;
import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.TremorzillaModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEGammafied;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer$LayerGlow")
public abstract class AMITremorzillaRenderGlowMixin extends RenderLayer<TremorzillaEntity, TremorzillaModel> {

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_glow.png");
    private static final ResourceLocation TEXTURE_GLOW_POWERED = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_glow_powered.png");

    private static final ResourceLocation TEXTURE_RETRO_GLOW = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_retro_glow.png");
    private static final ResourceLocation TEXTURE_RETRO_GLOW_POWERED = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_retro_glow_powered.png");

    private static final ResourceLocation TEXTURE_TECTONIC_GLOW = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_glow.png");
    private static final ResourceLocation TEXTURE_TECTONIC_GLOW_POWERED = new ResourceLocation("alexscaves:textures/entity/tremorzilla/tremorzilla_tectonic_glow_powered.png");

    private static final ResourceLocation TEXTURE_GAMMA_GLOW = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_glow.png");
    private static final ResourceLocation TEXTURE_GAMMA_GLOW_POWERED = new ResourceLocation("alexscavesexemplified:textures/entity/tremorzilla/tremorzilla_gamma_glow_powered.png");

    public AMITremorzillaRenderGlowMixin(RenderLayerParent<TremorzillaEntity, TremorzillaModel> pRenderer) {
        super(pRenderer);
    }




    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, TremorzillaEntity tremorzilla, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ACEGammafied myAccessor = (ACEGammafied) tremorzilla;
        float normalAlpha = (float)Math.sin((double)(ageInTicks * 0.2F)) * 0.15F + 0.5F;
        float spikeDownAmount = tremorzilla.getClientSpikeDownAmount(partialTicks);
        VertexConsumer normalGlowConsumer = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(tremorzilla.isPowered() ? ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && myAccessor.isGamma() ? TEXTURE_GAMMA_GLOW_POWERED :tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_GLOW_POWERED : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_GLOW_POWERED : TEXTURE_GLOW_POWERED) : (ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && myAccessor.isGamma() ? TEXTURE_GAMMA_GLOW : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_GLOW : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_GLOW : TEXTURE_GLOW))));
        ((TremorzillaModel)this.getParentModel()).renderToBuffer(matrixStackIn, normalGlowConsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(tremorzilla, 0.0F), 1.0F, 1.0F, 1.0F, normalAlpha);
        if (spikeDownAmount > 0.0F) {
            VertexConsumer spikeGlowConsumer;
            if ((Boolean) AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get()) {
                PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
                spikeGlowConsumer = bufferIn.getBuffer(ACRenderTypes.getTremorzillaBeam(ACExemplifiedConfig.GAMMARATED_TREMORZILLA_ENABLED && myAccessor.isGamma() ? TEXTURE_GAMMA_GLOW_POWERED : tremorzilla.getAltSkin() == 2 ? TEXTURE_TECTONIC_GLOW_POWERED : (tremorzilla.getAltSkin() == 1 ? TEXTURE_RETRO_GLOW_POWERED : TEXTURE_GLOW_POWERED), true));
            } else {
                spikeGlowConsumer = normalGlowConsumer;
            }

            ((TremorzillaModel)this.getParentModel()).showSpikesBasedOnProgress(spikeDownAmount, 0.0F);
            ((TremorzillaModel)this.getParentModel()).renderToBuffer(matrixStackIn, spikeGlowConsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(tremorzilla, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            ((TremorzillaModel)this.getParentModel()).showAllSpikes();
        }

    }





}
