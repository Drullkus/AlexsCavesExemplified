package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.DinosaurEggBlock;
import com.github.alexmodguy.alexscaves.server.entity.item.GuanoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.ACEReflectionUtil;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DinosaurEggBlock.class)
public class ACEDinosaurEggBlock {


    @Shadow @Final private RegistryObject<EntityType> births;

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void playerBreak(Level worldIn, Player player, BlockPos pos, BlockState state, BlockEntity te, ItemStack stack, CallbackInfo ci) {
        if (!worldIn.isClientSide && ACExemplifiedConfig.DINOSAUR_EGG_ANGER_ENABLED) {
            AABB bb = (new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).inflate(10.0F, 10.0F, 10.0F);
            if (player.isCreative())
                return;

            for(Mob living : worldIn.getEntitiesOfClass(Mob.class, bb, (livingx) -> livingx.isAlive() && livingx.getType() == this.births.get())) {
                if (!(living instanceof TamableAnimal) || !((TamableAnimal)living).isTame() || !((TamableAnimal)living).isOwnedBy(player)) {
                    living.setTarget(player);
                }
            }
        }
    }


}
