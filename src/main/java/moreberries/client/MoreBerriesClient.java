package moreberries.client;

import java.util.ArrayList;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
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
            BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT);
        }
    }

    public void registerBlockColour(Block block) {
        ColorProviderRegistry.BLOCK.register((world_block, pos, world, layer) -> {
            BlockColor provider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
            return provider == null ? -1 : provider.getColor(world_block, pos, world, layer);
        }, block);
    }

}
