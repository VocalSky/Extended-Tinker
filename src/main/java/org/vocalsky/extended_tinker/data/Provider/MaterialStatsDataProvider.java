package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonPlatingArmorMaterialStats;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
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
        addMaterialStats(MaterialIds.iron, DragonArmorMaterialStats.IRON);
        addMaterialStats(MaterialIds.copper, DragonArmorMaterialStats.COPPER);
        addMaterialStats(MaterialIds.gold, DragonArmorMaterialStats.GOLD);
        addMaterialStats(IafMaterials.silver, DragonArmorMaterialStats.SILVER);
        addMaterialStats(IafMaterials.diamond, DragonArmorMaterialStats.DIAMOND);
        addMaterialStats(IafMaterials.fire, DragonArmorMaterialStats.FIRE);
        addMaterialStats(IafMaterials.ice, DragonArmorMaterialStats.ICE);
        addMaterialStats(IafMaterials.lightning, DragonArmorMaterialStats.LIGHTNING);

//        addArmorShieldStats(MaterialIds.iron, DragonPlatingArmorMaterialStats.builder().durabilityFactor(15.0F).armor(2.0F, 4.0F, 5.0F, 2.0F), StatlessMaterialStats.MAILLE);
//        addArmorShieldStats(MaterialIds.copper, DragonPlatingArmorMaterialStats.builder().durabilityFactor(13.0F).armor(1.0F, 2.0F, 3.0F, 1.0F), StatlessMaterialStats.MAILLE);;
//        addArmorShieldStats(MaterialIds.gold, DragonPlatingArmorMaterialStats.builder().durabilityFactor(7.0F).armor(1.0F, 3.0F, 4.0F, 1.0F), StatlessMaterialStats.MAILLE);
    }

    private void addMisc() {
    }
}
