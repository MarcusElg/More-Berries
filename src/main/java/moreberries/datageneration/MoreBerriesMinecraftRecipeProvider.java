package moreberries.datageneration;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class MoreBerriesMinecraftRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesMinecraftRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // Override cake recipe
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, Blocks.CAKE).pattern("MBM").pattern("SES")
                .pattern("WWW").input('M', Items.MILK_BUCKET).input('S', Items.SUGAR).input('W', Items.WHEAT)
                .input('E', Items.EGG).input('B', Items.SWEET_BERRIES)
                .criterion(FabricRecipeProvider.hasItem(Items.EGG),
                        FabricRecipeProvider.conditionsFromItem(Items.EGG))
                .group("cakes")
                .offerTo(exporter);
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return new Identifier("minecraft", identifier.getPath());
    }

    @Override
    public String getName() {
        return "OverrideRecipes";
    }
}
