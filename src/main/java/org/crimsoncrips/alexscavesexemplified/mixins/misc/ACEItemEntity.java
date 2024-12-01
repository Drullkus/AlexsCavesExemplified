package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;


@Mixin(ItemEntity.class)
public abstract class ACEItemEntity extends Entity {


    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable public abstract Entity getOwner();

    public ACEItemEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        BlockState blockState = this.getBlockStateOn();
        Level level = this.level();

        if (ACExemplifiedConfig.AMPLIFIED_FROSTMINT_ENABLED){
            if (this.getItem().is(ACItemRegistry.FROSTMINT_SPEAR.get()) || this.getItem().is(ACBlockRegistry.FROSTMINT.get().asItem())) {
                if (blockState.getFluidState().getFluidType() == ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get() && !level.isClientSide) {
                    FrostmintExplosion explosion = new FrostmintExplosion(level, this, this.getX() + 0.5F, this.getY() + 0.5F, this.getZ() + 0.5F, 4.0F, Explosion.BlockInteraction.DESTROY_WITH_DECAY, false);
                    explosion.explode();
                    explosion.finalizeExplosion(true);

                    ACAdvancementTriggerRegistry.FROSTMINT_EXPLOSION.triggerForEntity(this.getOwner());

                    this.discard();
                }
            }
        }
    }
}
