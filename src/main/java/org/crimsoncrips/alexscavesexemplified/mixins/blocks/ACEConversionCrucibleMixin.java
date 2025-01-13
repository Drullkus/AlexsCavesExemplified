package org.crimsoncrips.alexscavesexemplified.mixins.blocks;

import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.BiomeTreatItem;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConverstionAmplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;


@Mixin(ConversionCrucibleBlock.class)
public abstract class ACEConversionCrucibleMixin extends BaseEntityBlock {


    protected ACEConversionCrucibleMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "use", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void alexsCavesExemplified$use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack playerItem = player.getItemInHand(handIn);
        if (worldIn.getBlockEntity(pos) instanceof ConversionCrucibleBlockEntity crucible && !player.isShiftKeyDown()) {
            if(playerItem.is(ACItemRegistry.RADIANT_ESSENCE.get())){
                if(!worldIn.isClientSide){
                    crucible.setFilledLevel(1);
                    ((ConverstionAmplified) crucible).alexsCavesExemplified$setStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                    playerItem.shrink(1);
                    worldIn.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                    crucible.markUpdated();
                    ((ConverstionAmplified) crucible).alexsCavesExemplified$setOverdrived(true);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }


}
