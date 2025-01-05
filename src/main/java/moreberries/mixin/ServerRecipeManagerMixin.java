package moreberries.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import moreberries.MoreBerries;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

@Mixin(ServerRecipeManager.class)
public class ServerRecipeManagerMixin {
    @Inject(method = "collectServerRecipes", at = @At("RETURN"), cancellable = true)
    private static void collectServerRecipes(
            Iterable<ServerRecipeManager.ServerRecipe> recipes,
            FeatureSet featureSet,
            CallbackInfoReturnable<List<ServerRecipeManager.ServerRecipe>> callbackReturnable) {
        List<ServerRecipeManager.ServerRecipe> original = callbackReturnable.getReturnValue();

        // Remove enchanting table recipe
        if (!MoreBerries.config.craftableBerryBushes) {
            original.removeIf(
                    recipe -> {
                        Identifier id = recipe.comp_3250().id().getValue();
                        return id.equals(MoreBerries.getId("black_berry_bush"))
                                || id.equals(MoreBerries.getId("blue_berry_bush"))
                                || id.equals(MoreBerries.getId("green_berry_bush"))
                                || id.equals(MoreBerries.getId("orange_berry_bush"))
                                || id.equals(MoreBerries.getId("purple_berry_bush"))
                                || id.equals(MoreBerries.getId("yellow_berry_bush"));
                    });
        }
        callbackReturnable.setReturnValue(original);
    }
}
