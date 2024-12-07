package moreberries.client.datageneration;

import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import moreberries.block.CandleBerryCakeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry.Reference;

public class MoreBerriesLootTableProvider extends FabricBlockLootTableProvider {

    RegistryWrapper.WrapperLookup lookup;

    protected MoreBerriesLootTableProvider(FabricDataOutput dataOutput,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
        this.lookup = registriesFuture.resultNow();
    }

    @Override
    public void generate() {
        Reference<Enchantment> fortuneEnchantement = lookup.getOrThrow(RegistryKeys.ENCHANTMENT)
                .getOrThrow(Enchantments.FORTUNE);

        // Bushes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            addDrop(MoreBerries.bushes.get(i),
                    LootTable.builder().pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(MoreBerries.berries.get(i)))
                            .conditionally(BlockStatePropertyLootCondition
                                    .builder(MoreBerries.bushes.get(i))
                                    .properties(StatePredicate.Builder.create()
                                            .exactMatch(BerryBushBlock.AGE,
                                                    3)))
                            .apply(SetCountLootFunction.builder(
                                    UniformLootNumberProvider.create(2, 3)))
                            .apply(ApplyBonusLootFunction
                                    .uniformBonusCount(fortuneEnchantement)))
                            .pool(LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1))
                                    .with(ItemEntry.builder(
                                            MoreBerries.berries.get(i)))
                                    .conditionally(BlockStatePropertyLootCondition
                                            .builder(MoreBerries.bushes
                                                    .get(i))
                                            .properties(
                                                    StatePredicate.Builder
                                                            .create()
                                                            .exactMatch(BerryBushBlock.AGE,
                                                                    2)))
                                    .apply(SetCountLootFunction.builder(
                                            UniformLootNumberProvider
                                                    .create(1, 2)))
                                    .apply(ApplyBonusLootFunction.uniformBonusCount(
                                            fortuneEnchantement)))
                            .pool(LootPool.builder().with(ItemEntry
                                    .builder(MoreBerries.bushes.get(i))
                                    .conditionally(
                                            MatchToolLootCondition
                                                    .builder(ItemPredicate.Builder
                                                            .create()
                                                            .items(Registries.ITEM,
                                                                    Items.SHEARS)))))
                            .apply(ExplosionDecayLootFunction.builder()));
        }

        // Cakes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            addDrop(MoreBerries.cakes.get(i), LootTable.builder());
        }

        // Berry cakes
        for (CandleBerryCakeBlock cake : MoreBerries.candleCakes) {
            addDrop(cake, LootTable.builder()
                    .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(cake.candle))));
        }
    }

}
