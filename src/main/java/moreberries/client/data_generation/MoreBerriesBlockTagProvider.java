package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.CandleBerryCakeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MoreBerriesBlockTagProvider extends BlockTagProvider {

    public MoreBerriesBlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        TagBuilder tagBuilder = getTagBuilder(TagKey.of(RegistryKeys.BLOCK, Identifier.of("candle_cakes")));
        for (CandleBerryCakeBlock cakeBlock : MoreBerries.candleCakes) {
            tagBuilder.add(Registries.BLOCK.getId(cakeBlock));
        }
    }

}
