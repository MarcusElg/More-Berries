package moreberries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import moreberries.config.MoreBerriesConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class MoreBerries implements ModInitializer {

	ItemGroup itemGroup;

	public static Block blueBerryBush;
	public static Block yellowBerryBush;
	public static Block orangeBerryBush;
	public static Block purpleBerryBush;
	public static Block greenBerryBush;
	public static Block blackBerryBush;

	public ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

	public static MoreBerriesConfig config;

	// Candle = Candle Cake Block
	public static HashMap<Block, CandleCakeBlock> VANILLA_CANDLES_TO_CANDLE_CAKES = new HashMap<>();

	@Override
	public void onInitialize() {
		AutoConfig.register(MoreBerriesConfig.class, JanksonConfigSerializer::new);

		config = AutoConfig.getConfigHolder(MoreBerriesConfig.class).getConfig();

		// Sweet berry stuff
		Item juicer = new ItemJuicer(new Item.Settings());
		Registry.register(Registries.ITEM, new Identifier("moreberries", "juicer"), juicer);
		itemStacks.add(new ItemStack(juicer));

		Item sweetBerryJuice = new ItemJuice(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(3).saturationModifier(0.1f).build()));
		Registry.register(Registries.ITEM, new Identifier("moreberries", "sweet_berry_juice"), sweetBerryJuice);
		itemStacks.add(new ItemStack(sweetBerryJuice));

		Item sweetBerryPie = new Item(new Item.Settings().food(FoodComponents.PUMPKIN_PIE));
		Registry.register(Registries.ITEM, new Identifier("moreberries", "sweet_berry_pie"), sweetBerryPie);
		itemStacks.add(new ItemStack(sweetBerryPie));

		// Berry stuff
		blueBerryBush = registerBlock("blue");
		yellowBerryBush = registerBlock("yellow");
		orangeBerryBush = registerBlock("orange");
		purpleBerryBush = registerBlock("purple");
		greenBerryBush = registerBlock("green");
		blackBerryBush = registerBlock("black");

		// Path node types (mobs should avoid berry bushes)
		LandPathNodeTypesRegistry.register(blueBerryBush, PathNodeType.DAMAGE_OTHER, null);
		LandPathNodeTypesRegistry.register(yellowBerryBush, PathNodeType.DAMAGE_OTHER, null);
		LandPathNodeTypesRegistry.register(orangeBerryBush, PathNodeType.DAMAGE_OTHER, null);
		LandPathNodeTypesRegistry.register(purpleBerryBush, PathNodeType.DAMAGE_OTHER, null);
		LandPathNodeTypesRegistry.register(greenBerryBush, PathNodeType.DAMAGE_OTHER, null);
		LandPathNodeTypesRegistry.register(blackBerryBush, PathNodeType.DAMAGE_OTHER, null);

		// Generation
		registerGeneration(config.blackBerrySpawnBiomes, blackBerryBush, config.blackBerrySpawnChance, "blackberry");
		registerGeneration(config.greenBerrySpawnBiomes, greenBerryBush, config.greenBerrySpawnChance, "greenberry");
		registerGeneration(config.blueBerrySpawnBiomes, blueBerryBush, config.blueBerrySpawnChance, "blueberry");
		registerGeneration(config.orangeBerrySpawnBiomes, orangeBerryBush, config.orangeBerrySpawnChance,
				"orangeberry");
		registerGeneration(config.purpleBerrySpawnBiomes, purpleBerryBush, config.purpleBerrySpawnChance,
				"purpleberry");
		registerGeneration(config.yellowBerrySpawnBiomes, yellowBerryBush, config.yellowBerrySpawnChance,
				"yellowberry");

		addVanillaCandlesToCakeMap();

		// Itemgroup
		itemGroup = FabricItemGroup.builder(new Identifier("moreberries", "berries"))
				.icon(() -> new ItemStack(blueBerryBush)).entries((enabledFeatures, entries, operatorEnabled) -> {
					entries.add(new ItemStack(Items.SWEET_BERRIES));
					entries.addAll(itemStacks);
				}).build();

		// Optional resource packs
		if (config.replaceSweetBerryBushModel) {
			ResourceManagerHelper.registerBuiltinResourcePack(
					new Identifier("moreberries", "modifiedsweetberrybushmodel"),
					FabricLoader.getInstance().getModContainer("moreberries").get(),
					Text.of("Modified Sweet Berry Bush Model"),
					ResourcePackActivationType.ALWAYS_ENABLED);
		}

		if (config.craftableBerryBushes) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("moreberries", "berrybushrecipes"),
					FabricLoader.getInstance().getModContainer("moreberries").get(), Text.of("Berry Bush Recipes"),
					ResourcePackActivationType.ALWAYS_ENABLED);
		}
	}

	private void addVanillaCandlesToCakeMap() {
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.CANDLE, (CandleCakeBlock) Blocks.CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.BLACK_CANDLE, (CandleCakeBlock) Blocks.BLACK_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.BLUE_CANDLE, (CandleCakeBlock) Blocks.BLUE_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.CYAN_CANDLE, (CandleCakeBlock) Blocks.CYAN_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.BROWN_CANDLE, (CandleCakeBlock) Blocks.BROWN_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.GRAY_CANDLE, (CandleCakeBlock) Blocks.GRAY_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.GREEN_CANDLE, (CandleCakeBlock) Blocks.GREEN_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.LIGHT_BLUE_CANDLE, (CandleCakeBlock) Blocks.LIGHT_BLUE_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.LIME_CANDLE, (CandleCakeBlock) Blocks.LIME_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.PURPLE_CANDLE, (CandleCakeBlock) Blocks.PURPLE_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.LIGHT_GRAY_CANDLE, (CandleCakeBlock) Blocks.LIGHT_GRAY_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.YELLOW_CANDLE, (CandleCakeBlock) Blocks.YELLOW_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.ORANGE_CANDLE, (CandleCakeBlock) Blocks.ORANGE_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.RED_CANDLE, (CandleCakeBlock) Blocks.RED_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.WHITE_CANDLE, (CandleCakeBlock) Blocks.WHITE_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.PINK_CANDLE, (CandleCakeBlock) Blocks.PINK_CANDLE_CAKE);
		VANILLA_CANDLES_TO_CANDLE_CAKES.put(Blocks.MAGENTA_CANDLE, (CandleCakeBlock) Blocks.MAGENTA_CANDLE_CAKE);
	}

	private void registerGeneration(String spawnBiomes, Block bushBlock, int spawnChance, String name) {
		String[] biomes = spawnBiomes.replaceAll(" ", "").split(",");

		// Configure feature
		BlockPredicate blockPredicate = BlockPredicate.allOf(BlockPredicate.IS_AIR,
				BlockPredicate.wouldSurvive(blackBerryBush.getDefaultState(), BlockPos.ORIGIN),
				BlockPredicate.not(BlockPredicate.matchingBlocks(new Vec3i(0, -1, 0), List.of(blackBerryBush,
						blueBerryBush, orangeBerryBush, greenBerryBush, purpleBerryBush, yellowBerryBush))));
		BlockStateProvider berryBushProvider = BlockStateProvider
				.of(bushBlock.getDefaultState().with(SweetBerryBushBlock.AGE, 3));
		RegistryEntry<PlacedFeature> placedFeatureEntry = PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
				new SimpleBlockFeatureConfig(
						berryBushProvider),
				blockPredicate);
		RandomPatchFeatureConfig randomPatchConfig = new RandomPatchFeatureConfig(32, 2, 3, placedFeatureEntry);
		ConfiguredFeature<RandomPatchFeatureConfig, ?> featureConfig = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				randomPatchConfig);

		// Place feature
		List<PlacementModifier> placementModifiers = List.of(RarityFilterPlacementModifier.of(spawnChance),
				PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP);
		RegistryEntry<PlacedFeature> placedFeature = PlacedFeatures.createEntry(Feature.RANDOM_PATCH, randomPatchConfig,
				placementModifiers.stream().toArray(PlacementModifier[]::new));

		// Register feature
		/*
		 * Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new
		 * Identifier("moreberries", name + "_generation"),
		 * featureConfig);
		 * Registry.register(BuiltinRegistries.PLACED_FEATURE, new
		 * Identifier("moreberries", name + "_generation"),
		 * placedFeature.getKeyOrValue().right().get());
		 * 
		 * // Add to existing biome generation
		 * ArrayList<RegistryKey<Biome>> biomeKeys = new ArrayList<>();
		 * ArrayList<TagKey<Biome>> biomeTags = new ArrayList<>();
		 * 
		 * for (String biome : biomes) {
		 * // Category
		 * if (biome.charAt(0) == '#') {
		 * biomeTags.add(TagKey.of(Registries.BIOME_KEY, new
		 * Identifier(biome.substring(1))));
		 * } else {
		 * // Biome
		 * biomeKeys.add(RegistryKey.of(Registries.BIOME_KEY, new Identifier(biome)));
		 * }
		 * }
		 * 
		 * registerBiomeGeneration(biomeKeys, biomeTags,
		 * placedFeature.getKeyOrValue().right().get());
		 */
	}

	private void registerBiomeGeneration(ArrayList<RegistryKey<Biome>> biomeKeys, ArrayList<TagKey<Biome>> biomeTags,
			PlacedFeature feature) {
		Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(biomeKeys);

		if (!biomeTags.isEmpty()) {
			for (TagKey<Biome> biomeTag : biomeTags) {
				biomeSelector = biomeSelector.or(BiomeSelectors.tag(biomeTag));
			}
		}

		/*
		 * BiomeModifications.addFeature(biomeSelector,
		 * GenerationStep.Feature.VEGETAL_DECORATION,
		 * BuiltinRegistries.PLACED_FEATURE.getKey(feature).get());
		 */
	}

	private Block registerBlock(String name) {
		// Create items
		Item berryItem = new Item(new Item.Settings()
				.food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).build()));
		Item juiceItem = null;
		juiceItem = new ItemJuice(new Item.Settings().maxCount(16)
				.food(new FoodComponent.Builder().hunger(3).saturationModifier(0.2F).build())
				.recipeRemainder(juiceItem));
		Item pieItem = new Item(new Item.Settings().food(FoodComponents.PUMPKIN_PIE));

		// Create blocks
		Block bush = new BlockBerryBush(berryItem);
		BlockItem bushItem = new BlockItem(bush, new Item.Settings());
		BlockBerryCake cake = new BlockBerryCake(Block.Settings.copy(Blocks.CAKE));
		BlockItem cakeItem = new BlockItem(cake, new Item.Settings());

		// Register items
		Registry.register(Registries.ITEM, new Identifier("moreberries", name + "_berries"), berryItem);
		Registry.register(Registries.ITEM, new Identifier("moreberries", name + "_berry_juice"), juiceItem);
		Registry.register(Registries.ITEM, new Identifier("moreberries", name + "_berry_pie"), pieItem);

		// Register blocks
		Registry.register(Registries.BLOCK, new Identifier("moreberries", name + "_berry_bush"), bush);
		Registry.register(Registries.ITEM, new Identifier("moreberries", name + "_berry_bush"), bushItem);
		Registry.register(Registries.BLOCK, new Identifier("moreberries", name + "_berry_cake"), cake);
		Registry.register(Registries.ITEM, new Identifier("moreberries", name + "_berry_cake"), cakeItem);

		itemStacks.add(new ItemStack(berryItem));
		itemStacks.add(new ItemStack(juiceItem));
		itemStacks.add(new ItemStack(pieItem));
		itemStacks.add(new ItemStack(bushItem));
		itemStacks.add(new ItemStack(cakeItem));

		// Candle cakes
		registerCandleCakes(name, cake);

		// Compost berries
		CompostingChanceRegistry.INSTANCE.add(berryItem, 0.3f);

		return bush;
	}

	// Register all 17 candle cakes for a specific berry
	private void registerCandleCakes(String berry, BlockBerryCake cakeBlock) {
		registerCandleCake(Blocks.CANDLE, cakeBlock, "", berry);
		registerCandleCake(Blocks.BLACK_CANDLE, cakeBlock, "black_", berry);
		registerCandleCake(Blocks.BLUE_CANDLE, cakeBlock, "blue_", berry);
		registerCandleCake(Blocks.BROWN_CANDLE, cakeBlock, "brown_", berry);
		registerCandleCake(Blocks.CYAN_CANDLE, cakeBlock, "cyan_", berry);
		registerCandleCake(Blocks.GRAY_CANDLE, cakeBlock, "gray_", berry);
		registerCandleCake(Blocks.GREEN_CANDLE, cakeBlock, "green_", berry);
		registerCandleCake(Blocks.LIME_CANDLE, cakeBlock, "lime_", berry);
		registerCandleCake(Blocks.MAGENTA_CANDLE, cakeBlock, "magenta_", berry);
		registerCandleCake(Blocks.ORANGE_CANDLE, cakeBlock, "orange_", berry);
		registerCandleCake(Blocks.PINK_CANDLE, cakeBlock, "pink_", berry);
		registerCandleCake(Blocks.PURPLE_CANDLE, cakeBlock, "purple_", berry);
		registerCandleCake(Blocks.RED_CANDLE, cakeBlock, "red_", berry);
		registerCandleCake(Blocks.WHITE_CANDLE, cakeBlock, "white_", berry);
		registerCandleCake(Blocks.YELLOW_CANDLE, cakeBlock, "yellow_", berry);
		registerCandleCake(Blocks.LIGHT_BLUE_CANDLE, cakeBlock, "light_blue_", berry);
		registerCandleCake(Blocks.LIGHT_GRAY_CANDLE, cakeBlock, "light_gray_", berry);
	}

	// Register a single candle cake
	private void registerCandleCake(Block candle, BlockBerryCake cake, String colour, String berry) {
		Block candleCake = new BlockCandleBerryCake(candle, cake, AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE));
		Identifier identifier = new Identifier("moreberries", colour + "candle_" + berry + "_berry_cake");
		Registry.register(Registries.BLOCK, identifier, candleCake);
	}
}
