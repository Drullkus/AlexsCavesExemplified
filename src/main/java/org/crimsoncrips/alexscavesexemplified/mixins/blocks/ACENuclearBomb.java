package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(NuclearBombBlock.class)
public abstract class ACENuclearBomb extends Block {


    public ACENuclearBomb(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return (ACExemplifiedConfig.NUCLEAR_PISTONATION_ENABLED) ? PushReaction.DESTROY : PushReaction.NORMAL;
    }
}
