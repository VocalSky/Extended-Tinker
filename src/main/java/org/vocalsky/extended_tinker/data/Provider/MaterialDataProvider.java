package org.vocalsky.extended_tinker.data.Provider;


import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class MaterialDataProvider extends AbstractMaterialDataProvider {
    public MaterialDataProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Materials";
    }

    @Override
    protected void addMaterials() {
        addMaterial(IafCore.Materials.copper, 1, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.iron, 2, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.gold, 2, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.silver, 2, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.diamond, 3, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.fire, 4, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.ice, 4, ORDER_COMPAT, false);
        addMaterial(IafCore.Materials.lightning, 4, ORDER_COMPAT, false);
    }

    private static MaterialId id(String name) {
        return new MaterialId(Extended_tinker.MODID, name);
    }
}