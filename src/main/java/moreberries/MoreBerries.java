package moreberries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import moreberries.block.BerryBushBlock;
import moreberries.block.BerryCakeBlock;
import moreberries.block.CandleBerryCakeBlock;
import moreberries.config.CraftableBerryBushesResourceCondition;
import moreberries.config.MoreBerriesConfig;
import moreberries.item.JuiceItem;
import moreberries.item.JuicerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.LandPathTypeRegistry;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathType;

public class MoreBerries implements ModInitializer {

    public static final String MOD_ID = "moreberries";

    public ArrayList<ItemLike> creativeTabItems = new ArrayList<>();

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

    // Resource conditions
    public static final ResourceConditionType<CraftableBerryBushesResourceCondition> CRAFTABLE_BERRIES_RESOURCE_CONDITION = ResourceConditionType
            .create(getId("craftable_berry_bushes"), CraftableBerryBushesResourceCondition.CODEC);

    public static Identifier getId(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(MoreBerriesConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MoreBerriesConfig.class).getConfig();

        // Sweet berry stuff
        juicer = new JuicerItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, getId("juicer"))));
        Registry.register(BuiltInRegistries.ITEM, getId("juicer"), juicer);
        creativeTabItems.add(juicer);

        JuiceItem sweetBerryJuice = new JuiceItem(
                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, getId("sweet_berry_juice")))
                        .food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.1f).build(),
                                Consumables.DEFAULT_DRINK));
        Registry.register(BuiltInRegistries.ITEM, getId("sweet_berry_juice"), sweetBerryJuice);
        creativeTabItems.add(sweetBerryJuice);
        juices.add(sweetBerryJuice);

        Item sweetBerryPie = new Item(new Item.Properties().food(Foods.PUMPKIN_PIE)
                .setId(ResourceKey.create(Registries.ITEM, getId("sweet_berry_pie"))));
        Registry.register(BuiltInRegistries.ITEM, getId("sweet_berry_pie"), sweetBerryPie);
        creativeTabItems.add(sweetBerryPie);
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
            LandPathTypeRegistry.register(bush, PathType.DAMAGING, null);
        }

        // Generation
        registerBiomeGeneration(config.blackBerrySpawnBiomes, "black_berry");
        registerBiomeGeneration(config.greenBerrySpawnBiomes, "green_berry");
        registerBiomeGeneration(config.blueBerrySpawnBiomes, "blue_berry");
        registerBiomeGeneration(config.orangeBerrySpawnBiomes, "orange_berry");
        registerBiomeGeneration(config.purpleBerrySpawnBiomes, "purple_berry");
        registerBiomeGeneration(config.yellowBerrySpawnBiomes, "yellow_berry");

        // Creative tab
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, getId("berries"), FabricCreativeModeTab.builder()
                .icon(() -> new ItemStack(blueBerryBush))
                .title(Component.translatable("itemGroup.moreberries.berries"))
                .displayItems((context, entries) -> {
                    entries.accept(new ItemStack(Items.SWEET_BERRIES));

                    for (ItemLike item : creativeTabItems) {
                        entries.accept(item);
                    }
                })
                .build());

        // Optional resource packs
        if (config.replaceSweetBerryBushModel) {
            ResourceLoader.registerBuiltinPack(
                    getId("modifiedsweetberrybushmodel"),
                    FabricLoader.getInstance().getModContainer(MOD_ID).get(),
                    Component.nullToEmpty("Modified Sweet Berry Bush Model"),
                    PackActivationType.ALWAYS_ENABLED);
        }

        // Resource conditions
        ResourceConditions.register(CRAFTABLE_BERRIES_RESOURCE_CONDITION);
    }

    // Adds berry bushes to vanilla biomes
    private void registerBiomeGeneration(String spawnBiomes, String name) {
        String[] biomes = spawnBiomes.replaceAll(" ", "").split(",");

        // Get list of spawn biomes
        ArrayList<ResourceKey<Biome>> biomeKeys = new ArrayList<>();
        ArrayList<TagKey<Biome>> biomeTags = new ArrayList<>();

        for (String biome : biomes) {
            // Category
            if (biome.charAt(0) == '#') {
                biomeTags.add(TagKey.create(Registries.BIOME, Identifier.parse(biome.substring(1))));
            } else {
                // Biome
                biomeKeys.add(ResourceKey.create(Registries.BIOME, Identifier.parse(biome)));
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
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE,
                        getId(String.format("%s_generation", name))));
    }

    private BerryBushBlock registerBerryType(String name) {
        // Create items
        Item berryItem = new Item(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, getId(String.format("%s_berries", name))))
                .food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1f).build()));
        JuiceItem juiceItem = new JuiceItem(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, getId(String.format("%s_berry_juice", name))))
                .stacksTo(16)
                .food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.2F).build()));
        Item pieItem = new Item(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, getId(String.format("%s_berry_pie", name))))
                .food(Foods.PUMPKIN_PIE));

        // Create blocks
        BerryBushBlock bush = new BerryBushBlock(berryItem,
                BlockBehaviour.Properties.of()
                        .setId(ResourceKey.create(Registries.BLOCK, getId(String.format("%s_berry_bush", name))))
                        .mapColor(MapColor.PLANT).randomTicks().noCollision()
                        .sound(SoundType.SWEET_BERRY_BUSH).noOcclusion());
        BlockItem bushItem = new BlockItem(bush, new Item.Properties().useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, getId(String.format("%s_berry_bush", name)))));
        BerryCakeBlock cake = new BerryCakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)
                .setId(ResourceKey.create(Registries.BLOCK, getId(String.format("%s_berry_cake", name)))));
        BlockItem cakeItem = new BlockItem(cake, new Item.Properties().useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, getId(String.format("%s_berry_cake", name)))));

        // Register items
        Registry.register(BuiltInRegistries.ITEM, getId(String.format("%s_berries", name)), berryItem);
        Registry.register(BuiltInRegistries.ITEM, getId(String.format("%s_berry_juice", name)),
                juiceItem);
        Registry.register(BuiltInRegistries.ITEM, getId(String.format("%s_berry_pie", name)), pieItem);

        // Register blocks
        Registry.register(BuiltInRegistries.BLOCK, getId(String.format("%s_berry_bush", name)), bush);
        Registry.register(BuiltInRegistries.ITEM, getId(String.format("%s_berry_bush", name)),
                bushItem);
        Registry.register(BuiltInRegistries.BLOCK, getId(String.format("%s_berry_cake", name)), cake);
        Registry.register(BuiltInRegistries.ITEM, getId(String.format("%s_berry_cake", name)),
                cakeItem);

        // Save items
        berries.add(berryItem);
        juices.add(juiceItem);
        pies.add(pieItem);

        // Save blocks
        bushes.add(bush);
        cakes.add(cake);

        // Add to creativetab
        creativeTabItems.add(berryItem);
        creativeTabItems.add(juiceItem);
        creativeTabItems.add(pieItem);
        creativeTabItems.add(bushItem);
        creativeTabItems.add(cakeItem);

        // Candle cakes
        registerCandleCakes(name, cake);

        // Compost berries
        CompostableRegistry.INSTANCE.add(berryItem, 0.3f);

        return bush;
    }

    // Register all 17 candle cakes for a specific berry
    private void registerCandleCakes(String berry, BerryCakeBlock cakeBlock) {
        registerCandleCake((CandleBlock) Blocks.CANDLE, cakeBlock, "", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.black(), cakeBlock, "black", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.blue(), cakeBlock, "blue", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.brown(), cakeBlock, "brown", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.cyan(), cakeBlock, "cyan", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.gray(), cakeBlock, "gray", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.green(), cakeBlock, "green", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.lime(), cakeBlock, "lime", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.magenta(), cakeBlock, "magenta", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.orange(), cakeBlock, "orange", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.pink(), cakeBlock, "pink", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.purple(), cakeBlock, "purple", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.red(), cakeBlock, "red", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.white(), cakeBlock, "white", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.yellow(), cakeBlock, "yellow", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.lightBlue(), cakeBlock, "light_blue", berry);
        registerCandleCake((CandleBlock) Blocks.DYED_CANDLE.lightGray(), cakeBlock, "light_gray", berry);
    }

    // Register a single candle cake
    private void registerCandleCake(CandleBlock candle, BerryCakeBlock cake, String colour, String berry) {
        CandleBerryCakeBlock candleCake = new CandleBerryCakeBlock(candle, cake,
                BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE).setId(ResourceKey.create(Registries.BLOCK,
                        getId(String.format("%s_candle_%s_berry_cake", colour, berry).replaceFirst("^_+", "")))));
        Identifier identifier = getId(String.format("%s_candle_%s_berry_cake", colour, berry).replaceFirst("^_+", ""));
        Registry.register(BuiltInRegistries.BLOCK, identifier, candleCake);
        candleCakes.add(candleCake);
    }
}
