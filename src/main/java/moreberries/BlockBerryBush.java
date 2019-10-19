package moreberries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.EntityContext;
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
		super(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH).build().nonOpaque());
		this.item = item;
	}

	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1) {
		return new ItemStack(this);
	}

	public ActionResult onUse(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1,
			Hand hand_1, BlockHitResult blockHitResult_1) {
		int int_1 = (Integer) blockState_1.get(AGE);
		boolean boolean_1 = int_1 == 3;
		if (!boolean_1 && playerEntity_1.getStackInHand(hand_1).getItem() == Items.BONE_MEAL) {
			return ActionResult.PASS;
		} else if (int_1 > 1) {
			int int_2 = 1 + world_1.random.nextInt(2);
			dropStack(world_1, blockPos_1, new ItemStack(item, int_2 + (boolean_1 ? 1 : 0)));
			world_1.playSound((PlayerEntity) null, blockPos_1, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH,
					SoundCategory.BLOCKS, 1.0F, 0.8F + world_1.random.nextFloat() * 0.4F);
			world_1.setBlockState(blockPos_1, (BlockState) blockState_1.with(AGE, 1), 2);
			return ActionResult.SUCCESS;
		} else {
			return super.onUse(blockState_1, world_1, blockPos_1, playerEntity_1, hand_1, blockHitResult_1);
		}
	}

	public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1,
			EntityContext entityContext_1) {
		if ((Integer) blockState_1.get(AGE) == 0) {
			return SMALL_SHAPE;
		} else {
			return LARGE_SHAPE;
		}
	}

	protected boolean canPlantOnTop(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
		Block block_1 = blockState_1.getBlock();
		return block_1 == Blocks.GRASS_BLOCK || block_1 == Blocks.DIRT || block_1 == Blocks.COARSE_DIRT
				|| block_1 == Blocks.PODZOL || block_1 == MoreBerries.blueBerryBush
				|| block_1 == MoreBerries.blackBerryBush || block_1 == MoreBerries.yellowBerryBush
				|| block_1 == MoreBerries.orangeBerryBush || block_1 == MoreBerries.purpleBerryBush
				|| block_1 == MoreBerries.greenBerryBush;
	}

}
