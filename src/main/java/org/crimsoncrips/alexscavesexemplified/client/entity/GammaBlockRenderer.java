package org.crimsoncrips.alexscavesexemplified.client.entity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.client.model.GammaBlockModel;
import org.crimsoncrips.alexscavesexemplified.server.entity.GammaBlock;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GammaBlockRenderer extends EntityRenderer<GammaBlock> {

    private static final ResourceLocation GLOW = new ResourceLocation(AlexsCavesExemplified.MODID, "textures/entity/gamma_block_glow.png");

    private static final ResourceLocation TEXTURE = new ResourceLocation(AlexsCavesExemplified.MODID, "textures/entity/gamma_block.png");
    private static final GammaBlockModel MODEL = new GammaBlockModel();

    public GammaBlockRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(GammaBlock entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        float scale = entityIn.getLerpedScale(partialTicks) == 0 ? 1.0F : entityIn.getLerpedScale(partialTicks);
        float glowAmount = scale / Math.max(entityIn.getMaxScale(), 1.0F);
        poseStack.pushPose();
        poseStack.translate(0, 0.25F, 0F);
        poseStack.scale(scale, scale, scale);
        MODEL.setupAnim(entityIn, 0.0F, 0.0F, entityIn.tickCount + partialTicks, 0.0F, 0.0F);
        VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        MODEL.renderToBuffer(poseStack, ivertexbuilder1, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE));
        MODEL.renderToBuffer(poseStack, ivertexbuilder2, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, glowAmount);
        poseStack.popPose();
        renderGlow(entityIn,poseStack,bufferIn,partialTicks,1,1);
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    private void renderGlow(GammaBlock entity, PoseStack poseStack, MultiBufferSource source, float partialTicks, float width, float length) {
        poseStack.pushPose();
        float startAlpha = 1.0F;
        float endAlpha = 0.0F;

        int vertices = 5;
        VertexConsumer vertexconsumer = source.getBuffer(ACRenderTypes.getTremorzillaBeam(GLOW, false));
        width += 0.25F;
        float speed = 1F;

        float v = ((float) entity.tickCount + partialTicks) * -0.25F * speed;
        float v1 = v + length * 0.15F;
        float f4 = -width;
        float f5 = 0;
        float f6 = 0.0F;
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        for (int j = 0; j <= vertices; ++j) {
            Matrix3f matrix3f = posestack$pose.normal();
            float f7 = Mth.cos((float) Math.PI + (float) j * ((float) Math.PI * 2F) / (float) vertices) * width;
            float f8 = Mth.sin((float) Math.PI + (float) j * ((float) Math.PI * 2F) / (float) vertices) * width;
            float f9 = (float) j + 1;
            vertexconsumer.vertex(matrix4f, (float) (f4 * entity.level().random.nextDouble()), f5 * 0.55F, 0.0F).color(1.0F, 1.0F, 1.0F, startAlpha).uv(f6, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f4, f5, length).color(1.0F, 1.0F, 1.0F, endAlpha).uv(f6, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, f7, f8, length).color(1.0F, 1.0F, 1.0F, endAlpha).uv(f9, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) (f7 * entity.level().random.nextDouble()), f8 * 0.55F, 0.0F).color(1.0F, 1.0F, 1.0F, startAlpha).uv(f9, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }
        poseStack.popPose();
    }
    public ResourceLocation getTextureLocation(GammaBlock entity) {
        return TEXTURE;
    }
}
