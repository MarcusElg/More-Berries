package moreberries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import moreberries.block.BerryBushBlock;
import moreberries.block.BerryCakeBlock;
import moreberries.block.CandleBerryCakeBlock;
import moreberries.config.MoreBerriesConfig;
import moreberries.item.JuiceItem;
import moreberries.item.JuicerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
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
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;

public class MoreBerries implements ModInitializer {

	public static final String MOD_ID = "moreberries";

	public ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

	// Blocks
	public static BerryBushBlock blueBerryBush;
	public static BerryBushBlock yellowBerryBush;
	public static BerryBushBlock orangeBerryBush;
	public static BerryBushBlock purpleBerryBush;
	public static BerryBushBlock greenBerryBush;
	public static BerryBushBlock blackBerryBush;

	public static ArrayList<BerryBushBlock> bushes = new ArrayList<>();
	public static ArrayList<BerryCakeBlock> cakes = new ArrayList<>();
	public static ArrayList<CandleBerryCakeBlock> candleCakes = new ArrayList<>();

	// Items
	public static ArrayList<Item> berries = new ArrayList<>();
	public static ArrayList<JuiceItem> juices = new ArrayList<>();
	public static ArrayList<Item> pies = new ArrayList<>();
	public static Item juicer;

	public static MoreBerriesConfig config;

	// Candle -> Candle Cake Block
	public static HashMap<Block, CandleCakeBlock> VANILLA_CANDLES_TO_CANDLE_CAKES = new HashMap<>();

	@Override
	public void onInitialize() {
		AutoConfig.register(MoreBerriesConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(MoreBerriesConfig.class).getConfig();

		// Sweet berry stuff
		juicer = new JuicerItem(new Item.Settings());
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "juicer"), juicer);
		itemStacks.add(new ItemStack(juicer));

