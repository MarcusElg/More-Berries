package moreberries;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class BerryFeature extends Feature<DefaultFeatureConfig> {
	protected final BlockState berry;

	public BerryFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1, BlockState blockState_1) {
		super(function_1);
		this.berry = blockState_1;
	}

	public boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1,
			Random random_1, BlockPos blockPos_1, DefaultFeatureConfig defaultFeatureConfig_1) {
		int int_1 = 0;

		if (random_1.nextInt(3) == 0) { // Chance
			for (int int_2 = 0; int_2 < 24; ++int_2) { // Amount
				BlockPos blockPos_2 = blockPos_1.add(random_1.nextInt(4) - random_1.nextInt(4),
						random_1.nextInt(2) - random_1.nextInt(2), random_1.nextInt(4) - random_1.nextInt(4));
				if (iWorld_1.isAir(blockPos_2)
						&& iWorld_1.getBlockState(blockPos_2.down(1)).getBlock() == Blocks.GRASS_BLOCK) {
					iWorld_1.setBlockState(blockPos_2, this.berry, 2);
					++int_1;
				}
			}
		}

		return int_1 > 0;
	}
}
