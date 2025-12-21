package moreberries.block;

import java.util.HashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class BerryCakeBlock extends CakeBlock {

    public HashMap<Block, CandleBerryCakeBlock> CANDLES_TO_CANDLE_CAKES;

    public BerryCakeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(BITES, 0));
        CANDLES_TO_CANDLE_CAKES = new HashMap<>();
    }

    public static InteractionResult eat(LevelAccessor worldAccess, BlockPos blockPos, BlockState blockState,
            Player playerEntity) {
        if (!playerEntity.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            playerEntity.awardStat(Stats.EAT_CAKE_SLICE);
            playerEntity.getFoodData().eat(2, 0.1F);
            int i = (Integer) blockState.getValue(BITES);
            worldAccess.gameEvent(playerEntity, GameEvent.EAT, blockPos);
            if (i < 6) {
                worldAccess.setBlock(blockPos, (BlockState) blockState.setValue(BITES, i + 1), 3);
            } else {
                worldAccess.removeBlock(blockPos, false);
                worldAccess.gameEvent(playerEntity, GameEvent.BLOCK_DESTROY, blockPos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level world, BlockPos blockPos,
            Player playerEntity, InteractionHand hand, BlockHitResult blockHitResult) {
        Item item = itemStack.getItem();
        Block block = Block.byItem(item);
        if (!itemStack.is(ItemTags.CANDLES) || blockState.getValue(BITES) != 0 || !(block instanceof CandleBlock)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (!playerEntity.isCreative()) {
            itemStack.shrink(1);
        }

        world.playSound((Player) null, blockPos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS,
                1.0F, 1.0F);
        world.setBlockAndUpdate(blockPos, ((BerryCakeBlock) blockState.getBlock()).CANDLES_TO_CANDLE_CAKES
                .get(block).defaultBlockState());
        world.gameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
        playerEntity.awardStat(Stats.ITEM_USED.get(item));

        return InteractionResult.SUCCESS;
    }

}
