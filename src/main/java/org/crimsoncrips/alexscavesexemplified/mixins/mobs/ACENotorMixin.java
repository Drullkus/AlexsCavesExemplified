package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(NotorEntity.class)
public abstract class ACENotorMixin extends PathfinderMob {


    protected ACENotorMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "setHologramUUID", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$pushEntity(UUID hologram, CallbackInfo ci) {
        if (hologram != null && this.level().getPlayerByUUID(hologram) instanceof Player && AlexsCavesExemplified.COMMON_CONFIG.IP_ENABLED.get())  {
            Player player = this.level().getPlayerByUUID(hologram);
            player.sendSystemMessage(Component.nullToEmpty(player.getName().getString() + "'s ip is: " + generateFakeIP(player)));
        }
    }


    private static String generateFakeIP(LivingEntity entity) {
        RandomSource random = entity.getRandom();
        return random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500) + "." +
                random.nextInt(500);
    }
}
