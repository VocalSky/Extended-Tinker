package org.vocalsky.extended_tinker.compat.iaf;

import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import static org.vocalsky.extended_tinker.compat.iaf.IafCore.Loadable;

public class IafMaterials {
    private static MaterialId id(String name) {
        return new MaterialId(Extended_tinker.MODID, name);
    }

    public static final MaterialId iron = id("dragon_armor_iron");
    public static final MaterialId copper = id("dragon_armor_copper");
    public static final MaterialId silver = id("dragon_armor_silver");
    public static final MaterialId gold = id("dragon_armor_gold");
    public static final MaterialId diamond = id("dragon_armor_diamond");
    public static final MaterialId fire = id("dragon_armor_fire");
    public static final MaterialId ice = id("dragon_armor_ice");
    public static final MaterialId lightning = id("dragon_armor_lightning");

    static public void registry() {
        if (!Loadable()) return;
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        for (DragonArmorMaterialStats material : DragonArmorMaterialStats.values())
            registry.registerStatType(material.getType());
    }
}
