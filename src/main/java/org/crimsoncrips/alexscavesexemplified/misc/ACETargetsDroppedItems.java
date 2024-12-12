package org.crimsoncrips.alexscavesexemplified.misc;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public interface ACETargetsDroppedItems {

    //from Alexs Mobs//

    boolean canTargetItem(ItemStack stack);

    void onGetItem(ItemEntity e);

    default void onFindTarget(ItemEntity e){}

    default double getMaxDistToItem(){return 2.0D; }

    default void setItemFlag(boolean itemAIFlag){}

    default void peck(){}

    default void setFlying(boolean flying){}

    default boolean isFlying(){ return false; }
}
