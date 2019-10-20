package moreberries;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;

public class MoreBerriesClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerBlockColour(MoreBerries.blueBerryBush);
		registerBlockColour(MoreBerries.blackBerryBush);
		registerBlockColour(MoreBerries.greenBerryBush);
		registerBlockColour(MoreBerries.yellowBerryBush);
		registerBlockColour(MoreBerries.orangeBerryBush);
		registerBlockColour(MoreBerries.purpleBerryBush);

		registerBlockColour(Blocks.SWEET_BERRY_BUSH);
		
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.blueBerryBush, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.blackBerryBush, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.greenBerryBush, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.yellowBerryBush, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.orangeBerryBush, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.purpleBerryBush, RenderLayer.getCutout());
	}

	public void registerBlockColour(Block block) {
		ColorProviderRegistry.BLOCK.register((block3, pos, world, layer) -> {
			BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
			return provider == null ? -1 : provider.getColor(block3, pos, world, layer);
		}, block);

		if (block != Blocks.SWEET_BERRY_BUSH) {
			ColorProviderRegistry.ITEM.register((item, layer) -> {
				return FoliageColors.getDefaultColor();
			}, block.asItem());
		}
	}

}
