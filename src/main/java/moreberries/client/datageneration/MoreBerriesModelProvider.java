package moreberries.client.datageneration;

import java.util.Optional;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import moreberries.block.BerryCakeBlock;
import moreberries.block.CandleBerryCakeBlock;
import moreberries.item.JuiceItem;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.BlockStateVariantMap.SingleProperty;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.item.tint.GrassTintSource;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class MoreBerriesModelProvider extends FabricModelProvider {

    public static final TextureKey BERRIES_KEY = TextureKey.of("berries");

    public MoreBerriesModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Bushes
        for (BerryBushBlock bush : MoreBerries.bushes) {
            SingleProperty<WeightedVariant, Integer> variantMap = BlockStateVariantMap.models(BerryBushBlock.AGE);
            for (int i = 0; i < 4; i++) {
                Model model = new Model(
                        Optional.of(MoreBerries.getId(String.format("block/berry_bush_stage_%d",
                                i < 1 ? 0 : 1))),
                        Optional.empty(),
                        BERRIES_KEY);
                Identifier ageIdentifier = model.upload(bush, String.format("_stage_%d", i),
                        new TextureMap().put(BERRIES_KEY,
                                i < 2 ? MoreBerries.getId(
                                        "block/empty")
                                        : TextureMap.getSubId(bush, String
                                                .format("_stage_%d",
                                                        i))),
                        blockStateModelGenerator.modelCollector);
                variantMap = variantMap.register(i, BlockStateModelGenerator.createWeightedVariant(ageIdentifier));
            }

            blockStateModelGenerator.blockStateCollector
                    .accept(VariantsBlockModelDefinitionCreator.of(bush).with(variantMap));
            blockStateModelGenerator.registerTintedItemModel(bush, TextureMap.getSubId(bush, "_stage_3"),
                    new GrassTintSource());
        }

        // Cakes
        for (BerryCakeBlock cake : MoreBerries.cakes) {
            SingleProperty<WeightedVariant, Integer> variantMap = BlockStateVariantMap.models(BerryCakeBlock.BITES);
            for (int i = 0; i < 7; i++) {
                Model model = new Model(
                        Optional.of(MoreBerries.getId(
                                String.format("block/berry_cake_slice_%d", i))),
                        Optional.empty(),
                        TextureKey.TOP);
                Identifier sliceIdentifier = model.upload(cake, String.format("_slice_%d", i),
                        new TextureMap().put(TextureKey.TOP,
                                TextureMap.getSubId(cake, "_top")),
                        blockStateModelGenerator.modelCollector);

                variantMap = variantMap.register(i, BlockStateModelGenerator.createWeightedVariant(sliceIdentifier));
            }

            blockStateModelGenerator.blockStateCollector
                    .accept(VariantsBlockModelDefinitionCreator.of(cake).with(variantMap));
        }

        // Candle cakes
        for (CandleBerryCakeBlock cake : MoreBerries.candleCakes) {
            Identifier unlitCandleCakeIdentifier = Models.TEMPLATE_CAKE_WITH_CANDLE.upload(cake,
                    getCandleCakeTextureMap(cake.cake, cake.candle, false),
                    blockStateModelGenerator.modelCollector);
            Identifier litCandleCakeIdentifier = Models.TEMPLATE_CAKE_WITH_CANDLE.upload(cake, "_lit",
                    getCandleCakeTextureMap(cake.cake, cake.candle, true),
                    blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector
                    .accept(VariantsBlockModelDefinitionCreator.of(cake).with(
                            BlockStateModelGenerator.createBooleanModelMap(Properties.LIT,
                                    BlockStateModelGenerator.createWeightedVariant(litCandleCakeIdentifier),
                                    BlockStateModelGenerator.createWeightedVariant(unlitCandleCakeIdentifier))));
        }
    }

    private TextureMap getCandleCakeTextureMap(BerryCakeBlock cakeBlock, CandleBlock candle, boolean lit) {
        return new TextureMap().put(TextureKey.PARTICLE, TextureMap.getSubId(Blocks.CAKE, "_side"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(Blocks.CAKE, "_bottom"))
                .put(TextureKey.TOP, TextureMap.getSubId(cakeBlock, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(Blocks.CAKE, "_side"))
                .put(TextureKey.CANDLE, TextureMap.getSubId(candle, lit ? "_lit" : ""));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Berries
        for (Item berry : MoreBerries.berries) {
            itemModelGenerator.register(berry, Models.GENERATED);
        }

        // Juices
        itemModelGenerator.register(MoreBerries.juicer, Models.GENERATED);
        for (JuiceItem juice : MoreBerries.juices) {
            itemModelGenerator.register(juice, Models.GENERATED);
        }

        // Pie
        for (Item pie : MoreBerries.pies) {
            itemModelGenerator.register(pie, Models.GENERATED);
        }

        // Cakes
        for (BerryCakeBlock cake : MoreBerries.cakes) {
            itemModelGenerator.register(cake.asItem(), Models.GENERATED);
        }
    }

}
