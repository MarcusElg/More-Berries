package moreberries.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class JuicerItem extends Item {

	public JuicerItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack getRecipeRemainder(ItemStack stack) {
		return new ItemStack(stack.getItem());
	}
}
