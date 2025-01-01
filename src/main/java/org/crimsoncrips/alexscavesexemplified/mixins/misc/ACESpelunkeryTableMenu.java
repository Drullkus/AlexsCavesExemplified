package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.gui.SpelunkeryTableScreen;
import com.github.alexmodguy.alexscaves.client.gui.SpelunkeryTableWordButton;
import com.github.alexmodguy.alexscaves.server.inventory.SpelunkeryTableMenu;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.CaveInfoItem;
import com.github.alexmodguy.alexscaves.server.message.SpelunkeryTableChangeMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;


@Mixin(SpelunkeryTableMenu.class)
public abstract class ACESpelunkeryTableMenu extends AbstractContainerMenu {


    @Shadow @Final private ContainerLevelAccess access;

    @Shadow @Final private ResultContainer resultContainer;

    protected ACESpelunkeryTableMenu(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    @Inject(method = "setupResultSlot", at = @At(value = "HEAD"),cancellable = true,remap = false)
    private void drawOrb(ResourceKey<Biome> biomeResourceKey, Player player, CallbackInfo ci) {
        ci.cancel();
        this.access.execute((p_39170_, p_39171_) -> {
            ItemStack itemInFinalSlot = this.resultContainer.getItem(2);
            ItemStack itemstack = biomeResourceKey == null ? new ItemStack(Items.PAPER) : CaveInfoItem.create((Item) ACItemRegistry.CAVE_CODEX.get(), biomeResourceKey);
            if (itemInFinalSlot.isEmpty()) {
                this.resultContainer.setItem(2, itemstack);
            } else if (ItemStack.isSameItemSameTags(itemInFinalSlot, itemstack) && itemInFinalSlot.getCount() + itemstack.getCount() < itemInFinalSlot.getMaxStackSize()) {
                itemInFinalSlot.setCount(itemInFinalSlot.getCount() + itemstack.getCount());
                this.resultContainer.setItem(2, itemInFinalSlot);
            } else {
                player.drop(itemstack, true);
            }
            if (ACExemplifiedConfig.DECIPHERABLE_EXPERIENCE_ENABLED){
                player.giveExperiencePoints(30);
            }

            this.broadcastChanges();
        });
    }

}
