package org.vocalsky.extended_tinker.compat.iaf;

import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;

public class IafMaterials {
    static public void registry() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        for (DragonArmorMaterialStats material : DragonArmorMaterialStats.values())
            registry.registerStatType(material.getType());
    }
}
