package moreberries.datageneration;

import java.util.Arrays;
import java.util.List;

import moreberries.MoreBerries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class MoreBerriesRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // Juicer
        withItemCriterions(
                ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, MoreBerries.juicer).pattern("#").pattern("_")
                        .input('#', Blocks.STONE_BUTTON).input('_', Blocks.STONE_SLAB),
                Arrays.asList(Items.STONE_BUTTON, Items.STONE_SLAB))
                .offerTo(exporter);

        // Juices
        withItemCriterions(
                ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.juices.get(0))
                        .input(MoreBerries.juicer)
                        .input(Items.GLASS_BOTTLE).input(Items.SWEET_BERRIES).group("juices"),
                Arrays.asList(MoreBerries.juicer, Items.GLASS_BOTTLE, Items.SWEET_BERRIES))
                .offerTo(exporter);

        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            withItemCriterions(
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.juices.get(i + 1))
                            .input(MoreBerries.juicer)
                            .input(Items.GLASS_BOTTLE).input(MoreBerries.berries.get(i)).group("juices"),
                    Arrays.asList(MoreBerries.juicer, Items.GLASS_BOTTLE, MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }

        // Pies
        withItemCriterions(
                ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.pies.get(0))
                        .input(Items.SUGAR).input(Items.EGG)
                        .input(Items.SWEET_BERRIES).group("pies"),
                Arrays.asList(Items.SUGAR, Items.EGG, Items.SWEET_BERRIES))
                .offerTo(exporter);

        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            withItemCriterions(
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.pies.get(i + 1))
                            .input(Items.SUGAR).input(Items.EGG)
                            .input(MoreBerries.berries.get(i)).group("pies"),
                    Arrays.asList(Items.SUGAR, Items.EGG, MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }

        // Cakes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            withItemCriterions(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, MoreBerries.cakes.get(i)).pattern("MBM")
                            .pattern("SES")
                            .pattern("WWW").input('M', Items.MILK_BUCKET).input('S', Items.SUGAR)
                            .input('W', Items.WHEAT)
                            .input('E', Items.EGG).input('B', MoreBerries.berries.get(i)).group("cakes"),
                    Arrays.asList(Items.MILK_BUCKET, Items.SUGAR, Items.WHEAT, Items.EGG, MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }

        // Dyes
        withItemCriterions(
                ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.RED_DYE)
                        .input(Items.SWEET_BERRIES).group("dyes"),
                Arrays.asList(Items.SWEET_BERRIES))
                .offerTo(exporter);

        List<Item> dyes = Arrays.asList(Items.BLUE_DYE, Items.YELLOW_DYE, Items.ORANGE_DYE, Items.PURPLE_DYE,
                Items.GREEN_DYE, Items.BLACK_DYE);
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            withItemCriterions(
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, dyes.get(i))
                            .input(MoreBerries.berries.get(i)).group("dyes"),
                    Arrays.asList(MoreBerries.berries.get(i)))
                    .offerTo(exporter);
        }
    }

    private CraftingRecipeJsonBuilder withItemCriterions(CraftingRecipeJsonBuilder builder,
            List<ItemConvertible> items) {
        for (ItemConvertible item : items) {
            builder = builder.criterion(FabricRecipeProvider.hasItem(item),
                    FabricRecipeProvider.conditionsFromItem(item));
        }
        return builder;
    }
}
