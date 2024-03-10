package moreberries.block;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class BerryCakeBlock extends CakeBlock {

	public HashMap<Block, CandleCakeBlock> CANDLES_TO_CANDLE_CAKES;

	public BerryCakeBlock(Block.Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(BITES, 0));
		CANDLES_TO_CANDLE_CAKES = new HashMap<>();
	}

	public static ActionResult tryEat(WorldAccess worldAccess, BlockPos blockPos, BlockState blockState,
			PlayerEntity playerEntity) {
		if (!playerEntity.canConsume(false)) {
			return ActionResult.PASS;
		} else {
			playerEntity.incrementStat(Stats.EAT_CAKE_SLICE);
			playerEntity.getHungerManager().add(2, 0.1F);
			int i = (Integer) blockState.get(BITES);
			worldAccess.emitGameEvent(playerEntity, GameEvent.EAT, blockPos);
			if (i < 6) {
				worldAccess.setBlockState(blockPos, (BlockState) blockState.with(BITES, i + 1), 3);
			} else {
				worldAccess.removeBlock(blockPos, false);
				worldAccess.emitGameEvent(playerEntity, GameEvent.BLOCK_DESTROY, blockPos);
			}

			return ActionResult.SUCCESS;
		}
	}

}
