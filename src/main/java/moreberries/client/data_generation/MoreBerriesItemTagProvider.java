package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.BerryCakeBlock;
import moreberries.item.JuiceItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MoreBerriesItemTagProvider extends ItemTagProvider {

    public MoreBerriesItemTagProvider(FabricDataOutput output, CompletableFuture<Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(Provider arg) {
        TagBuilder tagBuilder = getOrCreateRawBuilder(
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("dehydration", "hydrating_drinks")));
        for (JuiceItem juice : MoreBerries.juices) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(juice));
        }
        tagBuilder = getOrCreateRawBuilder(TagKey.create(Registries.ITEM, Identifier.parse("fox_food")));
        for (Item berry : MoreBerries.berries) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(berry));
        }

        // Nourish
        tagBuilder = getOrCreateRawBuilder(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nourish", "carbohydrates")));
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(cakeBlock.asItem()));
        }
        for (Item pie : MoreBerries.pies) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(pie));
        }

        tagBuilder = getOrCreateRawBuilder(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nourish", "fats")));
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(cakeBlock.asItem()));
        }
        tagBuilder = getOrCreateRawBuilder(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nourish", "fruit")));
        for (Item berry : MoreBerries.berries) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(berry));
        }
        for (JuiceItem juice : MoreBerries.juices) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(juice));
        }
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(cakeBlock.asItem()));
        }
        for (Item pie : MoreBerries.pies) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(pie));
        }

        tagBuilder = getOrCreateRawBuilder(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("nourish", "sweets")));
        for (BerryCakeBlock cakeBlock : MoreBerries.cakes) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(cakeBlock.asItem()));
        }
        for (Item pie : MoreBerries.pies) {
            tagBuilder.addElement(BuiltInRegistries.ITEM.getKey(pie));
        }
    }

}
