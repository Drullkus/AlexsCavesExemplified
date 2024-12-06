package org.crimsoncrips.alexscavesexemplified.mixins.misc;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.PrimalMagmaBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.VolcanicCoreBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.crimsoncrips.alexscavesexemplified.ACEReflectionUtil;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

import static com.github.alexmodguy.alexscaves.server.block.PrimalMagmaBlock.SINK_SHAPE;


@Mixin(PrimalMagmaBlock.class)
public abstract class ACEPrimalMagma extends Block {

    @Shadow @Final public static BooleanProperty ACTIVE;

    public ACEPrimalMagma(Properties pProperties) {
        super(pProperties);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (state.getValue(ACTIVE) && context instanceof EntityCollisionContext entityCollisionContext) {
            boolean aceThrough = entityCollisionContext.getEntity() instanceof AtlatitanEntity && ACExemplifiedConfig.VOLCANIC_SACRIFICE_ENABLED;
            if (!(entityCollisionContext.getEntity() instanceof LuxtructosaurusEntity)) {
                return entityCollisionContext.getEntity() instanceof ItemEntity || aceThrough  ? Shapes.empty() : SINK_SHAPE;
            }
        }

        return super.getCollisionShape(state, level, blockPos, context);
    }

}
