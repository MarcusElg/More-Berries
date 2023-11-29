package moreberries.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemJuicer extends Item {

	public ItemJuicer(Item.Settings settings) {
		super(settings);
	}

	public ItemStack getRecipeRemainder(ItemStack stack) {
		return new ItemStack(stack.getItem());
	}
}
