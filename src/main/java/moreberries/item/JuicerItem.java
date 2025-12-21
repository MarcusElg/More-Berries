package moreberries.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class JuicerItem extends Item {

	public JuicerItem(Item.Properties settings) {
		super(settings);
	}

	@Override
	public ItemStack getRecipeRemainder(ItemStack stack) {
		return new ItemStack(stack.getItem());
	}
}
