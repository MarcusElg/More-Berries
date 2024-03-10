package moreberries.datageneration;

import java.util.Arrays;
import java.util.List;

import moreberries.MoreBerries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class MoreBerriesRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // Juicer
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, MoreBerries.juicer).pattern("#").pattern("_")
                .input('#', Blocks.STONE_BUTTON).input('_', Blocks.STONE_SLAB)
                .criterion(FabricRecipeProvider.hasItem(Blocks.STONE_SLAB),
                        FabricRecipeProvider.conditionsFromItem(Blocks.STONE_SLAB))
                .offerTo(exporter);

        // Juices
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.juices.get(0))
                .input(MoreBerries.juicer)
                .input(Items.GLASS_BOTTLE).input(Items.SWEET_BERRIES).group("juices")
                .criterion(FabricRecipeProvider.hasItem(Items.SWEET_BERRIES),
                        FabricRecipeProvider.conditionsFromItem(Items.SWEET_BERRIES))
                .offerTo(exporter);

        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.juices.get(i + 1))
                    .input(MoreBerries.juicer)
                    .input(Items.GLASS_BOTTLE).input(MoreBerries.berries.get(i)).group("juices")
                    .criterion(FabricRecipeProvider.hasItem(MoreBerries.berries.get(i)),
                            FabricRecipeProvider
                                    .conditionsFromItem(MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }

        // Pies
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.pies.get(0))
                .input(Items.SUGAR).input(Items.EGG)
                .input(Items.SWEET_BERRIES).group("pies")
                .criterion(FabricRecipeProvider.hasItem(Items.SWEET_BERRIES),
                        FabricRecipeProvider.conditionsFromItem(Items.SWEET_BERRIES))
                .offerTo(exporter);

        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.pies.get(i + 1))
                    .input(Items.SUGAR).input(Items.EGG)
                    .input(MoreBerries.berries.get(i)).group("pies")
                    .criterion(FabricRecipeProvider.hasItem(MoreBerries.berries.get(i)),
                            FabricRecipeProvider
                                    .conditionsFromItem(MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }

        // Cakes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.cakes.get(i)).pattern("MBM")
                    .pattern("SES")
                    .pattern("WWW").input('M', Items.MILK_BUCKET).input('S', Items.SUGAR)
                    .input('W', Items.WHEAT)
                    .input('E', Items.EGG).input('B', MoreBerries.berries.get(i)).group("cakes")
                    .criterion(FabricRecipeProvider.hasItem(MoreBerries.berries.get(i)),
                            FabricRecipeProvider
                                    .conditionsFromItem(MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }

        // Dyes
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.RED_DYE)
                .input(Items.SWEET_BERRIES).group("dyes")
                .criterion(FabricRecipeProvider.hasItem(Items.SWEET_BERRIES),
                        FabricRecipeProvider.conditionsFromItem(Items.SWEET_BERRIES))
                .offerTo(exporter);

        List<Item> dyes = Arrays.asList(Items.BLUE_DYE, Items.YELLOW_DYE, Items.ORANGE_DYE, Items.PURPLE_DYE,
                Items.GREEN_DYE, Items.BLACK_DYE);
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, dyes.get(i))
                    .input(MoreBerries.berries.get(i)).group("dyes")
                    .criterion(FabricRecipeProvider.hasItem(MoreBerries.berries.get(i)),
                            FabricRecipeProvider
                                    .conditionsFromItem(MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }
    }
}
