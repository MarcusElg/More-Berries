package moreberries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockBerryBush extends SweetBerryBushBlock {

	public Item item;
	private static final VoxelShape SMALL_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);
	private static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

	public BlockBerryBush(Item item) {
		super(AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH).nonOpaque());
		this.item = item;
	}

	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
		return new ItemStack(this);
	}

	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity,
			Hand hand, BlockHitResult blockHitResult) {
		int int_1 = (Integer) blockState.get(AGE);
		boolean boolean_1 = int_1 == 3;
		if (!boolean_1 && playerEntity.getStackInHand(hand).getItem() == Items.BONE_MEAL) {
			return ActionResult.PASS;
		} else if (int_1 > 1) {
			int int_2 = 1 + world.random.nextInt(2);
			dropStack(world, blockPos, new ItemStack(item, int_2 + (boolean_1 ? 1 : 0)));
			world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES,
					SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			world.setBlockState(blockPos, (BlockState) blockState.with(AGE, 1), 2);
			return ActionResult.SUCCESS;
		} else {
			return super.onUse(blockState, world, blockPos, playerEntity, hand, blockHitResult);
		}
	}

	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos,
			ShapeContext entityContext) {
		if ((Integer) blockState.get(AGE) == 0) {
			return SMALL_SHAPE;
		} else {
			return LARGE_SHAPE;
		}
	}

	protected boolean canPlantOnTop(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		Block block = blockState.getBlock();
		return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.ROOTED_DIRT
				|| block == Blocks.PODZOL || block == MoreBerries.blueBerryBush
				|| block == MoreBerries.blackBerryBush || block == MoreBerries.yellowBerryBush
				|| block == MoreBerries.orangeBerryBush || block == MoreBerries.purpleBerryBush
				|| block == MoreBerries.greenBerryBush;
	}

}
