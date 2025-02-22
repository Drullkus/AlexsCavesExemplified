package org.crimsoncrips.alexscavesexemplified.mixins.misc.projector;

import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HologramProjectorBlockEntity.class)
public abstract class ACEHologramProjectorEntityMixin extends BlockEntity implements ACEBaseInterface {


    public ACEHologramProjectorEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Unique
    private int scale = 1;


    @Inject(method = "load", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$load(CompoundTag tag, CallbackInfo ci) {
        System.out.println("Load scale is :" + tag.getInt("Scale"));
        scale = tag.getInt("Scale");
    }

    @Inject(method = "saveAdditional", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$saveAdditional(CompoundTag tag, CallbackInfo ci) {
        System.out.println("Save scale is :" + getProjectionScale());
        tag.putInt("Scale", getProjectionScale());
    }


    public int getProjectionScale() {
        return scale;
    }

    public void setProjectionScale(int val) {
        scale = val;
    }
}