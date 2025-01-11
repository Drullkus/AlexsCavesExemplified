package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity;

import com.github.alexmodguy.alexscaves.client.model.DeepOneModel;
import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.CustomBookEntityRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.DeepOneRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.MineGuardianRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@Mixin(DeepOneRenderer.class)
public abstract class ACEDeepOneMixin extends MobRenderer<DeepOneEntity, DeepOneModel> implements CustomBookEntityRenderer {

    @Shadow private boolean sepia;

    public ACEDeepOneMixin(EntityRendererProvider.Context pContext, DeepOneModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one.png");
    private static final ResourceLocation TEXTURE_WILTED = new ResourceLocation("alexscavesexemplified:textures/entity/base_deep_one/wilted_deep_one.png");


    protected RenderType getRenderType(DeepOneEntity entity, boolean normal, boolean translucent, boolean outline) {
        boolean wilted = !entity.level().getBiome(entity.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !entity.isInWaterRainOrBubble();
        return this.sepia ? ACRenderTypes.getBookWidget(wilted ? TEXTURE_WILTED : TEXTURE, true) : super.getRenderType(entity, normal, translucent, outline);
    }

    public ResourceLocation getTextureLocation(DeepOneEntity entity) {
        boolean wilted = !entity.level().getBiome(entity.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !entity.isInWaterRainOrBubble();
        return wilted ? TEXTURE_WILTED : TEXTURE;
    }


}
