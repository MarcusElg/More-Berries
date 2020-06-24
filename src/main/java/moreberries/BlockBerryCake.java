package moreberries;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;

public class BlockBerryCake extends CakeBlock {

	public BlockBerryCake(Block.Settings block$Settings_1) {
		super(block$Settings_1);
		this.setDefaultState(this.getDefaultState().with(BITES, 0));
	}

}
