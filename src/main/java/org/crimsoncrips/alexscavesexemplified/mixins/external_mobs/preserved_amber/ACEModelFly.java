package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.preserved_amber;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexthe666.alexsmobs.client.model.ModelCockroach;
import com.github.alexthe666.alexsmobs.client.model.ModelFly;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ModelFly.class)
public abstract class ACEModelFly {

    //Props to Drullkus for assistance


    @Inject(method = "setupAnim(Lcom/github/alexthe666/alexsmobs/entity/EntityFly;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/client/model/ModelFly;walk(Lcom/github/alexthe666/citadel/client/model/AdvancedModelBox;FFZFFFF)V",ordinal = 0),cancellable = true,remap = false)
    private void tick(EntityFly fly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        Block block = fly.level().getBlockState(fly.blockPosition()).getBlock();
        if (ACExemplifiedConfig.PRESERVED_AMBER_ENABLED && fly.isNoAi() && ModList.get().isLoaded("alexsmobs") && block == ACBlockRegistry.AMBER.get()) ci.cancel();
    }


}
