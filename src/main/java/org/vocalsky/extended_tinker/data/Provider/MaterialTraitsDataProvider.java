package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonPlatingArmorMaterialStats;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public class MaterialTraitsDataProvider extends AbstractMaterialTraitDataProvider {
    public MaterialTraitsDataProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addTraits(MaterialIds.gold, DragonArmorMaterialStats.GOLD.getId(), TinkerModifiers.golden.getId(), ModifierIds.magicProtection);
        addTraits(IafMaterials.silver, DragonArmorMaterialStats.SILVER.getId(), ModifierIds.consecrated);
        addTraits(IafMaterials.diamond, DragonArmorMaterialStats.DIAMOND.getId(), ModifierIds.diamond);
        addTraits(IafMaterials.fire, DragonArmorMaterialStats.FIRE.getId(), ModifierIds.dragonborn, ModifierIds.fireProtection);
        addTraits(IafMaterials.ice, DragonArmorMaterialStats.ICE.getId(), ModifierIds.dragonborn, ModifierIds.magicProtection);
        addTraits(IafMaterials.lightning, DragonArmorMaterialStats.LIGHTNING.getId(), ModifierIds.dragonborn, ModifierIds.blastProtection);
    }

    @Override
    public String getName() {
        return "Extended Tinker Material Traits";
    }
}
