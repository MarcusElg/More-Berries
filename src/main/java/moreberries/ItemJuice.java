package moreberries;

import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ItemJuice extends Item {

	public ItemJuice(Settings item$Settings_1) {
		super(item$Settings_1);
	}
	
	public UseAction getUseAction(ItemStack itemStack_1) {
        return UseAction.DRINK;
    }

	public ItemStack finishUsing(ItemStack itemStack_1, World world_1, LivingEntity livingEntity_1) {
		if (livingEntity_1 instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) livingEntity_1;
			player.getHungerManager().eat(itemStack_1.getItem(), itemStack_1);
			player.incrementStat(Stats.USED.getOrCreateStat(itemStack_1.getItem()));
			
			if (player instanceof ServerPlayerEntity) {
				Criterions.CONSUME_ITEM.handle((ServerPlayerEntity) player, itemStack_1);
			}
			
			player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE));
		}
		
		itemStack_1.decrement(1);
		return itemStack_1;
	}

}
