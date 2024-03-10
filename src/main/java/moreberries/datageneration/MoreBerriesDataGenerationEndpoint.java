package moreberries.datageneration;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MoreBerriesDataGenerationEndpoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(MoreBerriesWorldgenProvider::new);
        pack.addProvider(MoreBerriesItemTagProvider::new);
        pack.addProvider(MoreBerriesBlockTagProvider::new);
        pack.addProvider(MoreBerriesMinecraftRecipeProvider::new);
        pack.addProvider(MoreBerriesRecipeProvider::new);
        pack.addProvider(MoreBerriesAdvancementProvider::new);
        pack.addProvider(MoreBerriesLootTableProvider::new);
    }
}
