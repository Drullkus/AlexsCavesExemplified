package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.crimsoncrips.alexsmobsinteraction.config.AMInteractionConfig;
import com.github.alexmodguy.alexscaves.server.entity.living.GummyBearEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.GummyColors;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GummyBearEntity.class)
public abstract class ACEGummybear extends Animal {


    @Shadow private int sleepFor;

    @Shadow private int jellybeansToMake;

    @Shadow public abstract boolean isDigestiblePotion(ItemStack itemStack);

    @Shadow public abstract boolean isDigesting();


    @Shadow public abstract void setDigesting(boolean bool);

    @Shadow public abstract boolean digestEffect(Potion potion);

    @Shadow public abstract GummyColors getGummyColor();

    @Shadow public abstract boolean isBearSleeping();

    private int setVariable;

    protected ACEGummybear(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float pAmount) {

        if (ACExemplifiedConfig.JELLYBEAN_CHANGES_ENABLED){
            Entity entity = damageSource.getEntity();
            if (entity != null && isBearSleeping()) {
                this.jellybeansToMake = setVariable / 4000 - sleepFor / 4000;
                this.sleepFor = 0;

            }
        }

        return super.hurt(damageSource, pAmount);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isDigestiblePotion(itemstack) && !isDigesting()) {
            if (ACExemplifiedConfig.JELLYBEAN_CHANGES_ENABLED) {
                this.jellybeansToMake = 0;
            }
            this.setDigesting(true);
            this.digestEffect(PotionUtils.getPotion(itemstack));
            this.usePlayerItem(player, hand, itemstack);
            if (!player.getAbilities().instabuild) {
                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            }

            this.sleepFor = 24000 * (2 + this.random.nextInt(2));
            if (ACExemplifiedConfig.JELLYBEAN_CHANGES_ENABLED) {
                this.setVariable = sleepFor;
                this.jellybeansToMake = 1 + setVariable / 4000;
            } else {
                this.jellybeansToMake = this.random.nextInt(2) + 3;
            }
            this.playSound((SoundEvent)ACSoundRegistry.GUMMY_BEAR_EAT.get(), this.getSoundVolume(), this.getVoicePitch());
            return InteractionResult.SUCCESS;
        }

        if (isBearSleeping() && ACExemplifiedConfig.SWEETISH_SPEEDUP_ENABLED){
            switch (this.getGummyColor().toString()) {
                case "GREEN":
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_GREEN.get()) && this.sleepFor > 0){
                        this.sleepFor = sleepFor - 1000;
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                    return InteractionResult.SUCCESS;
                case "BLUE":
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_BLUE.get())  && this.sleepFor > 0){
                        this.sleepFor = sleepFor - 1000;
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);

                    }
                    return InteractionResult.SUCCESS;
                case "YELLOW":
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_YELLOW.get()) && this.sleepFor > 0){
                        this.sleepFor = sleepFor - 1000;
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                    return InteractionResult.SUCCESS;
                case "PINK":
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_PINK.get()) && this.sleepFor > 0){
                        this.sleepFor = sleepFor - 1000;
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                    return InteractionResult.SUCCESS;
                case "RED":
                    if (itemstack.is(ACItemRegistry.SWEETISH_FISH_RED.get()) && this.sleepFor > 0){
                        this.sleepFor = sleepFor - 1000;
                        this.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                    }
                    return InteractionResult.SUCCESS;
                default:
            }
        }

        return super.mobInteract(player, hand);
    }

    @ModifyConstant(method = "tick",constant = @Constant(intValue = 85),remap = false)
    private int modifyAmount(int amount) {
        if (ACExemplifiedConfig.JELLYBEAN_CHANGES_ENABLED){
            return 100000000;
        } else {
            return amount;
        }
    }



}
