package moreberries.client;

import java.util.ArrayList;
import java.util.List;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MoreBerriesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerBlocks(MoreBerries.bushes);

        registerBlockColour(Blocks.SWEET_BERRY_BUSH);
    }

    public void registerBlocks(ArrayList<BerryBushBlock> blocks) {
        for (Block block : blocks) {
            registerBlockColour(block);
        }
    }

    public void registerBlockColour(Block block) {
        BlockColorRegistry.register(List.of(BlockTintSources.foliage()), block);
    }

}
