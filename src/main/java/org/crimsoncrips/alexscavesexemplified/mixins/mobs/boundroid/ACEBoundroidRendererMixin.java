package org.crimsoncrips.alexscavesexemplified.mixins.mobs.boundroid;

import com.github.alexmodguy.alexscaves.client.model.BoundroidModel;
import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.BoundroidRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.BoundroidMagnetism;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@Mixin(BoundroidRenderer.class)
public abstract class ACEBoundroidRendererMixin extends MobRenderer<BoundroidEntity, BoundroidModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/boundroid.png");
    private static final ResourceLocation TEXTURE_SCARED = new ResourceLocation("alexscaves:textures/entity/boundroid_scared.png");
    private static final ResourceLocation TEXTURE_DISABLED = new ResourceLocation("alexscavesexemplified:textures/entity/boundroid_disabled.png");

    public ACEBoundroidRendererMixin(EntityRendererProvider.Context pContext, BoundroidModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }


    public ResourceLocation getTextureLocation(BoundroidEntity entity) {
        return !((BoundroidMagnetism)entity).alexsCavesExemplified$isMagnetizing() ? TEXTURE_DISABLED : entity.isScared() ? TEXTURE_SCARED : TEXTURE;
    }

}
