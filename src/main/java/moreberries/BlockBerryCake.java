package moreberries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.HashMap;

public class BlockBerryCake extends CakeBlock {

	public HashMap<Block, CandleCakeBlock> CANDLES_TO_CANDLE_CAKES;

	public BlockBerryCake(Block.Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(BITES, 0));
		CANDLES_TO_CANDLE_CAKES = new HashMap<>();
	}

}
