package moreberries.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class JuiceItem extends Item {

	public JuiceItem(Settings item$Settings) {
		super(item$Settings);
	}

	@Override
	public UseAction getUseAction(ItemStack itemStack) {
		return UseAction.DRINK;
	}

	@Override
	public ItemStack finishUsing(ItemStack itemStack, World world, LivingEntity livingEntity) {
		if (livingEntity instanceof PlayerEntity player) {
			player.getHungerManager().eat(itemStack.getComponents().get(DataComponentTypes.FOOD));
			player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));

			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, itemStack);
			}

			player.getInventory().offerOrDrop(new ItemStack(Items.GLASS_BOTTLE));
		}

		itemStack.decrement(1);
		return itemStack;
	}

}
