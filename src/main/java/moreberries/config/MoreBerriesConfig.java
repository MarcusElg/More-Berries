package moreberries.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = "moreberries")
public class MoreBerriesConfig implements ConfigData {

    public int blackBerrySpawnChance = 16;
    public int blueBerrySpawnChance = 16;
    public int greenBerrySpawnChance = 16;
    public int orangeBerrySpawnChance = 16;
    public int purpleBerrySpawnChance = 16;
    public int yellowBerrySpawnChance = 16;

    public String blackBerrySpawnBiomes = "minecraft:plains";
    public String blueBerrySpawnBiomes = "minecraft:forest,minecraft:wooded_hills";
    public String greenBerrySpawnBiomes = "minecraft:jungle,minecraft:jungle_hills,minecraft:jungle_edge,minecraft:modified_jungle,minecraft:modified_jungle_edge,minecraft:bamboo_jungle,minecraft:bamboo_jungle_hills";
    public String orangeBerrySpawnBiomes = "minecraft:badlands,minecraft:wooded_badlands_plateau,minecraft:badlands_plateau,minecraft:eroded_badlands,minecraft:modified_wooded_badlands_plateau,minecraft:modified_badlands_plateau";
    public String purpleBerrySpawnBiomes = "minecraft:swamp,minecraft:swamp_hills";
    public String yellowBerrySpawnBiomes = "minecraft:birch_forest,minecraft:birch_forest_hills,minecraft:tall_birch_forest,minecraft:tall_birch_hills";

    @Override
    public void validatePostLoad() throws ValidationException {
        blackBerrySpawnChance = Math.max(blackBerrySpawnChance, 1);
        blueBerrySpawnChance = Math.max(blueBerrySpawnChance, 1);
        greenBerrySpawnChance = Math.max(greenBerrySpawnChance, 1);
        orangeBerrySpawnChance = Math.max(orangeBerrySpawnChance, 1);
        purpleBerrySpawnChance = Math.max(purpleBerrySpawnChance, 1);
        yellowBerrySpawnChance = Math.max(yellowBerrySpawnChance, 1);
    }
}
