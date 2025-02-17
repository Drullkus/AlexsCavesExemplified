package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemRenderer.class)
public class ACEItemRenderer  {


    @Inject(method = "render", at = @At(value = "TAIL"), remap = false)
    private void alexsCavesExemplified$tick(ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel, CallbackInfo ci) {
        pPoseStack.pushPose();

        RandomSour


        int vibrate = (pBuffer.getRandom().nextFloat() - 0.5F) * (Math.sin((double) fly.tickCount / 50) * 0.5 + 0.5) * 0.1;
        preEvent.getPoseStack().translate(vibrate,  vibrate, vibrate);
    }


}
