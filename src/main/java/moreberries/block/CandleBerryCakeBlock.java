package moreberries.block;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class CandleBerryCakeBlock extends AbstractCandleBlock {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    protected static final VoxelShape CAKE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);
    protected static final VoxelShape CANDLE_SHAPE = Block.createCuboidShape(7.0, 8.0, 7.0, 9.0, 14.0, 9.0);
    protected static final VoxelShape SHAPE = VoxelShapes.union(CAKE_SHAPE, CANDLE_SHAPE);
    private static final Iterable<Vec3d> PARTICLE_OFFSETS = ImmutableList.of(new Vec3d(0.5, 1.0, 0.5));
    public BerryCakeBlock cake;
    public CandleBlock candle;

    public MapCodec<CandleCakeBlock> getCodec() {
        return null;
    }

    public CandleBerryCakeBlock(CandleBlock candle, BerryCakeBlock cake, Settings settings) {
        super(settings);
        cake.CANDLES_TO_CANDLE_CAKES.put(candle, this);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
        this.cake = cake;
        this.candle = candle;
    }

    @Override
    protected Iterable<Vec3d> getParticleOffsets(BlockState blockState) {
        return PARTICLE_OFFSETS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos,
            ShapeContext shapeContext) {
        return SHAPE;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack itemStack, BlockState blockState, World world, BlockPos blockPos,
            PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        if (itemStack.isOf(Items.FLINT_AND_STEEL) || itemStack.isOf(Items.FIRE_CHARGE)) {
            return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        }
        if (isHittingCandle(blockHitResult) && itemStack.isEmpty()
                && blockState.get(LIT).booleanValue()) {
            extinguish(playerEntity, blockState, world, blockPos);
            return ActionResult.SUCCESS;
        }
        return super.onUseWithItem(itemStack, blockState, world, blockPos, playerEntity, hand, blockHitResult);
    }

    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity,
            Hand hand, BlockHitResult blockHitResult) {
        ActionResult actionResult = BerryCakeBlock.tryEat(world, blockPos, cake.getDefaultState(), playerEntity);
        if (actionResult.isAccepted()) {
            CandleCakeBlock.dropStacks(blockState, world, blockPos);
        }
        return actionResult;
    }

    private static boolean isHittingCandle(BlockHitResult blockHitResult) {
        return blockHitResult.getPos().y - (double) blockHitResult.getBlockPos().getY() > 0.5D;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(cake);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState blockState,
            WorldView worldView,
            ScheduledTickView scheduledTickView,
            BlockPos blockPos,
            Direction direction,
            BlockPos blockPos2,
            BlockState blockState2,
            Random random) {
        if (direction == Direction.DOWN && !blockState.canPlaceAt(worldView, blockPos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(blockState, worldView, scheduledTickView, blockPos, direction, blockPos2,
                blockState2, random);
    }

    @Override
    protected boolean canPlaceAt(BlockState blockState, WorldView worldView, BlockPos blockPos) {
        return worldView.getBlockState(blockPos.down()).isSolidBlock(worldView, blockPos);
    }

    @Override
    protected int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
        return CakeBlock.DEFAULT_COMPARATOR_OUTPUT;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState blockState) {
        return true;
    }

    @Override
    protected boolean canPathfindThrough(BlockState blockState, NavigationType navigationType) {
        return false;
    }

    public static boolean canBeLit(BlockState blockState) {
        return blockState.isIn(BlockTags.CANDLE_CAKES,
                abstractBlockState -> abstractBlockState.contains(LIT) && blockState.get(LIT) == false);
    }
}
