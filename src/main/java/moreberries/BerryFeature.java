package moreberries;

import java.util.Random;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BerryFeature extends Feature {
	protected final BlockState berry;

	public BerryFeature(Codec<DefaultFeatureConfig> codec, BlockState berry) {
		super(codec);
		this.berry = berry;
	}

	@Override
	public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, FeatureConfig featureConfig) {
		int int_1 = 0;

		if (random.nextInt(3) == 0) { // Chance
			for (int int_2 = 0; int_2 < 24; ++int_2) { // Amount
				BlockPos blockPos_2 = blockPos.add(random.nextInt(4) - random.nextInt(4),
						random.nextInt(2) - random.nextInt(2), random.nextInt(4) - random.nextInt(4));
				if (serverWorldAccess.isAir(blockPos_2)
						&& serverWorldAccess.getBlockState(blockPos_2.down(1)).getBlock() == Blocks.GRASS_BLOCK) {
					serverWorldAccess.setBlockState(blockPos_2, this.berry, 2);
					++int_1;
				}
			}
		}

		return int_1 > 0;
	}
}
