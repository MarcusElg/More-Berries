package moreberries;

import java.util.ArrayList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.LakeDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class MoreBerries implements ModInitializer {

	ItemGroup itemGroup;

	public static Block blueBerryBush;
	public static Block yellowBerryBush;
	public static Block orangeBerryBush;
	public static Block purpleBerryBush;
	public static Block greenBerryBush;
	public static Block blackBerryBush;

	public ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

	@Override
	public void onInitialize() {
		itemGroup = FabricItemGroupBuilder.create(new Identifier("moreberries", "berries")).appendItems(stacks -> {
			stacks.add(new ItemStack(Items.SWEET_BERRIES));

			for (int i = 0; i < itemStacks.size(); i++) {
				stacks.add(itemStacks.get(i));
			}
		}).icon(() -> new ItemStack(blueBerryBush)).build();

		blueBerryBush = registerBlock("blue");
		yellowBerryBush = registerBlock("yellow");
		orangeBerryBush = registerBlock("orange");
		purpleBerryBush = registerBlock("purple");
		greenBerryBush = registerBlock("green");
		blackBerryBush = registerBlock("black");

		// Sweet berry stuff
		Item item = new ItemJuice(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(3).saturationModifier(0.1f).build()).group(itemGroup));
		Registry.register(Registry.ITEM, new Identifier("moreberries", "sweet_berry_juice"), item);
		itemStacks.add(new ItemStack(item));

		item = new ItemJuicer(new Item.Settings().group(itemGroup));
		Registry.register(Registry.ITEM, new Identifier("moreberries", "juicer"), item);
		itemStacks.add(new ItemStack(item));

		item = new Item(new Item.Settings().food(FoodComponents.PUMPKIN_PIE).group(itemGroup));
		Registry.register(Registry.ITEM, new Identifier("moreberries", "sweet_berry_pie"), item);
		itemStacks.add(new ItemStack(item));

		registerGeneration(Biomes.FOREST, blueBerryBush, "blue");
		registerGeneration(Biomes.JUNGLE, greenBerryBush, "green");
		registerGeneration(Biomes.JUNGLE_EDGE, greenBerryBush, "green2");
		registerGeneration(Biomes.JUNGLE_HILLS, greenBerryBush, "green3");
		registerGeneration(Biomes.MODIFIED_JUNGLE, greenBerryBush, "green4");
		registerGeneration(Biomes.MODIFIED_JUNGLE_EDGE, greenBerryBush, "green5");
		registerGeneration(Biomes.SWAMP, purpleBerryBush, "purple");
		registerGeneration(Biomes.SWAMP_HILLS, purpleBerryBush, "purple2");
		registerGeneration(Biomes.PLAINS, blackBerryBush, "black");
		registerGeneration(Biomes.WOODED_BADLANDS_PLATEAU, orangeBerryBush, "orange");
		registerGeneration(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, orangeBerryBush, "orange2");
		registerGeneration(Biomes.BIRCH_FOREST, yellowBerryBush, "yellow");
		registerGeneration(Biomes.BIRCH_FOREST_HILLS, yellowBerryBush, "yellow2");
		registerGeneration(Biomes.TALL_BIRCH_FOREST, yellowBerryBush, "yellow3");
		registerGeneration(Biomes.TALL_BIRCH_HILLS, yellowBerryBush, "yellow4");
		
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.blueBerryBush, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.blackBerryBush, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.greenBerryBush, RenderLayer.getTranslucentNoCrumbling());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.yellowBerryBush, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.orangeBerryBush, RenderLayer.getSolid());
		BlockRenderLayerMap.INSTANCE.putBlock(MoreBerries.purpleBerryBush, RenderLayer.getTranslucent());
	}

	private Block registerBlock(String name) {
		Item berryItem = new Item(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).build()).group(itemGroup));
		Item juiceItem = new ItemJuice(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(3).saturationModifier(0.2F).build()).group(itemGroup));
		Item pieItem = new Item(new Item.Settings().food(FoodComponents.PUMPKIN_PIE).group(itemGroup));

		Block bush = new BlockBerryBush(berryItem);
		BlockItem bushItem = new BlockItem(bush, new Item.Settings().group(itemGroup));
		Block cake = new BlockBerryCake(Block.Settings.copy(Blocks.CAKE));
		BlockItem cakeItem = new BlockItem(cake, new Item.Settings().group(itemGroup));		
		
		Registry.register(Registry.ITEM, new Identifier("moreberries", name + "_berries"), berryItem);
		Registry.register(Registry.ITEM, new Identifier("moreberries", name + "_berry_juice"), juiceItem);
		Registry.register(Registry.ITEM, new Identifier("moreberries", name + "_berry_pie"), pieItem);
		Registry.register(Registry.BLOCK, new Identifier("moreberries", name + "_berry_bush"), bush);
		Registry.register(Registry.ITEM, new Identifier("moreberries", name + "_berry_bush"), bushItem);
		Registry.register(Registry.BLOCK, new Identifier("moreberries", name + "_berry_cake"), cake);
		Registry.register(Registry.ITEM, new Identifier("moreberries", name + "_berry_cake"), cakeItem);

		itemStacks.add(new ItemStack(berryItem));
		itemStacks.add(new ItemStack(juiceItem));
		itemStacks.add(new ItemStack(pieItem));
		itemStacks.add(new ItemStack(bushItem));
		itemStacks.add(new ItemStack(cakeItem));

		return bush;
	}

	private void registerGeneration(Biome biome, Block block, String name) {
		Feature<DefaultFeatureConfig> feature = new BerryFeature(DefaultFeatureConfig::deserialize,
				(BlockState) block.getDefaultState().with(SweetBerryBushBlock.AGE, 3));
		Registry.register(Registry.FEATURE, new Identifier("moreberries", name + "_berry_generation"), feature);
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, feature.method_23397(FeatureConfig.DEFAULT)
				.method_23388(Decorator.CHANCE_HEIGHTMAP_DOUBLE.method_23475(new LakeDecoratorConfig(1))));
	}
}
