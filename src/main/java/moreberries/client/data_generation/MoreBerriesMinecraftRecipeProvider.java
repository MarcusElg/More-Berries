package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class MoreBerriesMinecraftRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesMinecraftRecipeProvider(FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
        return new RecipeProvider(registries, exporter) {
            @Override
            public void buildRecipes() {
                // Override cake recipe
                ShapedRecipeBuilder.shaped(BuiltInRegistries.ITEM, RecipeCategory.FOOD, Blocks.CAKE).pattern("MBM")
                        .pattern("SES")
                        .pattern("WWW").define('M', Items.MILK_BUCKET).define('S', Items.SUGAR).define('W', Items.WHEAT)
                        .define('E', Items.EGG).define('B', Items.SWEET_BERRIES)
                        .unlockedBy(getHasName(Items.EGG),
                                this.has(Items.EGG))
                        .group("cakes")
                        .save(output);
            }
        };
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return Identifier.parse(identifier.getPath());
    }

    @Override
    public String getName() {
        return "OverrideRecipes";
    }
}
