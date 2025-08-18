package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public class MaterialStatsDataProvider extends AbstractMaterialStatsDataProvider {
    public MaterialStatsDataProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Material Stats";
    }

    @Override
    protected void addMaterialStats() {
        addMeleeHarvest();
        addRanged();
        addArmor();
        addMisc();
    }

    private void addMeleeHarvest() {
    }

    private void addRanged() {
    }

    private void addArmor() {
    }

    private void addMisc() {
        addMaterialStats(MaterialIds.iron,         DragonArmorMaterialStats.IRON);
        addMaterialStats(MaterialIds.copper,         DragonArmorMaterialStats.COPPER);
        addMaterialStats(MaterialIds.gold,         DragonArmorMaterialStats.GOLD);
    }
}
