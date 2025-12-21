package moreberries.client.data_generation;

import java.util.Optional;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import moreberries.block.BerryCakeBlock;
import moreberries.block.CandleBerryCakeBlock;
import moreberries.item.JuiceItem;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.PropertyDispatch.C1;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MoreBerriesModelProvider extends FabricModelProvider {

    public static final TextureSlot BERRIES_KEY = TextureSlot.create("berries");

    public MoreBerriesModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        // Bushes
        for (BerryBushBlock bush : MoreBerries.bushes) {
            C1<MultiVariant, Integer> variantMap = PropertyDispatch.initial(BerryBushBlock.AGE);
            for (int i = 0; i < 4; i++) {
                ModelTemplate model = new ModelTemplate(
                        Optional.of(MoreBerries.getId(String.format("block/berry_bush_stage_%d",
                                i < 1 ? 0 : 1))),
                        Optional.empty(),
                        BERRIES_KEY);
                Identifier ageIdentifier = model.createWithSuffix(bush, String.format("_stage_%d", i),
                        new TextureMapping().put(BERRIES_KEY,
                                i < 2 ? MoreBerries.getId(
                                        "block/empty")
                                        : TextureMapping.getBlockTexture(bush, String
                                                .format("_stage_%d",
                                                        i))),
                        blockStateModelGenerator.modelOutput);
                variantMap = variantMap.select(i, BlockModelGenerators.plainVariant(ageIdentifier));
            }

            blockStateModelGenerator.blockStateOutput
                    .accept(MultiVariantGenerator.dispatch(bush).with(variantMap));
            blockStateModelGenerator.registerSimpleTintedItemModel(bush, TextureMapping.getBlockTexture(bush, "_stage_3"),
                    new GrassColorSource());
        }

        // Cakes
        for (BerryCakeBlock cake : MoreBerries.cakes) {
            C1<MultiVariant, Integer> variantMap = PropertyDispatch.initial(BerryCakeBlock.BITES);
            for (int i = 0; i < 7; i++) {
                ModelTemplate model = new ModelTemplate(
                        Optional.of(MoreBerries.getId(
                                String.format("block/berry_cake_slice_%d", i))),
                        Optional.empty(),
                        TextureSlot.TOP);
                Identifier sliceIdentifier = model.createWithSuffix(cake, String.format("_slice_%d", i),
                        new TextureMapping().put(TextureSlot.TOP,
                                TextureMapping.getBlockTexture(cake, "_top")),
                        blockStateModelGenerator.modelOutput);

                variantMap = variantMap.select(i, BlockModelGenerators.plainVariant(sliceIdentifier));
            }

            blockStateModelGenerator.blockStateOutput
                    .accept(MultiVariantGenerator.dispatch(cake).with(variantMap));
        }

        // Candle cakes
        for (CandleBerryCakeBlock cake : MoreBerries.candleCakes) {
            Identifier unlitCandleCakeIdentifier = ModelTemplates.CANDLE_CAKE.create(cake,
                    getCandleCakeTextureMap(cake.cake, cake.candle, false),
                    blockStateModelGenerator.modelOutput);
            Identifier litCandleCakeIdentifier = ModelTemplates.CANDLE_CAKE.createWithSuffix(cake, "_lit",
                    getCandleCakeTextureMap(cake.cake, cake.candle, true),
                    blockStateModelGenerator.modelOutput);
            blockStateModelGenerator.blockStateOutput
                    .accept(MultiVariantGenerator.dispatch(cake).with(
                            BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT,
                                    BlockModelGenerators.plainVariant(litCandleCakeIdentifier),
                                    BlockModelGenerators.plainVariant(unlitCandleCakeIdentifier))));
        }
    }

    private TextureMapping getCandleCakeTextureMap(BerryCakeBlock cakeBlock, CandleBlock candle, boolean lit) {
        return new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.CAKE, "_side"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.CAKE, "_bottom"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(cakeBlock, "_top"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CAKE, "_side"))
                .put(TextureSlot.CANDLE, TextureMapping.getBlockTexture(candle, lit ? "_lit" : ""));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        // Berries
        for (Item berry : MoreBerries.berries) {
            itemModelGenerator.generateFlatItem(berry, ModelTemplates.FLAT_ITEM);
        }

        // Juices
        itemModelGenerator.generateFlatItem(MoreBerries.juicer, ModelTemplates.FLAT_ITEM);
        for (JuiceItem juice : MoreBerries.juices) {
            itemModelGenerator.generateFlatItem(juice, ModelTemplates.FLAT_ITEM);
        }

        // Pie
        for (Item pie : MoreBerries.pies) {
            itemModelGenerator.generateFlatItem(pie, ModelTemplates.FLAT_ITEM);
        }

        // Cakes
        for (BerryCakeBlock cake : MoreBerries.cakes) {
            itemModelGenerator.generateFlatItem(cake.asItem(), ModelTemplates.FLAT_ITEM);
        }
    }

}
