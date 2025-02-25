package org.crimsoncrips.alexscavesexemplified.server.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

/**
 * Copied from SimpleBlockFeature, but with place flag customized to being 3 (Block.UPDATE_ALL) instead of 2 (Block.UPDATE_CLIENTS)
 */
public class FlaggedSimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
	private final int placeFlag;

	public FlaggedSimpleBlockFeature(Codec<SimpleBlockConfiguration> codec) {
		super(codec);

		this.placeFlag = Block.UPDATE_ALL;
	}

	/**
	 * Places the given feature at the given location.
	 * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
	 * that they can safely generate into.
	 * @param context A context object with a reference to the level and the position the feature is being placed at
	 */
	@Override
	public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
		WorldGenLevel worldGenLevel = context.level();
		BlockPos blockPos = context.origin();
		BlockState blockState = context.config().toPlace().getState(context.random(), blockPos);

		if (blockState.canSurvive(worldGenLevel, blockPos)) {
			if (blockState.getBlock() instanceof DoublePlantBlock) {
				if (!worldGenLevel.isEmptyBlock(blockPos.above())) {
					return false; // Feature obstructed from generating
				}

				DoublePlantBlock.placeAt(worldGenLevel, blockState, blockPos, this.placeFlag);
			} else {
				worldGenLevel.setBlock(blockPos, blockState, this.placeFlag);
			}

			return true; // Feature generated
		} else {
			return false; // Feature failed to generate
		}
	}
}
