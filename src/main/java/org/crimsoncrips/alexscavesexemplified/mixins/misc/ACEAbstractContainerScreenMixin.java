package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Debug(export = true)
@Mixin(AbstractContainerScreen.class)
public abstract class ACEAbstractContainerScreenMixin {

    Minecraft minecraft = Minecraft.getInstance();


    @Inject(method = "renderFloatingItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void alexsMobsInteraction$renderFloatingItem(GuiGraphics pGuiGraphics, ItemStack pStack, int pX, int pY, String pText, CallbackInfo ci){
        if(magneticMove(pStack)){
            double x = 5D * Math.cos(minecraft.player.tickCount * 0.1 + 1 * 4638.361D + 164.35D);
            double y = 5D * Math.sin(-minecraft.player.tickCount * 0.1 + 1 * 1641.532D - 584.35D);
            pGuiGraphics.pose().translate(x, y, 0);
        }
    }

//    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
//    private void alexsMobsInteraction$renderSlot(GuiGraphics pGuiGraphics, Slot pSlot, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemstack){
//        if(magneticMove(itemstack)){
//            double x = 2D * Math.sin(minecraft.player.tickCount * 0.1 * itemstack.getBarWidth() * 4638.361D + 164.35D);
//            double y = 2D * Math.sin(minecraft.player.tickCount * 0.1 * itemstack.getBarWidth() * 4638.361D + 584.35D);
//            pGuiGraphics.pose().translate(x, y, 0);
//        }
//    }



    public boolean magneticMove(ItemStack itemStack){
        return itemStack.is(ACTagRegistry.MAGNETIC_ITEMS) && minecraft.player.level().getBiome(minecraft.player.blockPosition()).is(ACBiomeRegistry.MAGNETIC_CAVES) && AlexsCavesExemplified.CLIENT_CONFIG.MAGNETIC_MOVEMENT_ENABLED.get();
    }


    



}