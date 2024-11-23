package org.crimsoncrips.alexscavesexemplified.event;

import com.crimsoncrips.alexsmobsinteraction.config.AMInteractionConfig;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.GeothermalVentBlock;
import com.github.alexmodguy.alexscaves.server.block.PottedFlytrapBlock;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GumbeeperEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.ACExexmplifiedTagRegistry;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.datagen.ACELootModifierProvider;

import java.util.Objects;


@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ACExemplifiedEvents {

    @SubscribeEvent
    public void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        final var entity = event.getEntity();

        if (entity instanceof GumbeeperEntity gumbeeper){
            if (entity.getRandom().nextDouble() < ACExemplifiedConfig.CHARGED_CAVE_CREEPER_CHANCE){
                gumbeeper.setCharged(true);
            }
        }
        if (entity instanceof NucleeperEntity nucleeper){
            if (entity.getRandom().nextDouble() < ACExemplifiedConfig.CHARGED_CAVE_CREEPER_CHANCE){
                nucleeper.setCharged(true);
            }
        }

    }


    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockState = event.getEntity().level().getBlockState(event.getPos());
        BlockPos pos = event.getPos();
        Level worldIn = event.getLevel();
        RandomSource random = event.getEntity().getRandom();
        LivingEntity livingEntity = event.getEntity();


        if (ACExemplifiedConfig.GLUTTONY_ENABLED) {
            if (blockState.is(ACExexmplifiedTagRegistry.CONSUMABLE_BLOCKS)) {
                ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, blockState);

                if (livingEntity instanceof Player player && player.isCrouching()) {
                    MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
                    if (hunger != null) {
                        if (!hunger.isInfiniteDuration()) {
                            player.removeEffect(MobEffects.HUNGER);
                            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, hunger.getDuration() - 60, hunger.getAmplifier()));
                        }
                        worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
                        player.getFoodData().eat(1, 1);
                        player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                        for (int i = 0; i <= 15; i++) {
                            Vec3 lookAngle = player.getLookAngle();
                            worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                        }
                        if (random.nextDouble() < 0.01)
                            player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 100, 0));


                    } else if (player.getFoodData().needsFood()) {
                        player.getFoodData().eat(2, 2);
                        player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                        for (int i = 0; i <= 15; i++) {
                            Vec3 lookAngle = player.getLookAngle();
                            worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                        }
                        if (random.nextDouble() < 0.04)
                            player.addEffect(new MobEffectInstance(ACEffectRegistry.SUGAR_RUSH.get(), 100, 0));

                    }
                }
            }


        }

    }

    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        RandomSource random = player.getRandom();
        if (event.getTarget() instanceof GingerbreadManEntity gingerbreadMan) {
            if (itemStack.getItem() instanceof AxeItem && ACExemplifiedConfig.GINGERBREAD_AMPUTATION_ENABLED) {
                if (gingerbreadMan.hasBothLegs()) {
                    gingerbreadMan.setLostLimb(gingerbreadMan.getRandom().nextBoolean(), false, true);
                } else if (gingerbreadMan.getRandom().nextInt(2) == 0) {
                    gingerbreadMan.setLostLimb(gingerbreadMan.getRandom().nextBoolean(), true, true);
                }
            }
        }

    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent) {
        LivingEntity died = deathEvent.getEntity();

        if (died instanceof NucleeperEntity nucleeper){
            if (deathEvent.getSource().is(DamageTypes.PLAYER_EXPLOSION) || deathEvent.getSource().is(DamageTypes.EXPLOSION) || deathEvent.getSource().is(ACDamageTypes.NUKE)){
                NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(nucleeper.level());
                explosion.copyPosition(nucleeper);
                explosion.setSize(nucleeper.isCharged() ? 1.75F : 1F);
                if(!nucleeper.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    explosion.setNoGriefing(true);
                }
                nucleeper.level().addFreshEntity(explosion);
            }
        }

    }



    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent breakEvent){
        BlockState blockState = breakEvent.getState();
        Level level = (Level) breakEvent.getLevel();
        if (ACExemplifiedConfig.BURST_OUT_ENABLED) {
            if (blockState.is(ACExexmplifiedTagRegistry.BURST_BLOCKS) && breakEvent.getLevel().getRandom().nextDouble() < 0.02) {
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    if (level.getRandom().nextBoolean()) {
                        ACEntityRegistry.UNDERZEALOT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    } else {
                        ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                    }
                }

            }
            if (blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && breakEvent.getLevel().getRandom().nextDouble() < 0.4){
                if (level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.FORLORN_HOLLOWS)){
                    ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, BlockPos.containing(breakEvent.getPos().getX(), breakEvent.getPos().getY(), breakEvent.getPos().getZ()), MobSpawnType.MOB_SUMMONED);
                }
            }
        }

    }

    @SubscribeEvent
    public void mobTickEvents(LivingEvent.LivingTickEvent livingTickEvent) {
        LivingEntity livingEntity = livingTickEvent.getEntity();
        if (ACExemplifiedConfig.FLY_TRAP_ENABLED && livingEntity instanceof EntityFly fly && ModList.get().isLoaded("alexsmobs")){
            BlockState blockState = fly.getFeetBlockState();
            BlockPos blockPos = new BlockPos(fly.getBlockX(),fly.getBlockY(),fly.getBlockZ());
            if (blockState.is(ACBlockRegistry.FLYTRAP.get())){
                 if (blockState.getValue(PottedFlytrapBlock.OPEN)){
                     livingEntity.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                     fly.captureDrops();
                     fly.kill();
                     livingEntity.level().setBlock(blockPos, blockState.setValue(PottedFlytrapBlock.OPEN, false), 2);
                 }
            }
        }

        if(ACExemplifiedConfig.IRRADIATION_WASHOFF_ENABLED){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && livingEntity.getRandom().nextDouble() < 0.05 && (livingEntity.isInWater() || livingEntity.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                livingEntity.removeEffect(irradiated.getEffect());
                livingEntity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
            }
        }

        if (true){
            BlockState blockState = livingEntity.getBlockStateOn();
            if (blockState.getBlock() instanceof GeothermalVentBlock){
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 1){
                    if(ACExemplifiedConfig.IRRADIATION_WASHOFF_ENABLED){
                        MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
                        if (irradiated != null && livingEntity.getRandom().nextDouble() < 0.05) {
                            livingEntity.removeEffect(irradiated.getEffect());
                            livingEntity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
                        }
                    }
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 2){
                    livingEntity.setSecondsOnFire(5);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 3){
                    if(!livingEntity.hasEffect(ACEffectRegistry.IRRADIATED.get())){
                        livingEntity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 400, 1));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if(ACExemplifiedConfig.IRRADIATION_WASHOFF_ENABLED && ModList.get().isLoaded("supplementaries")){
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && player.getMainHandItem().is(ModRegistry.SOAP.get()) && (player.isInWater() || player.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                player.removeEffect(irradiated.getEffect());
                player.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 1000, irradiated.getAmplifier()));

                for (int i = 0; i < 10; i++){
                    double d1 = player.getRandom().nextGaussian() * 0.02;
                    double d2 = player.getRandom().nextGaussian() * 0.02;
                    double d3 = player.getRandom().nextGaussian() * 0.02;

                    level.addParticle(ModParticles.SUDS_PARTICLE.get(), player.getX(), player.getY() + 0.5, player.getZ(), d1 * 2, d2 * 2, d3 * 2);
                }
            }
        }
    }

}

