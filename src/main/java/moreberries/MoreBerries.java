package moreberries;

import com.google.common.collect.ImmutableSet;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import moreberries.config.MoreBerriesConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.ArrayList;
import java.util.function.Predicate;

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
		registerGeneration(config.blackBerrySpawnBiomes, blackBerryBush, config.blackBerrySpawnChance, "blackberry");
		registerGeneration(config.greenBerrySpawnBiomes, greenBerryBush, config.greenBerrySpawnChance, "greenberry");
		registerGeneration(config.blueBerrySpawnBiomes, blueBerryBush, config.blueBerrySpawnChance, "blueberry");
		registerGeneration(config.orangeBerrySpawnBiomes, orangeBerryBush, config.orangeBerrySpawnChance, "orangeberry");
		registerGeneration(config.purpleBerrySpawnBiomes, purpleBerryBush, config.purpleBerrySpawnChance, "purpleberry");
		registerGeneration(config.yellowBerrySpawnBiomes, yellowBerryBush, config.yellowBerrySpawnChance, "yellowberry");
	}

	private void registerGeneration (String spawnBiomes, Block bushBlock, int spawnChance, String name) {
		String[] biomes = spawnBiomes.split(",");
		BlockState blockState = bushBlock.getDefaultState().with(SweetBerryBushBlock.AGE, 3);
		RandomPatchFeatureConfig featureConfig = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.INSTANCE)).tries(32).spreadX(2).spreadY(3).spreadZ(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).build();
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("moreberries", name + "_generation"), Feature.RANDOM_PATCH.configure(featureConfig).applyChance(spawnChance * 2));

		for(int i = 0; i < biomes.length; i++) {
			Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(RegistryKey.of(Registry.BIOME_KEY, new Identifier(biomes[i])));
			BiomeModifications.addFeature(biomeSelector, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("moreberries", name + "_generation")));
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
}
