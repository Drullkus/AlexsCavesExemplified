package org.crimsoncrips.alexscavesexemplified.client.render.layer;

import com.github.alexmodguy.alexscaves.client.model.UnderzealotModel;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.ACEReflectionUtil;

public class UnderzealotDesolateLayer extends RenderLayer<UnderzealotEntity, UnderzealotModel> {
    public UnderzealotDesolateLayer(RenderLayerParent<UnderzealotEntity, UnderzealotModel> p_117346_) {
        super(p_117346_);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, UnderzealotEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }

//    @Override
//    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, UnderzealotEntity entitylivingbaseIn, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
//        ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.MAINHAND);
//        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
//
//        matrixStackIn.pushPose();
//
//        translateToHand(matrixStackIn);
////        matrixStackIn.translate(0.1F, 1.45F, -0.1F);
//
//        matrixStackIn.mulPose(Axis.YP.rotationDegrees(90F));
//        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-180F));
//        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-40F));
//
//        renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, f, matrixStackIn, bufferIn, packedLightIn);
//        matrixStackIn.popPose();
//    }
//
//
//    protected void translateToHand(PoseStack matrixStack) {
//        AdvancedModelBox larm = (AdvancedModelBox)ACEReflectionUtil.getField(this.getParentModel(), "larm");
//        larm.translateAndRotate(matrixStack);
//    }

}
