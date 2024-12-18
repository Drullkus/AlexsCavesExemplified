package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs.ACECockroach;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.alexsmobs.client.model.ModelCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem.*;


@Mixin(ModelCockroach.class)
public abstract class ACEModelCockroach extends AdvancedEntityModel<EntityCockroach> {

    //Props to Drullkus for assistance


    @Inject(method = "setupAnim(Lcom/github/alexthe666/alexsmobs/entity/EntityCockroach;FFFFF)V", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/client/model/ModelCockroach;resetToDefaultPose()V"),cancellable = true,remap = false)
    private void tick(EntityCockroach entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (ACExemplifiedConfig.PRESERVED_AMBER_ENABLED && entity.isNoAi() && ModList.get().isLoaded("alexsmobs")) ci.cancel();
    }


}
