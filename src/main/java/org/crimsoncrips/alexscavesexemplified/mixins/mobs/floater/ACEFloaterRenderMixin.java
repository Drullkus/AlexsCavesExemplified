package org.crimsoncrips.alexscavesexemplified.mixins.mobs.floater;

import com.github.alexmodguy.alexscaves.client.model.FloaterModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.FloaterRenderer;
import com.github.alexmodguy.alexscaves.server.entity.item.FloaterEntity;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;


@Mixin(FloaterRenderer.class)
public class ACEFloaterRenderMixin extends EntityRenderer<FloaterEntity> {
    private static final FloaterModel FLOATER_MODEL = new FloaterModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/entity/floater.png");
    private static final ResourceLocation SUB_TEXTURE = new ResourceLocation("alexscavesexemplified", "textures/entity/sub_floater.png");

    protected ACEFloaterRenderMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(FloaterEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        float ageInTicks = (float)entity.tickCount + partialTicks;
        poseStack.pushPose();
        poseStack.translate((double)0.0F, (double)1.5F, (double)0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        FLOATER_MODEL.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
        VertexConsumer textureVertexConsumer = bufferIn.getBuffer(ACRenderTypes.getGhostly(ACExemplifiedConfig.SUBNAUTICAL_FLOATERS_ENABLED ? SUB_TEXTURE : TEXTURE));
        FLOATER_MODEL.renderToBuffer(poseStack, textureVertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }


    public ResourceLocation getTextureLocation(FloaterEntity entity) {
        return ACExemplifiedConfig.SUBNAUTICAL_FLOATERS_ENABLED ? SUB_TEXTURE : TEXTURE;
    }

}
