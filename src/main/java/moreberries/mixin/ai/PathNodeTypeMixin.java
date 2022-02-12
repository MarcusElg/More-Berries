package moreberries.mixin.ai;

import moreberries.BlockBerryBush;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LandPathNodeMaker.class)
public class PathNodeTypeMixin {
    @Inject(method="getCommonNodeType", at = @At("HEAD"), cancellable = true)
    private static void getCommonNodeType(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> callbackInfoReturnable) {
        BlockState blockState = blockView.getBlockState(blockPos);

        if (blockState.getBlock() instanceof BlockBerryBush) {
            callbackInfoReturnable.setReturnValue(PathNodeType.DAMAGE_OTHER);
        }
    }
}
