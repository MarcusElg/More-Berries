package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.CandleBerryCakeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

public class MoreBerriesBlockTagProvider extends BlockTagProvider {

    public MoreBerriesBlockTagProvider(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(Provider arg) {
        TagBuilder tagBuilder = getOrCreateRawBuilder(TagKey.create(Registries.BLOCK, Identifier.parse("candle_cakes")));
        for (CandleBerryCakeBlock cakeBlock : MoreBerries.candleCakes) {
            tagBuilder.addElement(BuiltInRegistries.BLOCK.getKey(cakeBlock));
        }
    }

}
