package moreberries.datageneration;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import moreberries.block.BerryCakeBlock;
import moreberries.block.CandleBerryCakeBlock;
import moreberries.item.JuiceItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.BlockStateVariantMap.SingleProperty;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class MoreBerriesModelProvider extends FabricModelProvider {

    public MoreBerriesModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Bushes
        for (BerryBushBlock bush : MoreBerries.bushes) {
            SingleProperty<Integer> variantMap = BlockStateVariantMap.create(BerryBushBlock.AGE);
            for (int i = 0; i < 4; i++) {
                Identifier ageIdentifier = TextureMap.getSubId(bush, String.format("_stage_%d", i));
                variantMap = variantMap.register(i,
                        BlockStateVariant.create().put(VariantSettings.MODEL, ageIdentifier));
            }

            blockStateModelGenerator.blockStateCollector
                    .accept(VariantsBlockStateSupplier.create(bush).coordinate(variantMap));
            blockStateModelGenerator.registerParentedItemModel(bush, TextureMap.getSubId(bush, "_stage_3"));
        }

        // Cakes
        for (BerryCakeBlock cake : MoreBerries.cakes) {
            SingleProperty<Integer> variantMap = BlockStateVariantMap.create(BerryCakeBlock.BITES);
            for (int i = 0; i < 7; i++) {
                Identifier sliceIdentifier = TextureMap.getSubId(cake, String.format("_slice_%d", i));
                variantMap = variantMap.register(i,
                        BlockStateVariant.create().put(VariantSettings.MODEL, sliceIdentifier));
            }

            blockStateModelGenerator.blockStateCollector
                    .accept(VariantsBlockStateSupplier.create(cake).coordinate(variantMap));
            blockStateModelGenerator.excludeFromSimpleItemModelGeneration(cake);
        }

        // Candle cakes
        for (CandleBerryCakeBlock cake : MoreBerries.candleCakes) {
            Identifier unlitCandleCakeIdentifier = Models.TEMPLATE_CAKE_WITH_CANDLE.upload(cake,
                    getCandleCakeTextureMap(cake.cake, cake.candle, false),
                    blockStateModelGenerator.modelCollector);
            Identifier litCandleCakeIdentifier = Models.TEMPLATE_CAKE_WITH_CANDLE.upload(cake, "_lit",
                    getCandleCakeTextureMap(cake.cake, cake.candle, true), blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(cake).coordinate(
                    BlockStateModelGenerator.createBooleanModelMap(Properties.LIT, litCandleCakeIdentifier,
                            unlitCandleCakeIdentifier)));
        }
    }

    private TextureMap getCandleCakeTextureMap(BerryCakeBlock cakeBlock, CandleBlock candle, boolean lit) {
        return new TextureMap().put(TextureKey.PARTICLE, TextureMap.getSubId(Blocks.CAKE, "_side"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(Blocks.CAKE, "_bottom"))
                .put(TextureKey.TOP, TextureMap.getId(cakeBlock))
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
