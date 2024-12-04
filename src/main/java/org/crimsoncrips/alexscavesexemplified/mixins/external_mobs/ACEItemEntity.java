package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.goals.ACEVesperTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Objects;


@Mixin(ItemEntity.class)
public abstract class ACEItemEntity extends Entity {


    @Shadow public abstract ItemStack getItem();

    public ACEItemEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        ItemStack item = this.getItem();
        //idk how this is a sin but alr//
        DyeableLeatherItem dyeableLeatherItem = new DyeableLeatherItem() {};
        if (ACExemplifiedConfig.PURPLE_LEATHERED_ENABLED) {
            Item[] leatherItems = {Items.LEATHER_BOOTS, Items.LEATHER_HELMET, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HORSE_ARMOR};
            if (item.is(leatherItems[random.nextInt(0, 5)]) && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !dyeableLeatherItem.hasCustomColor(item)) {
                dyeableLeatherItem.setColor(item, 0Xb839e6);
            }
        }
    }


}
