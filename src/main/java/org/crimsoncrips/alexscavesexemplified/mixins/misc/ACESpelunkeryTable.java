package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.gui.SpelunkeryTableScreen;
import com.github.alexmodguy.alexscaves.server.inventory.SpelunkeryTableMenu;
import com.github.alexmodguy.alexscaves.server.message.SpelunkeryTableChangeMessage;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(SpelunkeryTableScreen.class)
public abstract class ACESpelunkeryTable  extends AbstractContainerScreen<SpelunkeryTableMenu> {


    @Shadow public abstract boolean hasPaper();

    @Shadow public abstract boolean hasTablet();

    @Shadow protected abstract boolean hasClickedAnyWord();

    @Shadow private int level;

    public ACESpelunkeryTable(SpelunkeryTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @ModifyConstant(method = "generateWords",constant = @Constant(intValue = 5),remap = false)
    private int modifyAmount(int amount) {
        return ACExemplifiedConfig.SPELUNKY_ATTEMPTS_AMOUNT;
    }

    @Override
    public void onClose() {
        if (ACExemplifiedConfig.REDOABLE_SPELUNKY_ENABLED) {
            super.onClose();
        } else {
            if (hasPaper() && hasTablet() && hasClickedAnyWord() && level < 3) {
                AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(false));
            }
        }
    }
}
