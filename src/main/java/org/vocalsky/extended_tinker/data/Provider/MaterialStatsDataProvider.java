package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

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
        addArmorShieldStats(IafMaterials.copper, DragonArmorMaterialStats.builder().durabilityFactor(13).armor(1, 1, 1, 1), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.iron, DragonArmorMaterialStats.builder().durabilityFactor(15).armor(2, 2, 2, 2), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.gold, DragonArmorMaterialStats.builder().durabilityFactor( 15).armor(3, 3, 3, 3), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.silver, DragonArmorMaterialStats.builder().durabilityFactor(11).armor(3, 3, 3, 3).toughness(1.5f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.diamond, DragonArmorMaterialStats.builder().durabilityFactor(18).armor(5, 5, 5, 5).knockbackResistance(0.05f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.fire, DragonArmorMaterialStats.builder().durabilityFactor(35).armor(10, 10, 10, 10).toughness(3).knockbackResistance(0.2f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.ice, DragonArmorMaterialStats.builder().durabilityFactor(35).armor(10, 10, 10, 10).toughness(3).knockbackResistance(0.2f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafMaterials.lightning, DragonArmorMaterialStats.builder().durabilityFactor(35).armor(10, 10, 10, 10).toughness(3).knockbackResistance(0.2f), StatlessMaterialStats.MAILLE);
    }

    private void addMisc() {
    }
}
