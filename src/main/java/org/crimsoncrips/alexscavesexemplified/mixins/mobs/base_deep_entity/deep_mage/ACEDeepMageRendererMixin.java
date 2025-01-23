package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity.deep_mage;

import com.github.alexmodguy.alexscaves.client.model.DeepOneMageModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.DeepOneMageRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneMageEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import org.crimsoncrips.alexscavesexemplified.ACEReflectionUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.DeepOneMageRenderer$LayerGlow")
public abstract class ACEDeepMageRendererMixin extends RenderLayer<DeepOneMageEntity, DeepOneMageModel> {

    @Shadow @Final private DeepOneMageRenderer this$0;
   private static final ResourceLocation TEXTURE_WILTED = new ResourceLocation("alexscavesexemplified:textures/entity/base_deep_one/wilted_deep_mage.png");

    public ACEDeepMageRendererMixin(RenderLayerParent<DeepOneMageEntity, DeepOneMageModel> pRenderer) {
        super(pRenderer);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DeepOneMageEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            DeepOneMageRenderer deepOneMageRenderer = this$0;
            boolean sepia = (boolean) ACEReflectionUtil.getField(deepOneMageRenderer, "sepia");
            boolean wilted = !entity.hasEffect(MobEffects.WATER_BREATHING) && (!entity.level().getBiome(entity.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !entity.isInWaterRainOrBubble());
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(sepia ? ACRenderTypes.getBookWidget(wilted ? TEXTURE_WILTED : DeepOneMageRenderer.TEXTURE, true) : ACRenderTypes.getGhostly(wilted ? TEXTURE_WILTED : DeepOneMageRenderer.TEXTURE));
            float alpha = 1.0F;
            ((DeepOneMageModel)this.getParentModel()).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }

    }


}
