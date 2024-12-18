package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.SugarStaffHexEntity;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.level.feature.AmbersolFeature;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(AmbersolFeature.class)
public abstract class ACEAmberFeature extends Feature<NoneFeatureConfiguration> {


    public ACEAmberFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }
    //Props to Drullkus for assistance


    @Inject(method = "drawOrb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private static void drawOrb(WorldGenLevel level, BlockPos center, RandomSource random, BlockState blockState, int radiusX, int radiusY, int radiusZ, CallbackInfo ci, @Local(ordinal = 1) BlockPos fill) {
        if (random.nextDouble() < 0.001 && ACExemplifiedConfig.PRESERVED_AMBER_ENABLED) {
            ServerLevel servLevel = level.getLevel();
            switch (level.getRandom().nextInt(0, 3)) {
                case 0:
                    if (!ModList.get().isLoaded("alexsmobs"))
                        return;
                    EntityCockroach cockroach = AMEntityRegistry.COCKROACH.get().create(servLevel);
                    if (cockroach != null) {
                        cockroach.setNoAi(true);
                        cockroach.setInvulnerable(true);
                        cockroach.setPos(new Vec3(cockroach.getX(), cockroach.getY() + 0.4, cockroach.getZ()));
                        level.addFreshEntity(cockroach);
                    }
                    break;
                case 1:
                    if (!ModList.get().isLoaded("alexsmobs"))
                        return;
                    EntityFly fly = AMEntityRegistry.FLY.get().create(servLevel);
                    if (fly != null) {
                        fly.setNoAi(true);
                        fly.setInvulnerable(true);
                        fly.setPos(new Vec3(fly.getX(), fly.getY() + 0.3, fly.getZ()));
                        level.addFreshEntity(fly);
                    }
                    break;
                case 2:
                    Frog frog = EntityType.FROG.create(servLevel);
                    if (frog != null) {
                        frog.setNoAi(true);
                        frog.setInvulnerable(true);
                        frog.setPos(new Vec3(frog.getX(), frog.getY() + 0.3, frog.getZ()));
                        level.addFreshEntity(frog);
                    }
                    break;
                case 3:
                    Tadpole tadpole = EntityType.TADPOLE.create(servLevel);
                    if (tadpole != null) {
                        tadpole.setNoAi(true);
                        tadpole.setInvulnerable(true);
                        tadpole.setPos(new Vec3(tadpole.getX(), tadpole.getY() + 0.3, tadpole.getZ()));
                        level.addFreshEntity(tadpole);
                    }
                    break;

            }
        }
    }
}
