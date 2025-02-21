package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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


    @Inject(method = "renderHotbar", at = @At(value = "HEAD"), cancellable = true)
    private void alexsMobsInteraction$renderHotbar1(float pPartialTick, GuiGraphics pGuiGraphics, CallbackInfo ci){
        if(true){
            ci.cancel();
            Player player = this.getCameraPlayer();
            if (player != null) {
                ItemStack itemstack = player.getOffhandItem();
                HumanoidArm humanoidarm = player.getMainArm().getOpposite();
                int i = this.screenWidth / 2;
                int j = 182;
                int k = 91;
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(0.0F, 0.0F, -90.0F);
                pGuiGraphics.blit(WIDGETS_LOCATION, i - 91, this.screenHeight - 22, 0, 0, 182, 22);
                pGuiGraphics.blit(WIDGETS_LOCATION, i - 91 - 1 + player.getInventory().selected * 20, this.screenHeight - 22 - 1, 0, 22, 24, 22);
                if (!itemstack.isEmpty()) {
                    if (humanoidarm == HumanoidArm.LEFT) {
                        pGuiGraphics.blit(WIDGETS_LOCATION, i - 91 - 29, this.screenHeight - 23, 24, 22, 29, 24);
                    } else {
                        pGuiGraphics.blit(WIDGETS_LOCATION, i + 91, this.screenHeight - 23, 53, 22, 29, 24);
                    }
                }

                pGuiGraphics.pose().popPose();
                int l = 1;

                for(int i1 = 0; i1 < 9; ++i1) {
                    int j1 = i - 90 + i1 * 20 + 2;
                    int k1 = this.screenHeight - 16 - 3;


                    pGuiGraphics.pose().pushPose();
                    double movement = 3 * Math.sin(minecraft.player.tickCount * 0.05 + i1 * 8549.4451F);
                    pGuiGraphics.pose().translate(movement,movement,0);
                    this.renderSlot(pGuiGraphics, j1, k1, pPartialTick, player, player.getInventory().items.get(i1), l++);
                    pGuiGraphics.pose().popPose();
                }

                if (!itemstack.isEmpty()) {
                    int i2 = this.screenHeight - 16 - 3;
                    if (humanoidarm == HumanoidArm.LEFT) {
                        this.renderSlot(pGuiGraphics, i - 91 - 26, i2, pPartialTick, player, itemstack, l++);
                    } else {
                        this.renderSlot(pGuiGraphics, i + 91 + 10, i2, pPartialTick, player, itemstack, l++);
                    }
                }

                RenderSystem.enableBlend();
                if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                    float f = this.minecraft.player.getAttackStrengthScale(0.0F);
                    if (f < 1.0F) {
                        int j2 = this.screenHeight - 20;
                        int k2 = i + 91 + 6;
                        if (humanoidarm == HumanoidArm.RIGHT) {
                            k2 = i - 91 - 22;
                        }

                        int l1 = (int)(f * 19.0F);
                        pGuiGraphics.blit(GUI_ICONS_LOCATION, k2, j2, 0, 94, 18, 18);
                        pGuiGraphics.blit(GUI_ICONS_LOCATION, k2, j2 + 18 - l1, 18, 112 - l1, 18, l1);
                    }
                }

                RenderSystem.disableBlend();
            }
        }
    }


    public boolean magneticMove(ItemStack itemStack){
        return itemStack.is(ACTagRegistry.MAGNETIC_ITEMS) && minecraft.player.level().getBiome(minecraft.player.blockPosition()).is(ACBiomeRegistry.MAGNETIC_CAVES) && AlexsCavesExemplified.CLIENT_CONFIG.MAGNETIC_MOVEMENT_ENABLED.get();
    }


    



}