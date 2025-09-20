package moreberries.client.data_generation;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MoreBerriesDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        // assets
        pack.addProvider(MoreBerriesModelProvider::new);

        // data
        pack.addProvider(MoreBerriesItemTagProvider::new);
        pack.addProvider(MoreBerriesBlockTagProvider::new);
        pack.addProvider(MoreBerriesMinecraftRecipeProvider::new);
        pack.addProvider(MoreBerriesRecipeProvider::new);
        pack.addProvider(MoreBerriesBlockLootTableProvider::new);
        pack.addProvider(MoreBerriesBlockInteractLootTableProvider::new);
    }
}
