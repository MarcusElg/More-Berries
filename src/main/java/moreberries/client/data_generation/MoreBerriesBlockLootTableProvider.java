package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import moreberries.block.CandleBerryCakeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class MoreBerriesBlockLootTableProvider extends FabricBlockLootSubProvider {

    HolderLookup.Provider lookup;

    protected MoreBerriesBlockLootTableProvider(FabricPackOutput dataOutput,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture);
        this.lookup = registriesFuture.resultNow();
    }

    @Override
    public void generate() {
        Reference<Enchantment> fortuneEnchantement = lookup.lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FORTUNE);

        // Bushes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            // Breaking bush
            add(MoreBerries.bushes.get(i),
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(MoreBerries.berries.get(i)))
                            .when(LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(MoreBerries.bushes.get(i))
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(BerryBushBlock.AGE,
                                                    3)))
                            .apply(SetItemCountFunction.setCount(
                                    UniformGenerator.between(2, 3)))
                            .apply(ApplyBonusCount
                                    .addUniformBonusCount(fortuneEnchantement)))
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(
                                            MoreBerries.berries.get(i)))
                                    .when(LootItemBlockStatePropertyCondition
                                            .hasBlockStateProperties(MoreBerries.bushes
                                                    .get(i))
                                            .setProperties(
                                                    StatePropertiesPredicate.Builder
                                                            .properties()
                                                            .hasProperty(BerryBushBlock.AGE,
                                                                    2)))
                                    .apply(SetItemCountFunction.setCount(
                                            UniformGenerator
                                                    .between(1, 2)))
                                    .apply(ApplyBonusCount.addUniformBonusCount(
                                            fortuneEnchantement)))
                            .withPool(LootPool.lootPool().add(LootItem
                                    .lootTableItem(MoreBerries.bushes.get(i))
                                    .when(
                                            MatchTool
                                                    .toolMatches(ItemPredicate.Builder
                                                            .item()
                                                            .of(BuiltInRegistries.ITEM,
                                                                    Items.SHEARS)))))
                            .apply(ApplyExplosionDecay.explosionDecay()));
        }

        // Cakes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            add(MoreBerries.cakes.get(i), LootTable.lootTable());
        }

        // Berry cakes
        for (CandleBerryCakeBlock cake : MoreBerries.candleCakes) {
            add(cake, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(cake.candle))));
        }
    }

}
