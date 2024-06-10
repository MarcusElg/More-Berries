package moreberries.datageneration;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.item.JuiceItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MoreBerriesItemTagProvider extends ItemTagProvider {

    public MoreBerriesItemTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("dehydration", "hydrating_drinks")))
                .add(MoreBerries.juices.stream().toArray(JuiceItem[]::new));
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("fox_food")))
                .add(MoreBerries.berries.stream().toArray(Item[]::new));

        // Nourish
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "carbohydrates")))
                .add(MoreBerries.cakes.stream().map((block) -> block.asItem()).toArray(Item[]::new))
                .add(MoreBerries.pies.stream().toArray(Item[]::new));
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "fats")))
                .add(MoreBerries.cakes.stream().map((block) -> block.asItem()).toArray(Item[]::new));
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "fruit")))
                .add(MoreBerries.berries.stream().toArray(Item[]::new))
                .add(MoreBerries.juices.stream().toArray(Item[]::new))
                .add(MoreBerries.cakes.stream().map((block) -> block.asItem()).toArray(Item[]::new))
                .add(MoreBerries.pies.stream().toArray(Item[]::new));
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "sweets")))
                .add(MoreBerries.cakes.stream().map((block) -> block.asItem()).toArray(Item[]::new))
                .add(MoreBerries.pies.stream().toArray(Item[]::new));
    }

}
