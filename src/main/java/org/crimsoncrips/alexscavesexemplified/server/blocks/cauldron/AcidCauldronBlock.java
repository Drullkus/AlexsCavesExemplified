//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.blocks.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AcidCauldronBlock extends ACECauldron {


    public AcidCauldronBlock(BlockBehaviour.Properties p_153498_) {
        super(p_153498_, ACECauldronInteraction.ACID);
    }

    protected double getContentHeight(BlockState p_153500_) {
        return 0.9375D;
    }



    public void entityInside(BlockState p_153506_, Level p_153507_, BlockPos p_153508_, Entity p_153509_) {
        if (this.isEntityInsideContent(p_153506_, p_153508_, p_153509_)) {
            p_153509_.lavaHurt();
        }

    }

    public int getAnalogOutputSignal(BlockState p_153502_, Level p_153503_, BlockPos p_153504_) {
        return 3;
    }



}
