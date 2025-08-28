package org.vocalsky.extended_tinker.data.Provider;


import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
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
        addMaterial(IafMaterials.copper, 1, ORDER_COMPAT, false);
        addMaterial(IafMaterials.iron, 2, ORDER_COMPAT, false);
        addMaterial(IafMaterials.gold, 2, ORDER_COMPAT, false);
        addMaterial(IafMaterials.silver, 2, ORDER_COMPAT, false);
        addMaterial(IafMaterials.diamond, 3, ORDER_COMPAT, false);
        addMaterial(IafMaterials.fire, 4, ORDER_COMPAT, false);
        addMaterial(IafMaterials.ice, 4, ORDER_COMPAT, false);
        addMaterial(IafMaterials.lightning, 4, ORDER_COMPAT, false);
    }

    private static MaterialId id(String name) {
        return new MaterialId(Extended_tinker.MODID, name);
    }
}