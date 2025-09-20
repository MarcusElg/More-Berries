package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class MoreBerriesBlockInteractLootTableProvider extends SimpleFabricLootTableProvider {

    public MoreBerriesBlockInteractLootTableProvider(FabricDataOutput output,
            CompletableFuture<WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.BLOCK_INTERACT);
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, Builder> biConsumer) {
        // Bushes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            // Harvesting bush
            biConsumer.accept(RegistryKey.of(RegistryKeys.LOOT_TABLE,
                    MoreBerries.getId("harvest/"
                            + MoreBerries.bushes.get(i).getTranslationKey().replace("block." + MoreBerries.MOD_ID + ".",
                                    ""))),
                    LootTable.builder().pool(LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(MoreBerries.berries.get(i))
                                    .conditionally(BlockStatePropertyLootCondition
                                            .builder(MoreBerries.bushes.get(i))
                                            .properties(StatePredicate.Builder.create()
                                                    .exactMatch(BerryBushBlock.AGE,
                                                            3)))
                                    .apply(SetCountLootFunction.builder(
                                            ConstantLootNumberProvider.create(1)))))
                            .pool(LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1))
                                    .with(ItemEntry.builder(
                                            MoreBerries.berries.get(i)).apply(
                                                    SetCountLootFunction.builder(
                                                            UniformLootNumberProvider
                                                                    .create(1, 2))))));
        }
    }

}
