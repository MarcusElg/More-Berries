package moreberries.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import moreberries.MoreBerries;

@Config(name = MoreBerries.MOD_ID)
public class MoreBerriesConfig implements ConfigData {
    public String blackBerrySpawnBiomes = "minecraft:plains";
    public String blueBerrySpawnBiomes = "minecraft:forest,minecraft:flower_forest,minecraft:dark_forest,minecraft:grove";
    public String greenBerrySpawnBiomes = "#minecraft:is_jungle";
    public String orangeBerrySpawnBiomes = "#minecraft:is_badlands,minecraft:savanna,minecraft:savanna_plateau,minecraft:windswept_savanna";
    public String purpleBerrySpawnBiomes = "minecraft:swamp,minecraft:mangrove_swamp";
    public String yellowBerrySpawnBiomes = "minecraft:birch_forest, minecraft:old_growth_birch_forest";

    public boolean craftableBerryBushes = false;
    public boolean replaceSweetBerryBushModel = true;
}
