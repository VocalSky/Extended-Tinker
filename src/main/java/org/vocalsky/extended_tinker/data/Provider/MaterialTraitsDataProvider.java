package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
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
        addTraits(IafMaterials.iron, IafMaterials.dragon_armor, TinkerModifiers.magnetic.getId(), ModifierIds.projectileProtection);
        addTraits(IafMaterials.copper, IafMaterials.dragon_armor, TinkerModifiers.dwarven.getId(), ModifierIds.depthProtection);
        addTraits(IafMaterials.gold, IafMaterials.dragon_armor, TinkerModifiers.golden.getId(), ModifierIds.magicProtection);
        addTraits(IafMaterials.silver, IafMaterials.dragon_armor, TinkerModifiers.frostshield.getId(), ModifierIds.consecrated);
        addTraits(IafMaterials.diamond, IafMaterials.dragon_armor, ModifierIds.diamond, ModifierIds.boundless);
        addTraits(IafMaterials.fire, IafMaterials.dragon_armor, ModifierIds.dragonborn, ModifierIds.fireProtection);
        addTraits(IafMaterials.ice, IafMaterials.dragon_armor, ModifierIds.dragonborn, ModifierIds.magicProtection);
        addTraits(IafMaterials.lightning, IafMaterials.dragon_armor, ModifierIds.dragonborn, ModifierIds.blastProtection);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Material Traits";
    }
}
