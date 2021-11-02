package moreberries;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;

public class MoreBerriesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerBlocks(new Block[]{
                MoreBerries.blueBerryBush,
                MoreBerries.blackBerryBush,
                MoreBerries.greenBerryBush,
                MoreBerries.yellowBerryBush,
                MoreBerries.orangeBerryBush,
                MoreBerries.purpleBerryBush
        });

        registerBlockColour(Blocks.SWEET_BERRY_BUSH);
    }

    public void registerBlocks(Block[] blocks) {
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
