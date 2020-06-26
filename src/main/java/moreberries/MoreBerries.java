package moreberries;

import java.util.ArrayList;

import com.google.common.collect.ImmutableSet;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import moreberries.config.MoreBerriesConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
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
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

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
		AutoConfig.register(MoreBerriesConfig.class, JanksonConfigSerializer::new);

		itemGroup = FabricItemGroupBuilder.create(new Identifier("moreberries", "berries")).appendItems(stacks -> {
			stacks.add(new ItemStack(Items.SWEET_BERRIES));

			for (int i = 0; i < itemStacks.size(); i++) {
				stacks.add(itemStacks.get(i));
			}
		}).icon(() -> new ItemStack(blueBerryBush)).build();

		MoreBerriesConfig config = AutoConfig.getConfigHolder(MoreBerriesConfig.class).getConfig();

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

		// Generation
		String[] biomes = config.blackBerrySpawnBiomes.split(",");
		for(int i = 0; i < biomes.length; i++) {
			String[] biome = biomes[i].split(":");
			registerGeneration(Registry.BIOME.get(new Identifier(biome[0], biome[1])), blackBerryBush, config.blackBerrySpawnChance);
		}

		biomes = config.blueBerrySpawnBiomes.split(",");
		for(int i = 0; i < biomes.length; i++) {
			String[] biome = biomes[i].split(":");
			registerGeneration(Registry.BIOME.get(new Identifier(biome[0], biome[1])), blueBerryBush, config.blueBerrySpawnChance);
		}

		biomes = config.greenBerrySpawnBiomes.split(",");
		for(int i = 0; i < biomes.length; i++) {
			String[] biome = biomes[i].split(":");
			registerGeneration(Registry.BIOME.get(new Identifier(biome[0], biome[1])), greenBerryBush, config.greenBerrySpawnChance);
		}

		biomes = config.orangeBerrySpawnBiomes.split(",");
		for(int i = 0; i < biomes.length; i++) {
			String[] biome = biomes[i].split(":");
			registerGeneration(Registry.BIOME.get(new Identifier(biome[0], biome[1])), orangeBerryBush, config.orangeBerrySpawnChance);
		}

		biomes = config.purpleBerrySpawnBiomes.split(",");
		for(int i = 0; i < biomes.length; i++) {
			String[] biome = biomes[i].split(":");
			registerGeneration(Registry.BIOME.get(new Identifier(biome[0], biome[1])), purpleBerryBush, config.purpleBerrySpawnChance);
		}

		biomes = config.yellowBerrySpawnBiomes.split(",");
		for(int i = 0; i < biomes.length; i++) {
			String[] biome = biomes[i].split(":");
			registerGeneration(Registry.BIOME.get(new Identifier(biome[0], biome[1])), yellowBerryBush, config.yellowBerrySpawnChance);
		}
	}

	private Block registerBlock(String name) {
		Item berryItem = new Item(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).build()).group(itemGroup));
		Item juiceItem = null;
		juiceItem = new ItemJuice(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(3).saturationModifier(0.2F).build()).recipeRemainder(juiceItem).group(itemGroup));
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

	private void registerGeneration(Biome biome, Block block, int chance) {
		BlockState blockState = block.getDefaultState().with(SweetBerryBushBlock.AGE, 3);
		RandomPatchFeatureConfig config = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.field_24871)).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();
		biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(config)
				.createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(chance))));
	}
}
