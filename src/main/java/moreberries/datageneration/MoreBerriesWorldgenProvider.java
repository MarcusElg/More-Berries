package moreberries.datageneration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import moreberries.MoreBerries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.Block;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class MoreBerriesWorldgenProvider extends FabricDynamicRegistryProvider {
        public MoreBerriesWorldgenProvider(FabricDataOutput output,
                        CompletableFuture<WrapperLookup> registriesFuture) {
                super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
                addBerryBushGeneration(entries, MoreBerries.config.blackBerrySpawnBiomes, MoreBerries.blackBerryBush,
                                "black_berry");
                addBerryBushGeneration(entries, MoreBerries.config.greenBerrySpawnBiomes, MoreBerries.greenBerryBush,
                                "green_berry");
                addBerryBushGeneration(entries, MoreBerries.config.blueBerrySpawnBiomes, MoreBerries.blueBerryBush,
                                "blue_berry");
                addBerryBushGeneration(entries, MoreBerries.config.orangeBerrySpawnBiomes, MoreBerries.orangeBerryBush,
                                "orange_berry");
                addBerryBushGeneration(entries, MoreBerries.config.purpleBerrySpawnBiomes, MoreBerries.purpleBerryBush,
                                "purple_berry");
                addBerryBushGeneration(entries, MoreBerries.config.yellowBerrySpawnBiomes, MoreBerries.yellowBerryBush,
                                "yellow_berry");
        }

        private void addBerryBushGeneration(Entries entries, String spawnBiomes, Block bushBlock,
                        String name) {
                // Configured feature
                BlockStateProvider berryBushProvider = BlockStateProvider
                                .of(bushBlock.getDefaultState().with(SweetBerryBushBlock.AGE, 3));
                List<Block> berryBushes = List.of(MoreBerries.blackBerryBush,
                                MoreBerries.blueBerryBush, MoreBerries.orangeBerryBush,
                                MoreBerries.greenBerryBush,
                                MoreBerries.purpleBerryBush,
                                MoreBerries.yellowBerryBush);
                BlockPredicate placementBlockPredicate = BlockPredicate.allOf(List.of(BlockPredicate.IS_AIR,
                                BlockPredicate.wouldSurvive(bushBlock.getDefaultState(), BlockPos.ORIGIN),
                                BlockPredicate.not(BlockPredicate.matchingBlocks(new Vec3i(0, -1, 0), berryBushes))));
                RandomPatchFeatureConfig randomPatchConfig = new RandomPatchFeatureConfig(32, 2, 3, PlacedFeatures
                                .createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(berryBushProvider),
                                                BlockFilterPlacementModifier.of(placementBlockPredicate)));
                ConfiguredFeature<RandomPatchFeatureConfig, ?> configuredFeature = new ConfiguredFeature<>(
                                Feature.RANDOM_PATCH,
                                randomPatchConfig);
                RegistryEntry<ConfiguredFeature<?, ?>> featureReference = entries.add(RegistryKey.of(
                                RegistryKeys.CONFIGURED_FEATURE,
                                new Identifier(MoreBerries.MOD_ID, String.format("%s_generation", name))),
                                configuredFeature);

                // Placed feature
                PlacedFeature placedFeatureEntry = new PlacedFeature(featureReference,
                                List.of(RarityFilterPlacementModifier.of(16),
                                                SquarePlacementModifier.of(),
                                                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                                                BiomePlacementModifier.of()));
                entries.add(RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                                new Identifier(MoreBerries.MOD_ID, String.format("%s_generation", name))),
                                placedFeatureEntry);
        }

        @Override
        public String getName() {
                return "More Berries World Generation";
        }
}
