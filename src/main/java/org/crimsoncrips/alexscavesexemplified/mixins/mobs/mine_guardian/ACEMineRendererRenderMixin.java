package org.crimsoncrips.alexscavesexemplified.mixins.mobs.mine_guardian;

import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer$LayerGlow")
public abstract class ACEMineRendererRenderMixin extends RenderLayer<MineGuardianEntity, MineGuardianModel> {

    public ACEMineRendererRenderMixin(RenderLayerParent<MineGuardianEntity, MineGuardianModel> pRenderer) {
        super(pRenderer);
    }

    private static final ResourceLocation TEXTURE_EYE = new ResourceLocation("alexscaves:textures/entity/mine_guardian_eye.png");
    private static final ResourceLocation TEXTURE_EXPLODE = new ResourceLocation("alexscaves:textures/entity/mine_guardian_explode.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_EYE = new ResourceLocation("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_eye.png");
    private static final ResourceLocation TEXTURE_NUCLEAR_EXPLODE = new ResourceLocation("alexscavesexemplified:textures/entity/mine_guardian/nuclear_guardian_explode.png");


    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, MineGuardianEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        MineGuardianXtra accesor = (MineGuardianXtra) entitylivingbaseIn;

        float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
        if (!entitylivingbaseIn.isEyeClosed() && !accesor.alexsCavesExemplified$isNoon()) {
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RenderType.eyes(accesor.alexsCavesExemplified$isNuclear() ? TEXTURE_NUCLEAR_EYE : TEXTURE_EYE));
            ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(accesor.alexsCavesExemplified$isNuclear() ? TEXTURE_NUCLEAR_EXPLODE : TEXTURE_NUCLEAR_EYE));
        ((MineGuardianModel)this.getParentModel()).renderToBuffer(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, explodeProgress);
    }







}
