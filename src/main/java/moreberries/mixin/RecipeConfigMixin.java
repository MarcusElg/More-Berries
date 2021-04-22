package moreberries.mixin;

import com.google.gson.JsonElement;
import moreberries.MoreBerries;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeConfigMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        // Remove berry bushes recipes depending on config
        if (!MoreBerries.config.craftableBerryBushes) {
            Identifier id = new Identifier("moreberries", "black_berry_bush");
            map.remove(id, map.get(id));

            id = new Identifier("moreberries", "blue_berry_bush");
            map.remove(id, map.get(id));

            id = new Identifier("moreberries", "green_berry_bush");
            map.remove(id, map.get(id));

            id = new Identifier("moreberries", "orange_berry_bush");
            map.remove(id, map.get(id));

            id = new Identifier("moreberries", "purple_berry_bush");
            map.remove(id, map.get(id));

            id = new Identifier("moreberries", "yellow_berry_bush");
            map.remove(id, map.get(id));
        }
    }
}
