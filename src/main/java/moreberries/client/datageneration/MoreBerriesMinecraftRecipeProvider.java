package moreberries.client.datageneration;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class MoreBerriesMinecraftRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesMinecraftRecipeProvider(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                // Override cake recipe
                ShapedRecipeJsonBuilder.create(Registries.ITEM, RecipeCategory.FOOD, Blocks.CAKE).pattern("MBM")
                        .pattern("SES")
                        .pattern("WWW").input('M', Items.MILK_BUCKET).input('S', Items.SUGAR).input('W', Items.WHEAT)
                        .input('E', Items.EGG).input('B', Items.SWEET_BERRIES)
                        .criterion(hasItem(Items.EGG),
                                this.conditionsFromItem(Items.EGG))
                        .group("cakes")
                        .offerTo(exporter);
            }
        };
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return Identifier.of(identifier.getPath());
    }

    @Override
    public String getName() {
        return "OverrideRecipes";
    }
}
