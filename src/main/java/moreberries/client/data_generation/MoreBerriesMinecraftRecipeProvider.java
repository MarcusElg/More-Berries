package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

public class MoreBerriesMinecraftRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesMinecraftRecipeProvider(FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(Provider registries, BootstrapContext<Recipe<?>> recipes,
            BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
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
