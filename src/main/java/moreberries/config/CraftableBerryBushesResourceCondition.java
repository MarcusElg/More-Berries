package moreberries.config;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import moreberries.MoreBerries;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.registry.RegistryOps.RegistryInfoGetter;

public record CraftableBerryBushesResourceCondition() implements ResourceCondition {

    public static final MapCodec<CraftableBerryBushesResourceCondition> CODEC = MapCodec
            .unit(CraftableBerryBushesResourceCondition::new);

    @Override
    public ResourceConditionType<?> getType() {
        return MoreBerries.CRAFTABLE_BERRIES_RESOURCE_CONDITION;
    }

    @Override
    public boolean test(@Nullable RegistryInfoGetter registryInfo) {
        return MoreBerries.config.craftableBerryBushes;
    }

}
