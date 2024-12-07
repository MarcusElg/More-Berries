package moreberries.client.datageneration;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.CandleBerryCakeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MoreBerriesBlockTagProvider extends BlockTagProvider {

    public MoreBerriesBlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, Identifier.of("candle_cakes")))
                .add(MoreBerries.candleCakes.stream().toArray(CandleBerryCakeBlock[]::new));
    }

}
