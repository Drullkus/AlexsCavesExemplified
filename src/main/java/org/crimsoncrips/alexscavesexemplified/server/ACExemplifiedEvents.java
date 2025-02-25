package org.crimsoncrips.alexscavesexemplified.server;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.*;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.item.MeltedCaramelEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.compat.AMCompat;
import org.crimsoncrips.alexscavesexemplified.compat.CuriosCompat;
import org.crimsoncrips.alexscavesexemplified.datagen.ACEFeatures;
import org.crimsoncrips.alexscavesexemplified.misc.ACEUtils;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.ACEBaseInterface;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

import static com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity.ANIMATION_DRINK_BARREL;
import static net.minecraft.world.entity.EntityType.*;


@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ACExemplifiedEvents {

    private static final AttributeModifier FAST_FALLING = new AttributeModifier(UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA"), "Fast falling acceleration reduction", 0.1, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01

    @SubscribeEvent
    public void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        final var entity = event.getEntity();

        Double chargedChance = AlexsCavesExemplified.COMMON_CONFIG.CHARGED_CAVE_CREEPER_CHANCE.get();
        if (entity instanceof GumbeeperEntity gumbeeper){
            if (entity.getRandom().nextDouble() < chargedChance){
                gumbeeper.setCharged(true);
            }
        }
        if (entity instanceof NucleeperEntity nucleeper){
            if (entity.getRandom().nextDouble() < chargedChance){
                nucleeper.setCharged(true);
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && entity.getRandom().nextDouble() < 0.05){
            if (entity instanceof CorrodentEntity || entity instanceof UnderzealotEntity || entity instanceof VesperEntity){
                entity.addEffect(new MobEffectInstance(ACEEffects.RABIAL.get(), 140000, 0));
            }
        }

    }

    @SubscribeEvent
    public void onLevelJoin(EntityJoinLevelEvent event) {
        final var entity = event.getEntity();

        if (entity.getType().is(ACExexmplifiedTagRegistry.CAN_RABIES) && entity instanceof Mob mob && AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get()){
            mob.targetSelector.addGoal(2, new MobTarget3DGoal(mob, LivingEntity.class, false,10, livingEntity -> {
                return livingEntity.getType() != entity.getType();
            }){
                @Override
                public boolean canUse() {
                    return super.canUse() && mob.hasEffect(ACEEffects.RABIAL.get());
                }
            });
        }

    }


    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockState = event.getEntity().level().getBlockState(event.getPos());
        BlockPos pos = event.getPos();
        Level worldIn = event.getLevel();
        RandomSource random = event.getEntity().getRandom();
        Player player = event.getEntity();

        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get()) {
            if (blockState.is(ACExexmplifiedTagRegistry.CONSUMABLE_BLOCKS)) {
                ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, blockState);

                if (player.isCrouching()) {
                    MobEffectInstance hunger = player.getEffect(MobEffects.HUNGER);
                    if (hunger != null) {
                        ((ACEBaseInterface) player).addSweets(1);

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
                        ((ACEBaseInterface) player).addSweets(1);

                        player.getFoodData().eat(2, 2);
                        player.playSound(SoundEvents.GENERIC_EAT, 1F, 1F);
                        for (int i = 0; i <= 15; i++) {
                            Vec3 lookAngle = player.getLookAngle();
                            worldIn.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 1.5, lookAngle.y * 2, lookAngle.z * 1.5);
                        }
                        if (random.nextDouble() < 0.01)
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
        Level level = event.getLevel();
        Entity target = event.getTarget();

        if (itemStack.getItem() instanceof AxeItem && AlexsCavesExemplified.COMMON_CONFIG.AMPUTATION_ENABLED.get() && target instanceof GingerbreadManEntity gingerbread) {
            if (gingerbread.hasBothLegs()) {
                gingerbread.hurt(player.damageSources().mobAttack(player), 0.5F);
                gingerbread.setLostLimb(gingerbread.getRandom().nextBoolean(), false, true);
                player.swing(player.getUsedItemHand());
            } else if (gingerbread.getRandom().nextInt(2) == 0) {
                player.swing(player.getUsedItemHand());
                gingerbread.hurt(player.damageSources().mobAttack(player), 0.5F);
                gingerbread.setLostLimb(gingerbread.getRandom().nextBoolean(), true, true);
            }
        }

        if (target instanceof UnderzealotEntity underzealot && underzealot.getPassengers().isEmpty() && !underzealot.isPraying() && AlexsCavesExemplified.COMMON_CONFIG.DARK_OFFERING_ENABLED.get()) {

            for (Mob leashedEntities : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(10))) {
                boolean lasso = ModList.get().isLoaded("alexsmobs") && AMCompat.isLeashed(leashedEntities,player);
                if ((leashedEntities.getLeashHolder() == player || lasso) && leashedEntities instanceof UnderzealotSacrifice) {
                    if (lasso){
                        leashedEntities.spawnAtLocation(new ItemStack(AMItemRegistry.VINE_LASSO.get()));
                        VineLassoUtil.lassoTo(null, leashedEntities);
                    } else {
                        leashedEntities.dropLeash(true,true);
                    }
                    leashedEntities.startRiding(underzealot);

                    boolean respect = (player.getItemBySlot(EquipmentSlot.CHEST).is(ACItemRegistry.CLOAK_OF_DARKNESS.get()) && player.getItemBySlot(EquipmentSlot.HEAD).is(ACItemRegistry.HOOD_OF_DARKNESS.get()));
                    boolean happy;
                    if (AlexsCavesExemplified.COMMON_CONFIG.UNDERZEALOT_RESPECT_ENABLED.get() && respect){
                        if (level instanceof ServerLevel serverLevel){
                            ResourceLocation sacrificeLocation = new ResourceLocation(AlexsCavesExemplified.MODID, "entities/underzealot_trade");
                            LootParams ctx = new LootParams.Builder(serverLevel).withParameter(LootContextParams.THIS_ENTITY, underzealot).create(LootContextParamSets.EMPTY);
                            ObjectArrayList<ItemStack> rewards = level.getServer().getLootData().getLootTable(sacrificeLocation).getRandomItems(ctx);

                            rewards.forEach(stack -> BehaviorUtils.throwItem(underzealot, rewards.get(0), player.position().add(0.0D, 1.0D, 0.0D)));
                        }
                        happy = true;
                    } else {
                        happy = false;
                    }
                    for(int i = 0; i < 5; ++i) {
                        double d0 = underzealot.getRandom().nextGaussian() * 0.02D;
                        double d1 = underzealot.getRandom().nextGaussian() * 0.02D;
                        double d2 = underzealot.getRandom().nextGaussian() * 0.02D;
                        underzealot.level().addParticle((happy ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.ANGRY_VILLAGER), underzealot.getRandomX(1.0D), underzealot.getRandomY() + 1.0D, underzealot.getRandomZ(1.0D), d0, d1, d2);
                    }
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GLUTTONY_ENABLED.get() && player.isCrouching()) {

            if (player.getEffect(MobEffects.HUNGER) == null)
                return;
            if (target instanceof GingerbreadManEntity gingerbread && !gingerbread.isOvenSpawned()) {
                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, ACItemRegistry.GINGERBREAD_CRUMBS.get().asItem().getDefaultInstance());
                Vec3 lookAngle = player.getLookAngle();
                level.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2);


                for (GingerbreadManEntity entity : level.getEntitiesOfClass(GingerbreadManEntity.class, gingerbread.getBoundingBox().inflate(10, 5, 10))) {
                    if (!entity.isOvenSpawned() && !player.isCreative()) {
                        entity.setTarget(player);
                    }
                }
                event.getTarget().discard();
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 2);
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);

            }

            if (target instanceof CaramelCubeEntity caramelCube && caramelCube.getSlimeSize() <= 0) {
                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, ACItemRegistry.CARAMEL.get().asItem().getDefaultInstance());
                Vec3 lookAngle = player.getLookAngle();
                level.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2);

                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 3);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0,false,false));
                event.getTarget().discard();
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);

            }

            if (target instanceof GummyBearEntity gummyBearEntity && gummyBearEntity.isBaby()) {
                Vec3 lookAngle = player.getLookAngle();

                ItemStack gelatinColor = switch (gummyBearEntity.getGummyColor()) {
                    case GREEN -> ACItemRegistry.GELATIN_GREEN.get().asItem().getDefaultInstance();
                    case BLUE -> ACItemRegistry.GELATIN_BLUE.get().asItem().getDefaultInstance();
                    case YELLOW -> ACItemRegistry.GELATIN_YELLOW.get().asItem().getDefaultInstance();
                    case PINK -> ACItemRegistry.GELATIN_PINK.get().asItem().getDefaultInstance();
                    default -> ACItemRegistry.GELATIN_RED.get().asItem().getDefaultInstance();
                };

                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, gelatinColor.getItem().getDefaultInstance());

                for (int i = 0; i <= 15; i++) {
                    level.addParticle(particle, player.getX(), player.getY() + 1.5, player.getZ(), lookAngle.x * 0.2, lookAngle.y * 0.6, lookAngle.z * 0.2);
                }

                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 5);
                for (GummyBearEntity entity : level.getEntitiesOfClass(GummyBearEntity.class, player.getBoundingBox().inflate(10, 5, 10))) {
                    if (!player.isCreative()) {
                        entity.setTarget(player);
                    }
                }
                target.discard();
                player.playSound(SoundEvents.GENERIC_EAT, 1.0F, -2F);
                player.playSound(ACSoundRegistry.GUMMY_BEAR_DEATH.get(), 0.4F, 2F);

            }
        }

        if(target instanceof SeaPigEntity && AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get() && level.getBiome(target.getOnPos()).is(ACBiomeRegistry.ABYSSAL_CHASM) && player.getItemInHand(player.getUsedItemHand()).is(ACBlockRegistry.MUCK.get().asItem())){
            for (LivingEntity deepOne : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(50))) {
                if (deepOne instanceof DeepOneBaseEntity deepOneBaseEntity) {
                    deepOneBaseEntity.addReputation(player.getUUID(),1);
                }
            }
        }


        if(event.getTarget() instanceof Parrot parrot && AlexsCavesExemplified.COMMON_CONFIG.COOKIE_CRUMBLE_ENABLED.get()){
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            parrot.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
            parrot.hurt(player.damageSources().playerAttack(player), Float.MAX_VALUE);
            parrot.level().explode(player,parrot.getX(),parrot.getY(),parrot.getZ(),3, Level.ExplosionInteraction.MOB);
        }

        if (event.getTarget() instanceof CandicornEntity candicornEntity) {
            if (itemStack.is(ACItemRegistry.CARAMEL_APPLE.get()) && AlexsCavesExemplified.COMMON_CONFIG.CANDICORN_HEAL_ENABLED.get()) {
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                candicornEntity.heal(4);
                player.swing(event.getHand());
            }
        }

    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent) {
        LivingEntity died = deathEvent.getEntity();
        Entity killer = deathEvent.getSource().getEntity();
        Level level = died.level();

        if (died instanceof NucleeperEntity nucleeper && AlexsCavesExemplified.COMMON_CONFIG.NUCLEAR_CHAIN_ENABLED.get() && !((NucleeperXtra)nucleeper).alexsCavesExemplified$isDefused()){
            if (deathEvent.getSource().is(DamageTypes.PLAYER_EXPLOSION) || deathEvent.getSource().is(DamageTypes.EXPLOSION) || deathEvent.getSource().is(ACDamageTypes.NUKE) || deathEvent.getSource().is(ACDamageTypes.TREMORZILLA_BEAM)){
                NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(nucleeper.level());
                explosion.copyPosition(nucleeper);
                explosion.setSize(nucleeper.isCharged() ? 1.75F : 1F);
                if(!nucleeper.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)){
                    explosion.setNoGriefing(true);
                }
                nucleeper.level().addFreshEntity(explosion);
            }
        }

        if (died instanceof Player player && AlexsCavesExemplified.COMMON_CONFIG.MUTATED_DEATH_ENABLED.get()) {
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= 2 && player.getRandom().nextDouble() < 0.2) {
                ACEntityRegistry.BRAINIAC.get().spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
            }
        }

        if (killer instanceof Player player && AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()) {
            for (LivingEntity deepOne : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(50))) {
                if (deepOne instanceof DeepOneBaseEntity deepOneBaseEntity) {
                    if (died instanceof SeaPigEntity){
                        deepOneBaseEntity.addReputation(player.getUUID(),-1);
                    } else if (died instanceof GossamerWormEntity){
                        deepOneBaseEntity.addReputation(player.getUUID(),-2);
                    } else if (died instanceof LanternfishEntity && player.getRandom().nextDouble() < 0.3){
                        deepOneBaseEntity.addReputation(player.getUUID(),-1);
                    } else if (died instanceof TripodfishEntity){
                        deepOneBaseEntity.addReputation(player.getUUID(),-1);
                    } else if (died instanceof HullbreakerEntity){
                        deepOneBaseEntity.addReputation(player.getUUID(),-10);
                    } else if (died instanceof MineGuardianEntity){
                        deepOneBaseEntity.addReputation(player.getUUID(),2);
                    }
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.FISH_MUTATION_ENABLED.get() && died.getFeetBlockState().is(ACBlockRegistry.ACID.get()) && died.getType().is(ACExexmplifiedTagRegistry.FISH)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 1){
            ACEntityRegistry.RADGILL.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            died.discard();
        }
        if (AlexsCavesExemplified.COMMON_CONFIG.CAT_MUTATION_ENABLED.get() && died.getFeetBlockState().is(ACBlockRegistry.ACID.get()) && died.getType().is(ACExexmplifiedTagRegistry.CAT)  && !died.level().isClientSide() && died.getRandom().nextDouble() < 1){
            ACEntityRegistry.RAYCAT.get().spawn((ServerLevel) level, BlockPos.containing(died.getX(), died.getY(), died.getZ()), MobSpawnType.MOB_SUMMONED);
            died.discard();
        }

    }



    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent breakEvent){
        BlockState blockState = breakEvent.getState();
        Level level = (Level) breakEvent.getLevel();
        Player player = breakEvent.getPlayer();
        if (AlexsCavesExemplified.COMMON_CONFIG.BURST_OUT_ENABLED.get()) {
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

        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()) {
            if (blockState.is(ACExexmplifiedTagRegistry.ABYSSAL_ECOSYSTEM) && level.getBiome(breakEvent.getPos()).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
                for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(50))) {
                    if (entity instanceof DeepOneBaseEntity deepOneBaseEntity && player.getRandom().nextBoolean()) {
                        deepOneBaseEntity.addReputation(player.getUUID(),-1);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void mobBreathe(LivingBreatheEvent breatheEvent) {
        LivingEntity livingEntity = breatheEvent.getEntity();
        Level level = livingEntity.level();
        if (AlexsCavesExemplified.COMMON_CONFIG.PRIMORDIAL_OXYGEN_ENABLED.get() && livingEntity instanceof Player player && level.getBiome(player.getOnPos()).is(ACBiomeRegistry.PRIMORDIAL_CAVES)){
            breatheEvent.setConsumeAirAmount(breatheEvent.getConsumeAirAmount() + 2);
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.ABYSSAL_CRUSH_ENABLED.get() && livingEntity instanceof Player player && level.getBiome(player.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM)){
            int aboveWater = 0;
            BlockPos pos = new BlockPos(player.getBlockX(), (int) (player.getBlockY() + 2),player.getBlockZ());
            while (level.getBlockState(pos).is(Blocks.WATER)){
                pos = pos.above();
                aboveWater++;
            }

            int diving = ACEUtils.getDivingAmount(livingEntity);
            if (level.random.nextDouble() < (0.1 - (0.020 * diving)) && !(player.getVehicle() instanceof SubmarineEntity)){
                if (aboveWater > 50 && diving < 10){
                    breatheEvent.setConsumeAirAmount(breatheEvent.getConsumeAirAmount() + (int) (0.025 * (aboveWater - 40)));
                }
            }

        }


    }

    @SubscribeEvent
    public void mobTickEvents(LivingEvent.LivingTickEvent livingTickEvent){
        LivingEntity livingEntity = livingTickEvent.getEntity();
        Level level = livingEntity.level();

        if (livingEntity.getPersistentData().getBoolean("WastePowered")){
            //taken from Dreadbow's particle making
            Vec3 particlePos = livingEntity.position().add((level.random.nextFloat() - 0.5F) * 2.5F, 0F, (level.random.nextFloat() - 0.5F) * 2.5F);
            level.addParticle(ACParticleRegistry.PROTON.get(), particlePos.x, particlePos.y, particlePos.z, livingEntity.getX(), livingEntity.getY(0.5F), livingEntity.getZ());
        }



        if (livingEntity instanceof SeaPigEntity seaPigEntity && level.random.nextDouble() < 0.01 && AlexsCavesExemplified.COMMON_CONFIG.POISONOUS_SKIN_ENABLED.get()) {
            for (LivingEntity entity : seaPigEntity.level().getEntitiesOfClass(LivingEntity.class, seaPigEntity.getBoundingBox().inflate(0.5))) {
                if (entity != seaPigEntity && entity.getBbHeight() <= 3.5F && !(entity instanceof SeaPigEntity)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
                }
            }
        }

        if (livingEntity instanceof Player player && level.getBiome(player.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) && AlexsCavesExemplified.COMMON_CONFIG.ABYSSAL_CRUSH_ENABLED.get()){
            int aboveWater = 0;
            BlockPos pos = new BlockPos(player.getBlockX(), (int) (player.getBlockY() + 2),player.getBlockZ());
            while (level.getBlockState(pos).is(Blocks.WATER)){
                pos = pos.above();
                aboveWater++;
            }
            int diving = ACEUtils.getDivingAmount(livingEntity);

            if (level.random.nextDouble() < (0.1 - (0.020 * diving)) && !(player.getVehicle() instanceof SubmarineEntity)){
                if (aboveWater > 50 && diving < 10){
                    player.hurt(ACEDamageTypes.causeDepthDamage(level.registryAccess()), (float) (0.025 * (aboveWater - 40)));
                }
            }
        }


        if (livingEntity instanceof Player player) {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        BlockPos pickedBlock = new BlockPos(player.getBlockX() + x, player.getBlockY() + y , player.getBlockZ() + z);
                        BlockState blockState = level.getBlockState(pickedBlock);
                        if (AlexsCavesExemplified.COMMON_CONFIG.PEERING_TRIGGER_ENABLED.get() && blockState.is(ACBlockRegistry.PEERING_COPROLITH.get()) && (CuriosCompat.hasLight(livingEntity)) && level.random.nextDouble() < 0.1) {
                            if (player.getRandom().nextDouble() < 0.9) {
                                level.setBlock(pickedBlock, ACBlockRegistry.POROUS_COPROLITH.get().defaultBlockState(), 3);
                            } else if (!level.isClientSide) {
                                level.destroyBlock(pickedBlock, true, player);
                                ACEntityRegistry.CORRODENT.get().spawn((ServerLevel) level, pickedBlock, MobSpawnType.MOB_SUMMONED);
                            }
                        }
                        if (AlexsCavesExemplified.COMMON_CONFIG.RADIOACTIVE_AWARENESS_ENABLED.get() && blockState.is(ACExexmplifiedTagRegistry.RADIOACTIVE) && HazmatArmorItem.getWornAmount(player) < 4 && level.random.nextDouble() < 0.5) {
                            player.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 200, 0));
                        }
                        if (blockState.getBlock() instanceof ActivatedByAltar && livingEntity.isHolding(ACItemRegistry.PEARL.get()) && level.random.nextDouble() < 0.5) {
                            level.setBlockAndUpdate(pickedBlock, blockState.setValue(ActivatedByAltar.ACTIVE, true));
                            if (level.random.nextDouble() < 0.05){
                                level.playLocalSound(pickedBlock, ACSoundRegistry.ABYSSMARINE_GLOW_ON.get(), SoundSource.BLOCKS, 1.5F, level.random.nextFloat() * 0.4F + 0.8F, false);
                            }
                        }
                    }
                }
            }

        }

        if (AlexsCavesExemplified.COMMON_CONFIG.HEAVY_GRAVITY_ENABLED.get()){
            AttributeInstance gravity = livingEntity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = livingEntity.isHolding(ACItemRegistry.HEAVYWEIGHT.get());

            if (flag) {
                if (!gravity.hasModifier(FAST_FALLING)) gravity.addTransientModifier(FAST_FALLING);
            } else if (gravity.hasModifier(FAST_FALLING)) {
                gravity.removeModifier(FAST_FALLING);
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.DISORIENTED_ENABLED.get() && !level.isClientSide && livingEntity instanceof WatcherEntity watcherEntity){
            Entity possessedEntity = watcherEntity.getPossessedEntity();
            if (possessedEntity != null && possessedEntity.isAlive()) {
                if (possessedEntity.getId() != watcherEntity.getId() && possessedEntity instanceof Player player) {
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GUM_TRAMPLE_ENABLED.get() && livingEntity instanceof GumWormEntity gumWorm && gumWorm.isVehicle() && gumWorm.getFirstPassenger() instanceof Player){
            for (LivingEntity entity : gumWorm.level().getEntitiesOfClass(LivingEntity.class, gumWorm.getBoundingBox().inflate(1.2))) {
                if (entity != gumWorm && entity.getBbHeight() <= 3.5F) {
                    entity.hurt(gumWorm.damageSources().mobAttack(gumWorm), 1.0F);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.WASTE_POWERUP_ENABLED.get() && livingEntity instanceof BrainiacEntity brainiac){
            if (!level.isClientSide && brainiac.hasBarrel()) {
                if (brainiac.getAnimation() == ANIMATION_DRINK_BARREL && brainiac.getAnimationTick() >= 60) {
                    brainiac.getPersistentData().putBoolean("WastePowered", true);
                }
            }
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.PRESSURED_HOOKS_ENABLED.get()){
            if (livingEntity instanceof Player player) {
                boolean trueMainhand = livingEntity.getMainHandItem().is(ACItemRegistry.CANDY_CANE_HOOK.get());
                boolean trueOffhand = livingEntity.getOffhandItem().is(ACItemRegistry.CANDY_CANE_HOOK.get());
                if (livingEntity.getVehicle() instanceof GumWormSegmentEntity && trueMainhand) {
                    if (player.isCreative())
                        return;
                    if (!(player.getRandom().nextDouble() < 0.05))
                        return;
                    player.getMainHandItem().hurtAndBreak(1, player, (p_233654_0_) -> {
                    });
                }
                if (livingEntity.getVehicle() instanceof GumWormSegmentEntity && trueOffhand) {
                    if (player.isCreative())
                        return;
                    if (!(player.getRandom().nextDouble() < 0.05))
                        return;
                    player.getOffhandItem().hurtAndBreak(1, player, (p_233654_0_) -> {
                    });
                }

                if ((!trueMainhand || !trueOffhand) && livingEntity.getVehicle() instanceof GumWormSegmentEntity && AlexsCavesExemplified.COMMON_CONFIG.LOGICAL_RIDING_ENABLED.get()) {
                    livingEntity.removeVehicle();
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.AMBER_HEAL_ENABLED.get() && livingEntity.getFeetBlockState().is(ACBlockRegistry.AMBER.get()) && livingEntity.getRandom().nextDouble() < 0.01){
            if (livingEntity.getMobType() == MobType.UNDEAD){
                livingEntity.hurt(livingEntity.damageSources().generic(),2);
            } else livingEntity.heal(2);
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.GUANO_SLOW_ENABLED.get() && (livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_BLOCK.get()) || livingEntity.getFeetBlockState().is(ACBlockRegistry.GUANO_LAYER.get()))){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 0));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.SOLIDIFIED_WATCHER_ENABLED.get() && livingEntity instanceof WatcherEntity watcherEntity){
            if (watcherEntity.tickCount > 12000){
                BlockPos blockPos = watcherEntity.getOnPos().above();

                for (int i = 0; i < 4; i++){
                    BlockPos blockPlacing = new BlockPos(blockPos.getX(),blockPos.getY() + i,blockPos.getZ());
                    level.setBlock(blockPlacing,ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(),2);
                    if (i == 3){
                        if (watcherEntity.getDirection() == Direction.WEST || watcherEntity.getDirection() == Direction.EAST){
                            level.setBlock(blockPlacing.north(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.NORTH),2);
                            level.setBlock(blockPlacing.south(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.SOUTH),2);
                        } else {
                            level.setBlock(blockPlacing.east(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.EAST),2);
                            level.setBlock(blockPlacing.west(),ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().setValue(ThornwoodBranchBlock.FACING,Direction.WEST),2);
                        }
                    }
                    if (!(level.random.nextDouble() < 1))
                        return;
                    level.setBlock(new BlockPos(blockPlacing.above()),ACBlockRegistry.BEHOLDER.get().defaultBlockState(),2);


                }
                watcherEntity.playSound(ACSoundRegistry.WATCHER_DEATH.get(), 6F, -5F);
                watcherEntity.discard();

            }
        }



        if (AlexsCavesExemplified.COMMON_CONFIG.STICKY_SODA_ENABLED.get() && livingEntity.getFeetBlockState().is(ACBlockRegistry.PURPLE_SODA.get()) && !livingEntity.getType().is(ACTagRegistry.CANDY_MOBS)){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 90, 0));
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.PURPLE_LEATHERED_ENABLED.get()) {
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.HEAD),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.FEET),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.CHEST),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.LEGS),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND),livingEntity);
            checkLeatherArmor(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND),livingEntity);
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.CRUMBY_RAGE_ENABLED.get()){
            Iterator<GingerbreadManEntity> var4 = level.getEntitiesOfClass(GingerbreadManEntity.class, livingEntity.getBoundingBox().inflate(10, 5, 10)).iterator();
            while (var4.hasNext()) {
                LivingEntity entity = var4.next();
                if (entity instanceof GingerbreadManEntity gingerbreadMan && !gingerbreadMan.isOvenSpawned() && livingEntity.getUseItem().is(ACItemRegistry.GINGERBREAD_CRUMBS.get())) {
                    gingerbreadMan.setTarget(livingEntity);
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.FLY_TRAP_ENABLED.get()  && ModList.get().isLoaded("alexsmobs")){
            BlockState blockState = livingEntity.getFeetBlockState();
            BlockPos blockPos = new BlockPos(livingEntity.getBlockX(),livingEntity.getBlockY(),livingEntity.getBlockZ());
            if (blockState.is(ACBlockRegistry.FLYTRAP.get())&& AMCompat.fly(livingEntity)){
                 if (blockState.getValue(PottedFlytrapBlock.OPEN)){
                     livingEntity.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                     livingEntity.captureDrops();
                     livingEntity.kill();
                     livingEntity.level().setBlock(blockPos, blockState.setValue(PottedFlytrapBlock.OPEN, false), 2);
                 }
            }
        }

        if(AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get()){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && livingEntity.getRandom().nextDouble() < 0.05 && (livingEntity.isInWater() || livingEntity.getBlockStateOn().is(Blocks.WATER_CAULDRON) || livingEntity.isInWaterRainOrBubble())) {
                livingEntity.removeEffect(irradiated.getEffect());
                livingEntity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
            }
        }

        BlockState blockState = livingEntity.getBlockStateOn();
        if (blockState.getBlock() instanceof GeothermalVentBlock){
            if (AlexsCavesExemplified.COMMON_CONFIG.GEOTHERMAL_EFFECTS_ENABLED.get()){
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 1){
                    if(AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get()){
                        MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
                        if (irradiated != null && livingEntity.getRandom().nextDouble() < 0.05) {
                            livingEntity.removeEffect(irradiated.getEffect());
                            livingEntity.addEffect(new MobEffectInstance(irradiated.getEffect(), irradiated.getDuration() - 100, irradiated.getAmplifier()));
                        }
                    }
                    if (livingEntity.isOnFire()) livingEntity.setRemainingFireTicks(livingEntity.getRemainingFireTicks() - 30);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 2){
                    livingEntity.setSecondsOnFire(5);
                }
                if (blockState.getValue(GeothermalVentBlock.SMOKE_TYPE) == 3){
                    if(!livingEntity.hasEffect(ACEffectRegistry.IRRADIATED.get())){
                        livingEntity.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 400, 0));
                    }
                }
            }
        }

        int irradiationAmmount = AlexsCavesExemplified.COMMON_CONFIG.EXEMPLIFIED_IRRADIATION_AMOUNT.get();
        if(irradiationAmmount > 0){
            MobEffectInstance irradiated = livingEntity.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && irradiated.getAmplifier() >= irradiationAmmount - 1) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));

                if (ModList.get().isLoaded("alexsmobs"))
                    livingEntity.addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION.get(), 60, 0));
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && !level.isClientSide && livingEntity.isInWaterRainOrBubble() && livingEntity.hasEffect(ACEEffects.RABIAL.get())) {
            livingEntity.hurt(ACEDamageTypes.causeRabialWaterDamage(level.registryAccess()), 1.0F);
        }
    }

    @SubscribeEvent
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        ItemStack offHand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();

        if(AlexsCavesExemplified.COMMON_CONFIG.IRRADIATION_WASHOFF_ENABLED.get() && ModList.get().isLoaded("supplementaries")){
            MobEffectInstance irradiated = player.getEffect(ACEffectRegistry.IRRADIATED.get());
            if (irradiated != null && mainHand.is(ModRegistry.SOAP.get()) && (player.isInWater() || player.getBlockStateOn().is(Blocks.WATER_CAULDRON))) {
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
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

        if (AlexsCavesExemplified.COMMON_CONFIG.KIROV_REPORTING_ENABLED.get() && player.isFallFlying() && (offHand.is(Items.FLINT_AND_STEEL) || offHand.is(Items.FIRE_CHARGE))){
            boolean clientSide = level.isClientSide;
            if (mainHand.is(Items.TNT)){
                if (clientSide){
                    player.getMainHandItem().shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 60);
                    player.swing(InteractionHand.MAIN_HAND);
                } else {
                    mainHand.shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(offHand.getItem(), 60);
                    TNT.spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
                }
            } else if (mainHand.is(Items.TNT_MINECART)){
                if (clientSide){
                    player.getMainHandItem().shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 60);
                    player.swing(InteractionHand.MAIN_HAND);
                } else {
                    mainHand.shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(offHand.getItem(), 60);
                    TNT_MINECART.spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
                }
            } else if (mainHand.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem())) {
                if (clientSide){
                    player.getMainHandItem().shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), 60);
                    player.swing(InteractionHand.MAIN_HAND);
                } else {
                    mainHand.shrink(player.isCreative() ? 0 : 1);
                    player.getCooldowns().addCooldown(offHand.getItem(), 60);
                    ACEntityRegistry.NUCLEAR_BOMB.get().spawn((ServerLevel) level, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
                }
            }

        }
    }

    @SubscribeEvent
    public void livingDamage(LivingDamageEvent livingDamageEvent) {
        Entity damager = livingDamageEvent.getSource().getEntity();
        LivingEntity damaged = livingDamageEvent.getEntity();


        if(AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && damager instanceof LivingEntity living && living.hasEffect(ACEEffects.RABIAL.get()) && damaged.getType().is(ACExexmplifiedTagRegistry.CAN_RABIES) && !damaged.hasEffect(MobEffects.DAMAGE_RESISTANCE)){
            damaged.addEffect(new MobEffectInstance(ACEEffects.RABIAL.get(), 72000, 0));
        }

        if(AlexsCavesExemplified.COMMON_CONFIG.STICKY_CARAMEL_ENABLED.get() && damager instanceof CaramelCubeEntity caramelCubeEntity && caramelCubeEntity.getRandom().nextDouble() < 0.5){
            MeltedCaramelEntity meltedCaramel = ACEntityRegistry.MELTED_CARAMEL.get().create(caramelCubeEntity.level());
            if (meltedCaramel == null)
                return;
            meltedCaramel.setPos(damaged.getPosition(1));
            meltedCaramel.setDespawnsIn(40 + ((1 + caramelCubeEntity.getSlimeSize()) - 1) * 40);
            meltedCaramel.setDeltaMovement(caramelCubeEntity.getDeltaMovement().multiply(-1.0F, 0.0F, -1.0F));
            caramelCubeEntity.level().addFreshEntity(meltedCaramel);
        }
    }
    

    @SubscribeEvent
    public void bonemealEvent(BonemealEvent bonemealEvent) {
        Entity entity = bonemealEvent.getEntity();
        Level level = bonemealEvent.getLevel();
        BlockPos blockPos = bonemealEvent.getPos();
        BlockState blockState = level.getBlockState(blockPos);
        RandomSource random = level.getRandom();

		if (level.getBiome(blockPos).is(ACBiomeRegistry.PRIMORDIAL_CAVES) && AlexsCavesExemplified.COMMON_CONFIG.CAVIAL_BONEMEAL_ENABLED.get() && level.getBlockState(blockPos).is(Blocks.GRASS_BLOCK) && level instanceof ServerLevel serverLevel && entity != null) {
            bonemealEvent.setCanceled(true);

            BlockPos $$4 = blockPos.above();
            BlockState $$5 = Blocks.GRASS.defaultBlockState();
            Optional<Holder.Reference<PlacedFeature>> placedFeature = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolder(ACEFeatures.PLACED_PRIMORDIAL_BONEMEAL);

			//noinspection OptionalIsPresent
			if (placedFeature.isPresent())
                tryPlacePrimordialBonemeal(serverLevel, $$4, random, level, $$5, placedFeature.get().value());
        }


        if (AlexsCavesExemplified.COMMON_CONFIG.ECOLOGICAL_REPUTATION_ENABLED.get()) {
            if (blockState.is(ACBlockRegistry.PING_PONG_SPONGE.get()) && level.getBiome(blockPos).is(ACBiomeRegistry.ABYSSAL_CHASM)) {
                for (LivingEntity deepOne : level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(30))) {
                    if (deepOne instanceof DeepOneBaseEntity deepOneBaseEntity && entity instanceof Player player) {
                        deepOneBaseEntity.addReputation(player.getUUID(),1);
                    }
                }
            }
        }

    }

    private void tryPlacePrimordialBonemeal(ServerLevel serverLevel, BlockPos placePos, RandomSource random, Level level, BlockState placeOn, PlacedFeature placedFeature) {
        label49:
        for(int tries = 0; tries < 128; ++tries) {
            BlockPos tryPos = placePos;

            for(int i = 0; i < tries / 16; ++i) {
                tryPos = tryPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!level.getBlockState(tryPos.below()).is(Blocks.GRASS_BLOCK) || level.getBlockState(tryPos).isCollisionShapeFullBlock(level, tryPos)) {
                    continue label49;
                }
            }

            BlockState blockAt = level.getBlockState(tryPos);
            if (blockAt.is(placeOn.getBlock()) && random.nextInt(10) == 0) {
                ((BonemealableBlock) placeOn.getBlock()).performBonemeal(serverLevel, random, tryPos, blockAt);
            }

			if (blockAt.isAir()) {
                placedFeature.feature().value().place(serverLevel, serverLevel.getChunkSource().getGenerator(), random, tryPos);
			}
        }
    }

    @SubscribeEvent
    public void talkEvent(ServerChatEvent serverChatEvent){
        Player player = serverChatEvent.getPlayer();
        String message = serverChatEvent.getMessage().getString();

        if (message.contains("pspspsps") && AlexsCavesExemplified.COMMON_CONFIG.CATTASTROPHE_ENABLED.get()){

            int delay = 0;
            while (delay < 200) {
                delay++;
                if (delay >= 200){
                    for (LivingEntity cats : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4, 4, 4))) {
                        if (cats instanceof Cat || cats instanceof Ocelot || cats instanceof RaycatEntity || AMCompat.tiger(cats)) {
                            NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(cats.level());
                            explosion.copyPosition(cats);
                            explosion.setSize(1.75F);

                            cats.level().addFreshEntity(explosion);
                            cats.discard();
                        }
                    }
                }
            }
        }

        if (AlexsCavesExemplified.COMMON_CONFIG.RABIES_ENABLED.get() && player.getRandom().nextDouble() < 0.01 && player.hasEffect(ACEEffects.RABIAL.get())){
            serverChatEvent.setMessage(Component.nullToEmpty("rrRRRrrrAgh!... " + message));
        }
    }


    private void checkLeatherArmor(ItemStack item, LivingEntity living){
        if (item.getItem() instanceof DyeableLeatherItem dyeableLeatherItem && living.isInFluidType(ACFluidRegistry.PURPLE_SODA_FLUID_TYPE.get())) {
            dyeableLeatherItem.setColor(item, 0Xb839e6);
        }
    }

//    @SubscribeEvent
//    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        Player player = event.getEntity();
//        CompoundTag playerData = event.getEntity().getPersistentData();
//        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
//
//        ItemStack book = new ItemStack(PatchouliItems.BOOK);
//        book.getOrCreateTag().putString("patchouli:book","alexscavesexemplified:ace_exemplified_wiki");
//
//        if (!data.getBoolean("ace_book") && AlexsCavesExemplified.COMMON_CONFIG.ACE_WIKI_ENABLED.get()) {
//            player.addItem(book);
//            data.putBoolean("ace_book", true);
//            playerData.put(Player.PERSISTED_NBT_TAG, data);
//        }
//    }






}

