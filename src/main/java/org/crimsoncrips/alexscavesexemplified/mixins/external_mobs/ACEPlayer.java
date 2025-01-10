package org.crimsoncrips.alexscavesexemplified.mixins.external_mobs;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.WorldEventMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.PlayerSweets;
import org.crimsoncrips.alexscavesexemplified.server.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.ACEDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class ACEPlayer extends LivingEntity implements PlayerSweets {



    @Unique
    private Item[] lastAte = new Item[2];

    private int sweets = 0;

    protected ACEPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void alexsCavesExemplified$setSweets(int sweets) {
        if (!this.level().isClientSide) {
            sweetCounter();
        }
        this.sweets = sweets;
    }

    public int alexsCavesExemplified$getSweets() {
        return sweets;
    }

    @Inject(method = "eat", at = @At(value = "HEAD"))
    private void alexsCavesExemplified$eat(Level pLevel, ItemStack pFood, CallbackInfoReturnable<ItemStack> cir) {
        if (!pLevel.isClientSide){
            if (pFood.is(ACItemRegistry.SHARPENED_CANDY_CANE.get()) && ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
                this.hurt(this.damageSources().generic(), 1);
            }


            if (pFood.is(ACItemRegistry.BIOME_TREAT.get()) && ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
                this.hurt(this.damageSources().generic(), 1);
            }
            if (pFood.is(ACExexmplifiedTagRegistry.COLD_FOOD) && ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() + 65));
            }
            if (pFood.is(ACItemRegistry.HOT_CHOCOLATE_BOTTLE.get()) && ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), getTicksFrozen() - 100));
                this.setRemainingFireTicks(60);
            }
            if (pFood.is(ACExexmplifiedTagRegistry.SWEETS)) {
                sweetCounter();
            }

            if (lastAte[0] != null) {
                lastAte[1] = lastAte[0];
            }
            lastAte[0] = pFood.getItem();
        }
    }




    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void alexsCavesExemplified$tick(CallbackInfo ci) {
        Level level = this.level();
        if (lastAte[0] != null && lastAte[1] != null && ACExemplifiedConfig.OVERTUNED_CONSUMPTION_ENABLED) {
            String[] foodItems = {"purple_soda_bottle", "frostmint"};
            String firstFood = foodItems[random.nextInt(0, 2)];
            if (lastAte[0].toString().equals(firstFood) && lastAte[1].toString().equals(foodItems[random.nextInt(0, 2)]) && !lastAte[1].toString().equals(firstFood)) {
                AlexsCaves.sendMSGToAll(new WorldEventMessage(9 , (int)this.getX(), (int)this.getY(), (int) this.getZ()));
                level.playSound(null, this.getBlockX(), this.getBlockY(), this.getBlockZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, -3.0F);
                this.hurt(ACEDamageTypes.causeStomachDamage(level.registryAccess()), 6F);

                lastAte[0] = null;
                lastAte[1] = null;
            }
        }
        if (sweets >= 9) {
            sweets = 0;
            this.hurt(ACEDamageTypes.causeSweetPunishDamage(this.level().registryAccess()), 10000);
        }
    }

    private void sweetCounter(){
        if (!ACExemplifiedConfig.SWEET_PUNISHMENT_ENABLED)
            return;
        sweets++;
        if (sweets == 8) {
            this.sendSystemMessage(Component.nullToEmpty("Thats enough candy child"));
        }
    }

}
