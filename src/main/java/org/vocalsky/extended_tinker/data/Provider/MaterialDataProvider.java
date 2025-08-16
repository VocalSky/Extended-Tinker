package org.vocalsky.extended_tinker.data.Provider;


import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import static slimeknights.mantle.Mantle.commonResource;

public class MaterialDataProvider extends AbstractMaterialDataProvider {
    public MaterialDataProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public String getName() {
        return "Extended Tinker Materials";
    }

    @Override
    protected void addMaterials() {
    }

    private static MaterialId id(String name) {
        return new MaterialId(Extended_tinker.MODID, name);
    }
}