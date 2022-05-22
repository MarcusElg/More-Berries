package moreberries.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "moreberries")
public class MoreBerriesConfig implements ConfigData {

    public int blackBerrySpawnChance = 16;
    public int blueBerrySpawnChance = 16;
    public int greenBerrySpawnChance = 16;
    public int orangeBerrySpawnChance = 16;
    public int purpleBerrySpawnChance = 16;
    public int yellowBerrySpawnChance = 16;

    public String blackBerrySpawnBiomes = "minecraft:plains";
    public String blueBerrySpawnBiomes = "minecraft:forest,minecraft:flower_forest,minecraft:dark_forest,minecraft:grove";
    public String greenBerrySpawnBiomes = "#minecraft:is_jungle";
    public String orangeBerrySpawnBiomes = "#minecraft:is_badlands,minecraft:savanna,minecraft:savanna_plateau,minecraft:windswept_savanna";
    public String purpleBerrySpawnBiomes = "minecraft:swamp,minecraft:mangrove_swamp";
    public String yellowBerrySpawnBiomes = "minecraft:birch_forest, minecraft:old_growth_birch_forest";

    public boolean craftableBerryBushes = false;
    public boolean replaceSweetBerryBushModel = true;

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
