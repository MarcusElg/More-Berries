package moreberries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

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

    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity,
            Hand hand, BlockHitResult blockHitResult) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
            if (isHittingCandle(blockHitResult) && playerEntity.getStackInHand(hand).isEmpty()
                    && (Boolean) blockState.get(LIT)) {
                extinguish(playerEntity, blockState, world, blockPos);
                return ActionResult.success(world.isClient);
            } else {
                ActionResult actionResult = BlockBerryCake.tryEat(world, blockPos, cake.getDefaultState(),
                        playerEntity);
                if (actionResult.isAccepted()) {
                    dropStacks(blockState, world, blockPos);
                }

                return actionResult;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    private static boolean isHittingCandle(BlockHitResult blockHitResult) {
        return blockHitResult.getPos().y - (double) blockHitResult.getBlockPos().getY() > 0.5D;
    }
}
