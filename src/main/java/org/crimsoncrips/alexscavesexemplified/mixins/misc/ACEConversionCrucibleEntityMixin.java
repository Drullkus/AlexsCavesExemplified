package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ConversionCrucibleBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.ConversionCrucibleBlockEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ConverstionAmplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;


@Mixin(ConversionCrucibleBlockEntity.class)
public abstract class ACEConversionCrucibleEntityMixin extends BlockEntity implements ConverstionAmplified {


    @Shadow private ItemStack wantStack;

    private boolean overtuned;


    public ACEConversionCrucibleEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;recursivelySpreadBiomeBlocks(Ljava/util/List;Lnet/minecraft/core/BlockPos;II)V"),index = 2,remap = false)
    private static int alexsCavesExemplified$1(int maxDistance) {
        return overtuned ? 50 : maxDistance;
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/block/blockentity/ConversionCrucibleBlockEntity;recursivelySpreadBiomeBlocks(Ljava/util/List;Lnet/minecraft/core/BlockPos;II)V"),index = 3,remap = false)
    private static int alexsCavesExemplified$tick2(int distanceIn) {
        return overtuned ? 50 : distanceIn;
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 100,ordinal = 0),remap = false)
    private static int alexsCavesExemplified$tick3(int amount) {
        if (ACExemplifiedConfig.OVERDRIVED_CONVERSION_ENABLED && overtuned){
            return 300;
        }
        return amount;
    }

    public void alexsCavesExemplified$setStack(ItemStack wantStack) {
        this.wantStack = wantStack;
    }

    public void alexsCavesExemplified$setOvertuned(boolean overtuned) {
        this.overtuned = overtuned;
    }

    public boolean alexsCavesExemplified$isOvertuned() {
        return overtuned;
    }

    @Inject(method = "tick", at = @At(value = "TAIL"), remap = false)
    private static void alexsCavesExemplified$tick(Level level, BlockPos pos, BlockState state, ConversionCrucibleBlockEntity entity, CallbackInfo ci) {
        if (entity.tickCount % 5 == 0 && ACExemplifiedConfig.OVERDRIVED_CONVERSION_ENABLED) {
            for(ItemEntity item : ((ConverstionAmplified) entity).alexsCavesExemplified$getItemsAtAndAbove(level, pos)) {
                if (entity.getConvertingToBiome() == null && item.getItem().is(ACItemRegistry.RADIANT_ESSENCE.get()) && !level.isClientSide && entity.getFilledLevel() <= 0){
                    entity.setFilledLevel(1);
                    ((ConverstionAmplified) entity).alexsCavesExemplified$setStack(ACItemRegistry.BIOME_TREAT.get().getDefaultInstance());
                    item.getItem().shrink(1);
                    level.playSound(null, pos, ACSoundRegistry.CONVERSION_CRUCIBLE_ACTIVATE.get(), SoundSource.BLOCKS);
                    entity.markUpdated();
                    ((ConverstionAmplified) entity).alexsCavesExemplified$setOvertuned(true);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"), remap = false)
    private static void alexsCavesExemplified$tick5(Level level, BlockPos pos, BlockState state, ConversionCrucibleBlockEntity entity, CallbackInfo ci) {
        overtuned = false;
    }

    public List<ItemEntity> alexsCavesExemplified$getItemsAtAndAbove(Level level, BlockPos pos) {
        return (List)ConversionCrucibleBlock.getSuckShape().toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }



}
