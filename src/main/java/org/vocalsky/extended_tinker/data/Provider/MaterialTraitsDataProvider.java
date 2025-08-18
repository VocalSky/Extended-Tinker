package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import static slimeknights.tconstruct.library.materials.MaterialRegistry.ARMOR;

public class MaterialTraitsDataProvider extends AbstractMaterialTraitDataProvider {
    public MaterialTraitsDataProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addTraits(MaterialIds.gold, DragonArmorMaterialStats.GOLD.getId(), TinkerModifiers.golden.getId(), ModifierIds.magicProtection);
    }

    @Override
    public String getName() {
        return "Extended Tinker Material Traits";
    }
}
