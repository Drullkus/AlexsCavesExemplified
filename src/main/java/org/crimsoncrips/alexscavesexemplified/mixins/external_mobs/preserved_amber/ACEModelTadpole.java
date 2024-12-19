package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.preserved_amber;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.TadpoleModel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.block.Block;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TadpoleModel.class)
public abstract class ACEModelTadpole<T extends Tadpole> extends AgeableListModel<T> {

    //Props to Drullkus for assistance


    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/frog/Tadpole;FFFFF)V", at = @At(value = "HEAD"),cancellable = true,remap = false)
    private void tick(T frog, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        Block block = frog.level().getBlockState(frog.blockPosition()).getBlock();
        if (ACExemplifiedConfig.PRESERVED_AMBER_ENABLED && frog.isNoAi() && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}
