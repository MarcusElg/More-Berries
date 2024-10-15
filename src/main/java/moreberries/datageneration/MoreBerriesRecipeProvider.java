package moreberries.datageneration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

public class MoreBerriesRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesRecipeProvider(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries,
            RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                // Juicer
                ShapedRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.TOOLS, MoreBerries.juicer).pattern("#")
                        .pattern("_")
                        .input('#', Blocks.STONE_BUTTON).input('_', Blocks.STONE_SLAB)
                        .criterion(RecipeGenerator.hasItem(Blocks.STONE_SLAB),
                                this
                                        .conditionsFromItem(Blocks.STONE_SLAB))
                        .offerTo(exporter);

                // Juices
                ShapelessRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.FOOD, MoreBerries.juices.get(0))
                        .input(MoreBerries.juicer)
                        .input(Items.GLASS_BOTTLE).input(Items.SWEET_BERRIES).group("juices")
                        .criterion(RecipeGenerator.hasItem(Items.SWEET_BERRIES),
                                this.conditionsFromItem(
                                        Items.SWEET_BERRIES))
                        .offerTo(exporter);

                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapelessRecipeJsonBuilder
                            .create(Registries.ITEM, RecipeCategory.FOOD, MoreBerries.juices.get(i + 1))
                            .input(MoreBerries.juicer)
                            .input(Items.GLASS_BOTTLE).input(MoreBerries.berries.get(i))
                            .group("juices")
                            .criterion(RecipeGenerator
                                    .hasItem(MoreBerries.berries.get(i)),
                                    this
                                            .conditionsFromItem(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .offerTo(exporter);
                }

                // Pies
                ShapelessRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.FOOD, MoreBerries.pies.get(0))
                        .input(Items.SUGAR).input(Items.EGG)
                        .input(Items.SWEET_BERRIES).group("pies")
                        .criterion(RecipeGenerator.hasItem(Items.SWEET_BERRIES),
                                this.conditionsFromItem(
                                        Items.SWEET_BERRIES))
                        .offerTo(exporter);

                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapelessRecipeJsonBuilder
                            .create(Registries.ITEM, RecipeCategory.FOOD, MoreBerries.pies.get(i + 1))
                            .input(Items.SUGAR).input(Items.EGG)
                            .input(MoreBerries.berries.get(i)).group("pies")
                            .criterion(RecipeGenerator
                                    .hasItem(MoreBerries.berries.get(i)),
                                    this
                                            .conditionsFromItem(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .offerTo(exporter);
                }

                // Cakes
                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapedRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.FOOD, MoreBerries.cakes.get(i))
                            .pattern("MBM")
                            .pattern("SES")
                            .pattern("WWW").input('M', Items.MILK_BUCKET)
                            .input('S', Items.SUGAR)
                            .input('W', Items.WHEAT)
                            .input('E', Items.EGG).input('B', MoreBerries.berries.get(i))
                            .group("cakes")
                            .criterion(RecipeGenerator
                                    .hasItem(MoreBerries.berries.get(i)),
                                    this
                                            .conditionsFromItem(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .offerTo(exporter);
                }

                // Dyes
                ShapelessRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.MISC, Items.RED_DYE)
                        .input(Items.SWEET_BERRIES).group("dyes")
                        .criterion(RecipeGenerator.hasItem(Items.SWEET_BERRIES),
                                this.conditionsFromItem(
                                        Items.SWEET_BERRIES))
                        .offerTo(exporter);

                List<Item> dyes = Arrays.asList(Items.BLUE_DYE, Items.YELLOW_DYE, Items.ORANGE_DYE,
                        Items.PURPLE_DYE,
                        Items.GREEN_DYE, Items.BLACK_DYE);
                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapelessRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.MISC, dyes.get(i))
                            .input(MoreBerries.berries.get(i)).group("dyes")
                            .criterion(RecipeGenerator
                                    .hasItem(MoreBerries.berries.get(i)),
                                    this
                                            .conditionsFromItem(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .offerTo(exporter);
                }
            }
        };
    }

    @Override
    public String getName() {
        return MoreBerries.MOD_ID;
    }
}
