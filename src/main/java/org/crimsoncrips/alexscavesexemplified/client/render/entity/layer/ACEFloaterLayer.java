package org.crimsoncrips.alexscavesexemplified.client.render.entity.layer;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import com.github.alexmodguy.alexscaves.server.potion.IrradiatedEffect;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.allay.Allay;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ACEFloaterLayer extends RenderLayer {

    private static final ResourceLocation FLOATER_LAYER = new ResourceLocation("");
    private RenderLayerParent parent;


    public ACEFloaterLayer(RenderLayerParent parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Entity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity instanceof Allay allay) {

        }
    }


}
