package moreberries.mixin.item;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import moreberries.ItemExtension;
import net.minecraft.item.Item;

@Mixin(Item.class)
public class ItemMixin implements ItemExtension {
    @Mutable @Shadow @Final private Item recipeRemainder;

    @Override
    public void setRecipeRemainder(Item item) {
        recipeRemainder = item;
    }
}