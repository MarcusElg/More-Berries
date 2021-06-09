package moreberries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BlockCandleBerryCake extends CandleCakeBlock {
    private BlockBerryCake cake;

    protected BlockCandleBerryCake(Block candle, BlockBerryCake cake, Settings settings) {
        super(candle, settings);
        cake.CANDLES_TO_CANDLE_CAKES.put(candle, this);
        this.cake = cake;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(cake);
    }
}
