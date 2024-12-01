package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.living.GummyBearEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(GummyBearEntity.class)
public abstract class ACEGummybear extends Animal {


    @Shadow private int sleepFor;

    @Shadow private int jellybeansToMake;

    @Shadow public abstract boolean isDigestiblePotion(ItemStack itemStack);

    @Shadow public abstract boolean isDigesting();


    @Shadow public abstract void setDigesting(boolean bool);

    @Shadow public abstract boolean digestEffect(Potion potion);

    private int setVariable;

    int incrementVar = -1;

    protected ACEGummybear(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float pAmount) {

        if (ACExemplifiedConfig.JELLYBEAN_CHANGES_ENABLED){
            Entity entity = damageSource.getEntity();
            if (entity != null) {
                this.sleepFor = 0;
                if (incrementVar <= 0) {
                    this.jellybeansToMake = 0;
                }

                this.jellybeansToMake = 1 + jellybeansToMake + incrementVar;
            }
        }

        return super.hurt(damageSource, pAmount);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if ((setVariable - sleepFor) % 4000 == 0 && sleepFor > 0) {
            incrementVar++;
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isDigestiblePotion(itemstack) && !this.isDigesting()) {
            this.setDigesting(true);
            this.digestEffect(PotionUtils.getPotion(itemstack));
            this.usePlayerItem(player, hand, itemstack);
            if (!player.getAbilities().instabuild) {
                player.addItem(new ItemStack(Items.GLASS_BOTTLE));
            }
            this.sleepFor = 24000 * (2 + random.nextInt(2));
            this.setVariable = sleepFor;
            this.playSound(ACSoundRegistry.GUMMY_BEAR_EAT.get(), this.getSoundVolume(), this.getVoicePitch());
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int onlyFlyIfAllowed(int original) {
        if (ACExemplifiedConfig.JELLYBEAN_CHANGES_ENABLED) {
            return this.jellybeansToMake;
        } else return original;
    }


}