		JuiceItem sweetBerryJuice = new JuiceItem(new Item.Settings()
				.food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.1f).build()));
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "sweet_berry_juice"), sweetBerryJuice);
		itemStacks.add(new ItemStack(sweetBerryJuice));
		juices.add(sweetBerryJuice);

		Item sweetBerryPie = new Item(new Item.Settings().food(FoodComponents.PUMPKIN_PIE));
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "sweet_berry_pie"), sweetBerryPie);
		itemStacks.add(new ItemStack(sweetBerryPie));
		pies.add(sweetBerryPie);

		// Berry stuff
		blueBerryBush = registerBerryType("blue");
		yellowBerryBush = registerBerryType("yellow");
		orangeBerryBush = registerBerryType("orange");
		purpleBerryBush = registerBerryType("purple");
		greenBerryBush = registerBerryType("green");
		blackBerryBush = registerBerryType("black");

		// Path node types (mobs should avoid berry bushes)
		for (BerryBushBlock bush : bushes) {
			LandPathNodeTypesRegistry.register(bush, PathNodeType.DAMAGE_OTHER, null);
		}

		// Generation
		registerBiomeGeneration(config.blackBerrySpawnBiomes, blackBerryBush,
				"black_berry");
		registerBiomeGeneration(config.greenBerrySpawnBiomes, greenBerryBush,
				"green_berry");
		registerBiomeGeneration(config.blueBerrySpawnBiomes, blueBerryBush,
				"blue_berry");
		registerBiomeGeneration(config.orangeBerrySpawnBiomes, orangeBerryBush,
				"orange_berry");
		registerBiomeGeneration(config.purpleBerrySpawnBiomes, purpleBerryBush,
				"purple_berry");
		registerBiomeGeneration(config.yellowBerrySpawnBiomes, yellowBerryBush,
				"yellow_berry");

		// Itemgroup
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "berries"), FabricItemGroup.builder()
				.icon(() -> new ItemStack(blueBerryBush))
				.displayName(Text.translatable("itemGroup.moreberries.berries"))
				.entries((context, entries) -> {
					entries.add(new ItemStack(Items.SWEET_BERRIES));
					entries.addAll(itemStacks);
				})
				.build());

		// Optional resource packs
		if (config.replaceSweetBerryBushModel) {
			ResourceManagerHelper.registerBuiltinResourcePack(
					new Identifier(MOD_ID, "modifiedsweetberrybushmodel"),
					FabricLoader.getInstance().getModContainer(MOD_ID).get(),
					Text.of("Modified Sweet Berry Bush Model"),
					ResourcePackActivationType.ALWAYS_ENABLED);
		}

		if (config.craftableBerryBushes) {
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(MOD_ID, "berrybushrecipes"),
					FabricLoader.getInstance().getModContainer(MOD_ID).get(), Text.of("Berry Bush Recipes"),
					ResourcePackActivationType.ALWAYS_ENABLED);
		}
	}

	// Adds berry bushes to vanilla biomes
	private void registerBiomeGeneration(String spawnBiomes, Block bushBlock, String name) {
		String[] biomes = spawnBiomes.replaceAll(" ", "").split(",");

		// Get list of spawn biomes
		ArrayList<RegistryKey<Biome>> biomeKeys = new ArrayList<>();
		ArrayList<TagKey<Biome>> biomeTags = new ArrayList<>();

		for (String biome : biomes) {
			// Category
			if (biome.charAt(0) == '#') {
				biomeTags.add(TagKey.of(RegistryKeys.BIOME, new Identifier(biome.substring(1))));
			} else {
				// Biome
				biomeKeys.add(RegistryKey.of(RegistryKeys.BIOME, new Identifier(biome)));
			}
		}

		Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.includeByKey(biomeKeys);

		if (!biomeTags.isEmpty()) {
			for (TagKey<Biome> biomeTag : biomeTags) {
				biomeSelector = biomeSelector.or(BiomeSelectors.tag(biomeTag));
			}
		}

		// Add to biomes
		BiomeModifications.addFeature(biomeSelector,
				GenerationStep.Feature.VEGETAL_DECORATION,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE,
						new Identifier(MOD_ID, String.format("%s_generation", name))));
	}

	private BerryBushBlock registerBerryType(String name) {
		// Create items
		Item berryItem = new Item(new Item.Settings()
				.food(new FoodComponent.Builder().nutrition(2).saturationModifier(0.1f).build()));
		JuiceItem juiceItem = new JuiceItem(new Item.Settings().maxCount(16)
				.food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.2F).build()));
		Item pieItem = new Item(new Item.Settings().food(FoodComponents.PUMPKIN_PIE));

		// Create blocks
		BerryBushBlock bush = new BerryBushBlock(berryItem);
		BlockItem bushItem = new BlockItem(bush, new Item.Settings());
		BerryCakeBlock cake = new BerryCakeBlock(Block.Settings.copy(Blocks.CAKE));
		BlockItem cakeItem = new BlockItem(cake, new Item.Settings());

		// Register items
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, String.format("%s_berries", name)), berryItem);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, String.format("%s_berry_juice", name)),
				juiceItem);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, String.format("%s_berry_pie", name)), pieItem);

		// Register blocks
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, String.format("%s_berry_bush", name)), bush);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, String.format("%s_berry_bush", name)),
				bushItem);
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, String.format("%s_berry_cake", name)), cake);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, String.format("%s_berry_cake", name)),
				cakeItem);

		// Save items
		berries.add(berryItem);
		juices.add(juiceItem);
		pies.add(pieItem);

		// Save blocks
		bushes.add(bush);
		cakes.add(cake);

		// Add to creativetab
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
	private void registerCandleCakes(String berry, BerryCakeBlock cakeBlock) {
		registerCandleCake((CandleBlock) Blocks.CANDLE, cakeBlock, "", berry);
		registerCandleCake((CandleBlock) Blocks.BLACK_CANDLE, cakeBlock, "black_", berry);
		registerCandleCake((CandleBlock) Blocks.BLUE_CANDLE, cakeBlock, "blue_", berry);
		registerCandleCake((CandleBlock) Blocks.BROWN_CANDLE, cakeBlock, "brown_", berry);
		registerCandleCake((CandleBlock) Blocks.CYAN_CANDLE, cakeBlock, "cyan_", berry);
		registerCandleCake((CandleBlock) Blocks.GRAY_CANDLE, cakeBlock, "gray_", berry);
		registerCandleCake((CandleBlock) Blocks.GREEN_CANDLE, cakeBlock, "green_", berry);
		registerCandleCake((CandleBlock) Blocks.LIME_CANDLE, cakeBlock, "lime_", berry);
		registerCandleCake((CandleBlock) Blocks.MAGENTA_CANDLE, cakeBlock, "magenta_", berry);
		registerCandleCake((CandleBlock) Blocks.ORANGE_CANDLE, cakeBlock, "orange_", berry);
		registerCandleCake((CandleBlock) Blocks.PINK_CANDLE, cakeBlock, "pink_", berry);
		registerCandleCake((CandleBlock) Blocks.PURPLE_CANDLE, cakeBlock, "purple_", berry);
		registerCandleCake((CandleBlock) Blocks.RED_CANDLE, cakeBlock, "red_", berry);
		registerCandleCake((CandleBlock) Blocks.WHITE_CANDLE, cakeBlock, "white_", berry);
		registerCandleCake((CandleBlock) Blocks.YELLOW_CANDLE, cakeBlock, "yellow_", berry);
		registerCandleCake((CandleBlock) Blocks.LIGHT_BLUE_CANDLE, cakeBlock, "light_blue_", berry);
		registerCandleCake((CandleBlock) Blocks.LIGHT_GRAY_CANDLE, cakeBlock, "light_gray_", berry);
	}

	// Register a single candle cake
	private void registerCandleCake(CandleBlock candle, BerryCakeBlock cake, String colour, String berry) {
		CandleBerryCakeBlock candleCake = new CandleBerryCakeBlock(candle, cake,
				AbstractBlock.Settings.copy(Blocks.CANDLE_CAKE));
		Identifier identifier = new Identifier(MOD_ID, String.format("%scandle_%s_berry_cake", colour, berry));
		Registry.register(Registries.BLOCK, identifier, candleCake);
		candleCakes.add(candleCake);
	}
}
