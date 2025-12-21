package moreberries.block;

import moreberries.MoreBerries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BerryBushBlock extends SweetBerryBushBlock {

    public Item item;
    private static final VoxelShape SMALL_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);
    private static final VoxelShape LARGE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public BerryBushBlock(Item item, BlockBehaviour.Properties settings) {
        super(settings);
        this.item = item;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockGetter blockView, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(this);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level world, BlockPos blockPos,
            Player playerEntity, InteractionHand hand, BlockHitResult blockHitResult) {
        if (blockState.getValue(AGE) < 3 && itemStack.is(Items.BONE_MEAL)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        return super.useItemOn(itemStack, blockState, world, blockPos, playerEntity, hand, blockHitResult);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level world, BlockPos blockPos, Player playerEntity,
            BlockHitResult blockHitResult) {
        if (blockState.getValue(AGE) > 1) {
            if (world instanceof ServerLevel serverWorld) {
                Block.dropFromBlockInteractLootTable(
                        serverWorld,
                        ResourceKey.create(Registries.LOOT_TABLE,
                                MoreBerries.getId("harvest/"
                                        + this.getDescriptionId().replace("block." + MoreBerries.MOD_ID + ".",
                                                ""))),
                        blockState,
                        world.getBlockEntity(blockPos),
                        null,
                        playerEntity,
                        (serverWorldx, itemStack) -> Block.popResource(serverWorldx, blockPos, itemStack));

                world.playSound((Player) null, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                        SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
                world.setBlock(blockPos, (BlockState) blockState.setValue(AGE, 1), 2);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(blockState, world, blockPos, playerEntity, blockHitResult);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockView, BlockPos blockPos,
            CollisionContext entityContext) {
        if (blockState.getValue(AGE) == 0) {
            return SMALL_SHAPE;
        } else {
            return LARGE_SHAPE;
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        Block block = blockState.getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT
                || block == Blocks.ROOTED_DIRT
                || block == Blocks.PODZOL || block == MoreBerries.blueBerryBush
                || block == MoreBerries.blackBerryBush || block == MoreBerries.yellowBerryBush
                || block == MoreBerries.orangeBerryBush || block == MoreBerries.purpleBerryBush
                || block == MoreBerries.greenBerryBush;
    }

}
