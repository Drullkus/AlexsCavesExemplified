package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Debug(export = true)
@Mixin(Gui.class)
public abstract class ACEGuiMixin {

    @Shadow @Final protected Minecraft minecraft;

    @Shadow @Final protected RandomSource random;


//    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"))
//    private void alexsMobsInteraction$renderHotbar(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci){
//        if(magneticMove()){
//            double movement = 30 * Math.sin(minecraft.player.tickCount * 0.2);
//            pGuiGraphics.pose().pushPose();
//            pGuiGraphics.pose().translate(movement,0,0);
//        }
//    }
//
//    @Inject(method = "renderHotbar", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V",ordinal = 2))
//    private void alexsMobsInteraction$renderHotbar1(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci){
//        if(magneticMove()){
//            pGuiGraphics.pose().popPose();
//        }
//    }

    @Shadow protected abstract Player getCameraPlayer();

    @Shadow protected int screenWidth;

    @Shadow @Final protected static ResourceLocation WIDGETS_LOCATION;

    @Shadow protected int screenHeight;

    @Shadow protected abstract void renderSlot(GuiGraphics pGuiGraphics, int pX, int pY, float pPartialTick, Player pPlayer, ItemStack pStack, int pSeed);

    @Shadow @Final protected static ResourceLocation GUI_ICONS_LOCATION;


    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IIFLnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V",ordinal = 0))
    private void alexsCavesExemplified$renderHotbar1(Gui instance, GuiGraphics pGuiGraphics, int j1, int k1, float pX, Player player, ItemStack pPartialTick, int l, Operation<Void> original,@Local(ordinal = 5) int i1) {
        if(magneticMove(player.getInventory().items.get(i1))){
            int t = minecraft.player.tickCount;
            double speed = 0.1;
            pGuiGraphics.pose().pushPose();

            //Thank you Reimnop for the giga nerd math code
            double x = -sin(speed * t) * cos(0.1 * t + i1 * 4638.361D + 164.35D) + cos(speed * t);
            double y = cos(speed * t) * cos(0.2 * t + i1 * 4638.361D + 364.35D) + sin(speed * t);
            pGuiGraphics.pose().translate(x, y, 0);
        }
        original.call(instance, pGuiGraphics, j1, k1, pX, player, pPartialTick, l);
        if(magneticMove(player.getInventory().items.get(i1))){
            pGuiGraphics.pose().popPose();
        }
    }








    public boolean magneticMove(ItemStack itemStack){
        return itemStack.is(ACTagRegistry.MAGNETIC_ITEMS) && minecraft.player.level().getBiome(minecraft.player.blockPosition()).is(ACBiomeRegistry.MAGNETIC_CAVES) && AlexsCavesExemplified.CLIENT_CONFIG.MAGNETIC_MOVEMENT_ENABLED.get();
    }


    



}