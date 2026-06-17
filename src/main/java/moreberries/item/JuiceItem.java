package moreberries.item;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class JuiceItem extends Item {

    public JuiceItem(Properties item$Settings) {
        super(item$Settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            player.getFoodData().eat(itemStack.getComponents().get(DataComponents.FOOD));
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

            if (player instanceof ServerPlayer serverPlayerEntity) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity,
                        itemStack);
            }

            player.getInventory().placeItemBackInInventory(new ItemStack(Items.GLASS_BOTTLE));
        }

        itemStack.shrink(1);
        return itemStack;
    }

}
