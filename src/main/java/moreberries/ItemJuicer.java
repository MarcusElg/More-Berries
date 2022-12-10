package moreberries;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemJuicer extends Item {

	public ItemJuicer(Item.Settings settings) {
		super(settings);
		// ((ItemExtension) this).setRecipeRemainder(this);
	}

	public ItemStack getRecipeRemainder(ItemStack stack) {
		return stack;
	}
}
