package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class MaterialTraitsDataProvider extends AbstractMaterialTraitDataProvider {
    public MaterialTraitsDataProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addTraits(IafCore.Materials.iron, IafCore.Materials.dragon_armor, TinkerModifiers.magnetic.getId(), ModifierIds.projectileProtection);
        addTraits(IafCore.Materials.copper, IafCore.Materials.dragon_armor, TinkerModifiers.dwarven.getId(), ModifierIds.depthProtection);
        addTraits(IafCore.Materials.gold, IafCore.Materials.dragon_armor, TinkerModifiers.golden.getId(), ModifierIds.magicProtection);
        addTraits(IafCore.Materials.silver, IafCore.Materials.dragon_armor, TinkerModifiers.frostshield.getId(), ModifierIds.consecrated);
        addTraits(IafCore.Materials.diamond, IafCore.Materials.dragon_armor, ModifierIds.diamond, ModifierIds.boundless);
        addTraits(IafCore.Materials.fire, IafCore.Materials.dragon_armor, ModifierIds.dragonborn, ModifierIds.fireProtection);
        addTraits(IafCore.Materials.ice, IafCore.Materials.dragon_armor, ModifierIds.dragonborn, ModifierIds.magicProtection);
        addTraits(IafCore.Materials.lightning, IafCore.Materials.dragon_armor, ModifierIds.dragonborn, ModifierIds.blastProtection);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Material Traits";
    }
}