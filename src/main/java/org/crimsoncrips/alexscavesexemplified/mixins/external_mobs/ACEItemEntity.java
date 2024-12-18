package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.FrostmintExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.DrinkableBottledItem;
import com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.effect.ACEEffects;
import org.crimsoncrips.alexscavesexemplified.goals.ACEVesperTarget;
import org.crimsoncrips.alexscavesexemplified.misc.ACEDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.github.alexmodguy.alexscaves.server.item.SackOfSatingItem.*;


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

        if (ACExemplifiedConfig.PURPLE_LEATHERED_ENABLED) {
            ItemStack item = this.getItem();
            //idk how this is a sin but alr//
            DyeableLeatherItem dyeableLeatherItem = new DyeableLeatherItem() {};
            Item[] leatherItems = {Items.LEATHER_BOOTS, Items.LEATHER_HELMET, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HORSE_ARMOR};
            if (item.is(leatherItems[random.nextInt(0, 5)]) && this.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get()) && !dyeableLeatherItem.hasCustomColor(item)) {
                dyeableLeatherItem.setColor(item, 0Xb839e6);
            }
        }

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

        if (ACExemplifiedConfig.DROPPED_SATING_ENABLED){
            ItemStack stack = this.getItem();
            if (stack.getItem() instanceof SackOfSatingItem){
                for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1))) {
                    ItemStack nearItemStack = itemEntity.getItem();
                    if (!nearItemStack.isEmpty() && nearItemStack.isEdible() && getOwner() instanceof Player player) {

                        if(nearItemStack.is(ACTagRegistry.EXPLODES_SACK_OF_SATING)){
                            setExploding(stack, true);
                        }

                        int foodAmount;

                        FoodProperties foodProperties = nearItemStack.getFoodProperties(player);
                        if(foodProperties != null && !nearItemStack.is(ACTagRegistry.RESTRICTED_FROM_SACK_OF_SATING)){
                            foodAmount = foodProperties.getNutrition() * nearItemStack.getCount();
                        } else foodAmount = 0;

                        setHunger(stack, getHunger(stack) + foodAmount);

                        nearItemStack.setCount(0);
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                }
            }
        }
    }

    public boolean fireImmune() {
        return this.getItem().getItem().isFireResistant() || super.fireImmune() || this.getPersistentData().getBoolean("DraggedProtection");
    }


}
