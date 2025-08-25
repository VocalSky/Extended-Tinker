package org.vocalsky.extended_tinker.compat.iaf;

import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class IafMaterials {
    private static MaterialId id(String name) {
        return new MaterialId(Extended_tinker.MODID, name);
    }

    public static final MaterialId silver = id("dragon_armor_silver");
    public static final MaterialId diamond = id("dragon_armor_diamond");
    public static final MaterialId fire = id("dragon_armor_fire");
    public static final MaterialId ice = id("dragon_armor_ice");
    public static final MaterialId lightning = id("dragon_armor_lightning");

    static public void registry() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        for (DragonArmorMaterialStats material : DragonArmorMaterialStats.values())
            registry.registerStatType(material.getType());
    }
}
