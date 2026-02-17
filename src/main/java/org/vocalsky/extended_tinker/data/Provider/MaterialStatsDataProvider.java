package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
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
        addArmorShieldStats(IafCore.Materials.copper, DragonArmorMaterialStats.builder().durabilityFactor(13).armor(1, 1, 1, 1), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.iron, DragonArmorMaterialStats.builder().durabilityFactor(15).armor(2, 2, 2, 2), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.gold, DragonArmorMaterialStats.builder().durabilityFactor( 15).armor(3, 3, 3, 3), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.silver, DragonArmorMaterialStats.builder().durabilityFactor(11).armor(3, 3, 3, 3).toughness(1.5f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.diamond, DragonArmorMaterialStats.builder().durabilityFactor(18).armor(5, 5, 5, 5).knockbackResistance(0.05f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.fire, DragonArmorMaterialStats.builder().durabilityFactor(35).armor(10, 10, 10, 10).toughness(3).knockbackResistance(0.2f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.ice, DragonArmorMaterialStats.builder().durabilityFactor(35).armor(10, 10, 10, 10).toughness(3).knockbackResistance(0.2f), StatlessMaterialStats.MAILLE);
        addArmorShieldStats(IafCore.Materials.lightning, DragonArmorMaterialStats.builder().durabilityFactor(35).armor(10, 10, 10, 10).toughness(3).knockbackResistance(0.2f), StatlessMaterialStats.MAILLE);
    }

    private void addMisc() {
    }
}
