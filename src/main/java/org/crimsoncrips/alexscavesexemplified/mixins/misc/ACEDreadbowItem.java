package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.item.DreadbowItem;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.DarknessIncarnateEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.alexmodguy.alexscaves.server.item.DreadbowItem.*;


@Mixin(DreadbowItem.class)
public abstract class ACEDreadbowItem extends ProjectileWeaponItem {

    public ACEDreadbowItem(Properties pProperties) {
        super(pProperties);
    }



    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1) {
        if (livingEntity instanceof Player player) {
            if (itemStack.getEnchantmentLevel( ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) <= 0) {
                int i = this.getUseDuration(itemStack) - i1;
                float f = getPowerForTime(i, itemStack);
                boolean precise = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.PRECISE_VOLLEY.get()) > 0;
                boolean respite = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.SHADED_RESPITE.get()) > 0 && !DarknessIncarnateEffect.isInLight(player, 11);
                boolean perfectShot = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.TWILIGHT_PERFECTION.get()) > 0 && getPerfectShotTicks(itemStack) > 0;
                if (f > 0.1) {
                    player.playSound( ACSoundRegistry.DREADBOW_RELEASE.get());
                    ItemStack ammoStack = player.getProjectile(itemStack);
                    if (respite && ammoStack.isEmpty()) {
                        ammoStack = new ItemStack(Items.ARROW);
                    }

                    AbstractArrow abstractArrow = this.createArrow(player, itemStack, ammoStack);
                    if (abstractArrow != null) {
                        float maxDist = 128.0F * f;
                        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, maxDist);
                        if (realHitResult.getType() == HitResult.Type.MISS) {
                            realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, (f * 42.0F));
                        }

                        BlockPos mutableSkyPos = new BlockPos.MutableBlockPos(realHitResult.getLocation().x, realHitResult.getLocation().y + 1.5, realHitResult.getLocation().z);
                        int maxFallHeight = 15;

                        for(int k = 0; (mutableSkyPos).getY() < level.getMaxBuildHeight() && level.isEmptyBlock(mutableSkyPos) && k < maxFallHeight; ++k) {
                            mutableSkyPos = (mutableSkyPos).above();
                        }

                        boolean darkArrows = isConvertibleArrow(abstractArrow);
                        int maxArrows = darkArrows ? 30 : 8;
                        (abstractArrow).pickup = AbstractArrow.Pickup.ALLOWED;

                        for(int j = 0; j < Math.ceil(maxArrows * f); ++j) {
                            int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
                            double power = 1;
                            int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
                            boolean flaming = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) == 1;
                            if (ACExemplifiedConfig.VANILLA_ADAPTIONS_ENABLED){
                                if (powerLevel > 0){
                                    power = (powerLevel * 0.5 + 0.5) * 0.5;
                                }
                            }

                            if (darkArrows) {
                                DarkArrowEntity darkArrowEntity = new DarkArrowEntity(level, livingEntity);
                                darkArrowEntity.setShadowArrowDamage(precise ? 2.0F : (float) (3.0F * power));
                                darkArrowEntity.setKnockback((punch < 0) ? darkArrowEntity.getKnockback() : punch);
                                if (flaming) darkArrowEntity.setSecondsOnFire(100);
                                darkArrowEntity.setPerfectShot(perfectShot);
                                abstractArrow = darkArrowEntity;
                            } else if (perfectShot) {
                                abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() * 2.0);
                            }

                            Vec3 vec3 = (mutableSkyPos).getCenter().add((level.random.nextFloat() * 16.0F - 8.0F), (level.random.nextFloat() * 4.0F - 2.0F), (level.random.nextFloat() * 16.0F - 8.0F));

                            for(int clearTries = 0; clearTries < 6 && !level.isEmptyBlock(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty(); vec3 = (mutableSkyPos).getCenter().add((level.random.nextFloat() * 16.0F - 8.0F), (level.random.nextFloat() * 4.0F - 2.0F), (level.random.nextFloat() * 16.0F - 8.0F))) {
                                ++clearTries;
                            }

                            if (!level.isEmptyBlock(BlockPos.containing(vec3)) && level.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                                vec3 = (mutableSkyPos).getCenter();
                            }

                            (abstractArrow).setPos(vec3);
                            Vec3 vec31 = realHitResult.getLocation().subtract(vec3);
                            float randomness = precise ? 0.0F : (darkArrows ? 20.0F : 5.0F) + level.random.nextFloat() * 10.0F;
                            if (!precise && level.random.nextFloat() < 0.25F) {
                                randomness = level.random.nextFloat();
                            }

                            (abstractArrow).shoot(vec31.x, vec31.y, vec31.z, 0.5F + 1.5F * level.random.nextFloat(), randomness);
                            level.addFreshEntity(abstractArrow);
                            abstractArrow = this.createArrow(player, itemStack, ammoStack);
                            (abstractArrow).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            level.addParticle(ACParticleRegistry.UNDERZEALOT_EXPLOSION.get(), vec3.x, vec3.y, vec3.z, 0, 0, 0);
                        }

                        if (darkArrows) {
                            Vec3 vec3 = realHitResult.getLocation();
                            level.playSound((Player)null, vec3.x, vec3.y, vec3.z, ACSoundRegistry.DREADBOW_RAIN.get(), SoundSource.PLAYERS, 12.0F, 1.0F);
                        }

                        if (!player.isCreative()) {
                            if (!respite) {
                                itemStack.hurtAndBreak(1, player, (player1) -> {
                                    player1.broadcastBreakEvent(player1.getUsedItemHand());
                                });
                            }

                            if (!respite || !ammoStack.is(Items.ARROW)) {
                                ammoStack.shrink(1);
                            }
                        }
                    }
                }
            }
        }

    }

    public void onUseTick(Level level, LivingEntity living, ItemStack itemStack, int timeUsing) {
        super.onUseTick(level, living, itemStack, timeUsing);
        if (living instanceof Player player) {
            int delay = (ACExemplifiedConfig.RATATATATATA_ENABLED) ? 1 : 3;
            if (itemStack.getEnchantmentLevel(ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) > 0 && timeUsing % delay == 0) {
                boolean respite = itemStack.getEnchantmentLevel(ACEnchantmentRegistry.SHADED_RESPITE.get()) > 0 && !DarknessIncarnateEffect.isInLight(living, 11);
                player.playSound(ACSoundRegistry.DREADBOW_RELEASE.get());
                ItemStack ammoStack = player.getProjectile(itemStack);
                if (respite && ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }

                AbstractArrow abstractArrow = this.createArrow(player, itemStack, ammoStack);
                boolean darkArrows = isConvertibleArrow(abstractArrow);
                int maxArrows = darkArrows ? 1 + living.getRandom().nextInt(2) : 1;
                float randomness = 0.5F;

                for(int i = 0; i < maxArrows; ++i) {
                    (abstractArrow).pickup = AbstractArrow.Pickup.ALLOWED;
                    int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
                    double power = 1;
                    int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
                    boolean flaming = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) == 1;
                    if (ACExemplifiedConfig.VANILLA_ADAPTIONS_ENABLED){
                        if (powerLevel > 0){
                            power = (powerLevel * 0.5 + 0.5) * 0.5;
                        }
                    }
                    if (darkArrows) {
                        DarkArrowEntity darkArrowEntity = new DarkArrowEntity(level, living);
                        darkArrowEntity.setShadowArrowDamage((float) (2.0F * power));
                        darkArrowEntity.setKnockback((punch > 0) ? punch : abstractArrow.getKnockback());
                        if (flaming) darkArrowEntity.setSecondsOnFire(100);
                        abstractArrow = darkArrowEntity;
                    }

                    (abstractArrow).setPos((abstractArrow).position().add((level.random.nextFloat() - 0.5F), (level.random.nextFloat() - 0.5F), (level.random.nextFloat() - 0.5F)));
                    Vec3 vec3 = player.getViewVector(1.0F);
                    (abstractArrow).shoot(vec3.x, vec3.y, vec3.z, 4.0F + 3.0F * level.random.nextFloat(), randomness);
                    randomness += 2.0F;
                    level.addFreshEntity(abstractArrow);
                    abstractArrow = this.createArrow(player, itemStack, ammoStack);
                    (abstractArrow).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                if (!player.isCreative()) {
                    if (!respite) {
                        itemStack.hurtAndBreak(1, player, (player1) -> {
                            player1.broadcastBreakEvent(player1.getUsedItemHand());
                        });
                    }

                    if (!respite || !ammoStack.is(Items.ARROW)) {
                        ammoStack.shrink(1);
                    }
                }
            }
        }

    }

    private AbstractArrow createArrow(Player player, ItemStack bowStack, ItemStack ammoIn) {
        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
        double power = 1;
        int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
        boolean flaming = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) == 1;
        if (ACExemplifiedConfig.VANILLA_ADAPTIONS_ENABLED){
            if (powerLevel > 0){
                power = (powerLevel * 0.5 + 0.5) * 0.5;
            }
        }
        ItemStack ammo = ammoIn.isEmpty() ? player.getProjectile(bowStack) : ammoIn;
        ArrowItem arrowitem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow abstractArrow = arrowitem.createArrow(player.level(), ammo, player);
        abstractArrow.setBaseDamage(power);
        abstractArrow.setKnockback((punch > 0) ? punch : abstractArrow.getKnockback());
        if (flaming) abstractArrow.setSecondsOnFire(100);
        return abstractArrow;
    }

    @Inject(method = "getMaxLoadTime", at = @At("HEAD"),cancellable = true,remap = false)
    private static void getMaxLoadTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        cir.setReturnValue(loadTime(stack));
    }

    @Unique
    private static int loadTime(ItemStack stack) {
        if(stack.getEnchantmentLevel(ACEnchantmentRegistry.RELENTLESS_DARKNESS.get()) > 0){
            return ACExemplifiedConfig.RATATATATATA_ENABLED ?  1 : 5;
        }else{
            return 40 - 8 * stack.getEnchantmentLevel(ACEnchantmentRegistry.DARK_NOCK.get());
        }
    }

}
