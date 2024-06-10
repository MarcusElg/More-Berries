package moreberries;

import java.util.ArrayList;

import moreberries.block.BerryBushBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.world.biome.FoliageColors;

public class MoreBerriesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerBlocks(MoreBerries.bushes);

        registerBlockColour(Blocks.SWEET_BERRY_BUSH);
    }

    public void registerBlocks(ArrayList<BerryBushBlock> blocks) {
        for (Block block : blocks) {
            registerBlockColour(block);
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }
    }

    public void registerBlockColour(Block block) {
        ColorProviderRegistry.BLOCK.register((world_block, pos, world, layer) -> {
            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
            return provider == null ? -1 : provider.getColor(world_block, pos, world, layer);
        }, block);

        if (block != Blocks.SWEET_BERRY_BUSH) {
            ColorProviderRegistry.ITEM.register((item, layer) -> FoliageColors.getDefaultColor(), block.asItem());
        }
    }

}
