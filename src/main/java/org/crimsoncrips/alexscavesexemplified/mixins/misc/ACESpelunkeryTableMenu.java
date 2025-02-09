package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.inventory.SpelunkeryTableMenu;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.CaveInfoItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


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
            if (AlexsCavesExemplified.COMMON_CONFIG.DECIPHERABLE_EXPERIENCE_ENABLED.get()){
                player.giveExperiencePoints(30);
            }

            this.broadcastChanges();
        });
    }

}
