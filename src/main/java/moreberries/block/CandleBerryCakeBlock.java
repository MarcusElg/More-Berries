package moreberries.block;

import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CandleBerryCakeBlock extends AbstractCandleBlock {
    protected static final VoxelShape CAKE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);
    protected static final VoxelShape CANDLE_SHAPE = Block.box(7.0, 8.0, 7.0, 9.0, 14.0, 9.0);
    protected static final VoxelShape SHAPE = Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);
    private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5, 1.0, 0.5));
    public BerryCakeBlock cake;
    public CandleBlock candle;

    public CandleBerryCakeBlock(CandleBlock candle, BerryCakeBlock cake, Properties settings) {
        super(settings);
        cake.CANDLES_TO_CANDLE_CAKES.put(candle, this);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
        this.cake = cake;
        this.candle = candle;
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState blockState) {
        return PARTICLE_OFFSETS;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos,
            CollisionContext shapeContext) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level world, BlockPos blockPos,
            Player playerEntity, InteractionHand hand, BlockHitResult blockHitResult) {
        if (itemStack.is(Items.FLINT_AND_STEEL) || itemStack.is(Items.FIRE_CHARGE)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (isHittingCandle(blockHitResult) && itemStack.isEmpty()
                && blockState.getValue(LIT)) {
            extinguish(playerEntity, blockState, world, blockPos);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, blockState, world, blockPos, playerEntity, hand, blockHitResult);
    }

    public InteractionResult onUse(BlockState blockState, Level world, BlockPos blockPos, Player playerEntity,
            InteractionHand hand, BlockHitResult blockHitResult) {
        InteractionResult actionResult = BerryCakeBlock.eat(world, blockPos, cake.defaultBlockState(), playerEntity);
        if (actionResult.consumesAction()) {
            CandleCakeBlock.dropResources(blockState, world, blockPos);
        }
        return actionResult;
    }

    private static boolean isHittingCandle(BlockHitResult blockHitResult) {
        return blockHitResult.getLocation().y - (double) blockHitResult.getBlockPos().getY() > 0.5D;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockGetter blockView, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(cake);
    }

    @Override
    protected BlockState updateShape(BlockState blockState,
            LevelReader worldView,
            ScheduledTickAccess scheduledTickView,
            BlockPos blockPos,
            Direction direction,
            BlockPos blockPos2,
            BlockState blockState2,
            RandomSource random) {
        if (direction == Direction.DOWN && !blockState.canSurvive(worldView, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(blockState, worldView, scheduledTickView, blockPos, direction, blockPos2,
                blockState2, random);
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader worldView, BlockPos blockPos) {
        return worldView.getBlockState(blockPos.below()).isRedstoneConductor(worldView, blockPos);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType navigationType) {
        return false;
    }

    @Override
    protected boolean canBeLit(BlockState blockState) {
        return blockState.is(BlockTags.CANDLE_CAKES,
                abstractBlockState -> abstractBlockState.hasProperty(LIT) && blockState.getValue(LIT) == false);
    }
}
