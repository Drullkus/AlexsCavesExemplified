package org.crimsoncrips.alexscavesexemplified.compat;

import biomesoplenty.api.block.BOPBlocks;
import com.github.alexthe666.alexsmobs.entity.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BOPCompat {
    public static Block getBOPBlock(boolean liquidBlock){
        return liquidBlock ? BOPBlocks.BLOOD : BOPBlocks.FLESH;
    }
}