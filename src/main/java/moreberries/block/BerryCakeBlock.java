package moreberries.block;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

public class BerryCakeBlock extends CakeBlock {

	public HashMap<Block, CandleBerryCakeBlock> CANDLES_TO_CANDLE_CAKES;

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

	@Override
	protected ItemActionResult onUseWithItem(ItemStack itemStack, BlockState blockState, World world, BlockPos blockPos,
			PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		Item item = itemStack.getItem();
		Block block = Block.getBlockFromItem(item);
		if (!itemStack.isIn(ItemTags.CANDLES) || blockState.get(BITES) != 0 || !(block instanceof CandleBlock)) {
			return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (!playerEntity.isCreative()) {
			itemStack.decrement(1);
		}

		world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_CAKE_ADD_CANDLE, SoundCategory.BLOCKS,
				1.0F, 1.0F);
		world.setBlockState(blockPos, ((BerryCakeBlock) blockState.getBlock()).CANDLES_TO_CANDLE_CAKES
				.get(block).getDefaultState());
		world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
		playerEntity.incrementStat(Stats.USED.getOrCreateStat(item));

		return ItemActionResult.SUCCESS;
	}

}
