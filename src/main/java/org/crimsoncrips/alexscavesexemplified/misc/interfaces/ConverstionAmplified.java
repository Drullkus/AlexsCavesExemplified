package org.crimsoncrips.alexscavesexemplified.misc.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ConverstionAmplified {

    void alexsCavesExemplified$setStack(ItemStack itemStack);

    void alexsCavesExemplified$setOvertuned(boolean val);

    boolean alexsCavesExemplified$isOvertuned();

    List<ItemEntity> alexsCavesExemplified$getItemsAtAndAbove(Level level, BlockPos pos);

}
