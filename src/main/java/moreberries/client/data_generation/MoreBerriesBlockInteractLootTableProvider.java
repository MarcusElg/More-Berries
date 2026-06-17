package moreberries.client.data_generation;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import moreberries.MoreBerries;
import moreberries.block.BerryBushBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class MoreBerriesBlockInteractLootTableProvider extends SimpleFabricLootTableSubProvider {

    public MoreBerriesBlockInteractLootTableProvider(FabricPackOutput output,
            CompletableFuture<Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.BLOCK_INTERACT);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, Builder> biConsumer) {
        // Bushes
        for (int i = 0; i < MoreBerries.berries.size(); i++) {
            // Harvesting bush
            biConsumer.accept(ResourceKey.create(Registries.LOOT_TABLE,
                    MoreBerries.getId("harvest/"
                            + MoreBerries.bushes.get(i).getDescriptionId().replace("block." + MoreBerries.MOD_ID + ".",
                                    ""))),
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(MoreBerries.berries.get(i))
                                    .when(LootItemBlockStatePropertyCondition
                                            .hasBlockStateProperties(MoreBerries.bushes.get(i))
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(BerryBushBlock.AGE,
                                                            3)))
                                    .apply(SetItemCountFunction.setCount(
                                            ConstantValue.exactly(1)))))
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(
                                            MoreBerries.berries.get(i)).apply(
                                                    SetItemCountFunction.setCount(
                                                            UniformGenerator
                                                                    .between(1, 2))))));
        }
    }

}
