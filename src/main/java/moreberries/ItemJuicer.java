package moreberries;

import net.minecraft.item.Item;

public class ItemJuicer extends Item {

	public ItemJuicer(Item.Settings item$Settings_1) {
		super(item$Settings_1);
		((ItemExtension) this).setRecipeRemainder(this);
	}
}
