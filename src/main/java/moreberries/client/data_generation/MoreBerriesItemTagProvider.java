package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.BerryCakeBlock;
import moreberries.item.JuiceItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MoreBerriesItemTagProvider extends ItemTagProvider {

    public MoreBerriesItemTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup arg) {
        TagBuilder tagBuilder = getTagBuilder(
                TagKey.of(RegistryKeys.ITEM, Identifier.of("dehydration", "hydrating_drinks")));
        for (JuiceItem juice : MoreBerries.juices) {
            tagBuilder.add(Registries.ITEM.getId(juice));
        }
        tagBuilder = getTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("fox_food")));
        for (Item berry : MoreBerries.berries) {
            tagBuilder.add(Registries.ITEM.getId(berry));
        }

        // Nourish
        tagBuilder = getTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "carbohydrates")));
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.add(Registries.ITEM.getId(cakeBlock.asItem()));
        }
        for (Item pie : MoreBerries.pies) {
            tagBuilder.add(Registries.ITEM.getId(pie));
        }

        tagBuilder = getTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "fats")));
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.add(Registries.ITEM.getId(cakeBlock.asItem()));
        }
        tagBuilder = getTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "fruit")));
        for (Item berry : MoreBerries.berries) {
            tagBuilder.add(Registries.ITEM.getId(berry));
        }
        for (JuiceItem juice : MoreBerries.juices) {
            tagBuilder.add(Registries.ITEM.getId(juice));
        }
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.add(Registries.ITEM.getId(cakeBlock.asItem()));
        }
        for (Item pie : MoreBerries.pies) {
            tagBuilder.add(Registries.ITEM.getId(pie));
        }

        tagBuilder = getTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("nourish", "sweets")));
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.add(Registries.ITEM.getId(cakeBlock.asItem()));
        }
        for (Item pie : MoreBerries.pies) {
            tagBuilder.add(Registries.ITEM.getId(pie));
        }
    }

}
