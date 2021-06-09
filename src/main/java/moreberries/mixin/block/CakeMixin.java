package moreberries.mixin.block;

import moreberries.BlockBerryCake;
import moreberries.MoreBerries;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeMixin {

    @Inject(method="onUse", at = @At("HEAD"), cancellable = true)
    public void onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        // Override candle to candle cake
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (itemStack.isIn(ItemTags.CANDLES) && (Integer)blockState.get(Properties.BITES) == 0) {
            Block block = Block.getBlockFromItem(item);
            if (block instanceof CandleBlock) {
                if (!playerEntity.isCreative()) {
                    itemStack.decrement(1);
                }

                world.playSound((PlayerEntity)null, blockPos, SoundEvents.BLOCK_CAKE_ADD_CANDLE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                Pair<Block, Block> pair = new Pair<Block, Block>(block, blockState.getBlock());

                if (blockState.getBlock() instanceof BlockBerryCake) {
                    world.setBlockState(blockPos, ((BlockBerryCake) blockState.getBlock()).CANDLES_TO_CANDLE_CAKES.get(block).getDefaultState());
                } else {
                    world.setBlockState(blockPos, MoreBerries.VANILLA_CANDLES_TO_CANDLE_CAKES.get(block).getDefaultState());
                }

                world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(item));
                callbackInfoReturnable.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
