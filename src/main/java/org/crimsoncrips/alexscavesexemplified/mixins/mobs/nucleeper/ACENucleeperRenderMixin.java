package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.client.model.NucleeperModel;
import com.github.alexmodguy.alexscaves.client.model.TeslaBulbModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.blockentity.TelsaBulbBlockRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.NucleeperRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.blockentity.TeslaBulbBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConverstionAmplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.TeslaCharge;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NucleeperRenderer.class)
public abstract class ACENucleeperRenderMixin extends MobRenderer<NucleeperEntity, NucleeperModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper.png");
    private static final ResourceLocation TEXTURE_RUSTED = new ResourceLocation("alexscavesexemplified:textures/entity/nucleeper/rusted_nucleeper.png");


    public ACENucleeperRenderMixin(EntityRendererProvider.Context pContext, NucleeperModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Override
    public ResourceLocation getTextureLocation(NucleeperEntity entity) {
        return ((NucleeperXtra)entity).alexsCavesExemplified$isRusted() ? TEXTURE_RUSTED : TEXTURE;
    }

    @Inject(method = "render(Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), cancellable = true)
    private void alexsCavesExemplified$render(NucleeperEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, CallbackInfo ci) {
        if (((NucleeperXtra)entityIn).alexsCavesExemplified$isDefused()){
            ci.cancel();
            poseStack.popPose();
            poseStack.popPose();
            super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        }
    }



}
