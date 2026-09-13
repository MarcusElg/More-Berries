package moreberries.client.data_generation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.config.CraftableBerryBushesResourceCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

public class MoreBerriesRecipeProvider extends FabricRecipeProvider {

    public MoreBerriesRecipeProvider(FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(Provider registries, BootstrapContext<Recipe<?>> recipes,
            BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                // Juicer
                ShapedRecipeBuilder
                        .shaped(BuiltInRegistries.ITEM, RecipeCategory.TOOLS, MoreBerries.juicer)
                        .pattern("#")
                        .pattern("_")
                        .define('#', Blocks.STONE_BUTTON).define('_', Blocks.STONE_SLAB)
                        .unlockedBy(RecipeProvider.getHasName(Blocks.STONE_SLAB),
                                this
                                        .has(Blocks.STONE_SLAB))
                        .save(output);

                // Juices
                ShapelessRecipeBuilder
                        .shapeless(BuiltInRegistries.ITEM, RecipeCategory.FOOD, MoreBerries.juices.get(0))
                        .requires(MoreBerries.juicer)
                        .requires(Items.GLASS_BOTTLE).requires(Items.SWEET_BERRIES).group("juices")
                        .unlockedBy(RecipeProvider.getHasName(Items.SWEET_BERRIES),
                                this.has(
                                        Items.SWEET_BERRIES))
                        .save(output);

                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapelessRecipeBuilder
                            .shapeless(BuiltInRegistries.ITEM, RecipeCategory.FOOD,
                                    MoreBerries.juices.get(i + 1))
                            .requires(MoreBerries.juicer)
                            .requires(Items.GLASS_BOTTLE).requires(MoreBerries.berries.get(i))
                            .group("juices")
                            .unlockedBy(RecipeProvider
                                    .getHasName(MoreBerries.berries.get(i)),
                                    this
                                            .has(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .save(output);
                }

                // Pies
                ShapelessRecipeBuilder
                        .shapeless(BuiltInRegistries.ITEM, RecipeCategory.FOOD, MoreBerries.pies.get(0))
                        .requires(Items.SUGAR).requires(Items.EGG)
                        .requires(Items.SWEET_BERRIES).group("pies")
                        .unlockedBy(RecipeProvider.getHasName(Items.SWEET_BERRIES),
                                this.has(
                                        Items.SWEET_BERRIES))
                        .save(output);

                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapelessRecipeBuilder
                            .shapeless(BuiltInRegistries.ITEM, RecipeCategory.FOOD,
                                    MoreBerries.pies.get(i + 1))
                            .requires(Items.SUGAR).requires(Items.EGG)
                            .requires(MoreBerries.berries.get(i)).group("pies")
                            .unlockedBy(RecipeProvider
                                    .getHasName(MoreBerries.berries.get(i)),
                                    this
                                            .has(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .save(output);
                }

                // Cakes
                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapedRecipeBuilder
                            .shaped(BuiltInRegistries.ITEM, RecipeCategory.FOOD,
                                    MoreBerries.cakes.get(i))
                            .pattern("MBM")
                            .pattern("SES")
                            .pattern("WWW").define('M', Items.MILK_BUCKET)
                            .define('S', Items.SUGAR)
                            .define('W', Items.WHEAT)
                            .define('E', Items.EGG).define('B', MoreBerries.berries.get(i))
                            .group("cakes")
                            .unlockedBy(RecipeProvider
                                    .getHasName(MoreBerries.berries.get(i)),
                                    this
                                            .has(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .save(output);
                }

                // Dyes
                ShapelessRecipeBuilder.shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, Items.DYE.red())
                        .requires(Items.SWEET_BERRIES).group("dyes")
                        .unlockedBy(RecipeProvider.getHasName(Items.SWEET_BERRIES),
                                this.has(
                                        Items.SWEET_BERRIES))
                        .save(output);

                List<Item> dyes = Arrays.asList(Items.DYE.blue(), Items.DYE.yellow(), Items.DYE.orange(),
                        Items.DYE.purple(),
                        Items.DYE.green(), Items.DYE.black());
                for (int i = 0; i < MoreBerries.berries.size(); i++) {
                    ShapelessRecipeBuilder
                            .shapeless(BuiltInRegistries.ITEM, RecipeCategory.MISC, dyes.get(i))
                            .requires(MoreBerries.berries.get(i)).group("dyes")
                            .unlockedBy(RecipeProvider
                                    .getHasName(MoreBerries.berries.get(i)),
                                    this
                                            .has(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .save(output);
                }

                // Optional berry bush recipes
                for (int i = 0; i < MoreBerries.bushes.size(); i++) {
                    ShapelessRecipeBuilder
                            .shapeless(BuiltInRegistries.ITEM, RecipeCategory.FOOD, MoreBerries.bushes.get(i))
                            .requires(MoreBerries.berries.get(i)).requires(Blocks.OAK_LEAVES).group("berry_bushes")
                            .unlockedBy(RecipeProvider
                                    .getHasName(MoreBerries.berries.get(i)),
                                    this
                                            .has(
                                                    MoreBerries.berries
                                                            .get(i)))
                            .save(withConditions(output, new CraftableBerryBushesResourceCondition()));
                }
            }
        };
    }

    @Override
    public String getName() {
        return MoreBerries.MOD_ID;
    }
}
