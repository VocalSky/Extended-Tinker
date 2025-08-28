package org.vocalsky.extended_tinker.compat.iaf;

import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import static org.vocalsky.extended_tinker.compat.iaf.IafCore.Loadable;
import static slimeknights.tconstruct.library.materials.MaterialRegistry.ARMOR;

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

    public static final MaterialStatsId dragon_armor = new MaterialStatsId(Extended_tinker.getResource("dragon_armor"));

    static public void registry() {
        if (!Loadable()) return;
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        for (MaterialStatType<?> type : DragonArmorMaterialStats.TYPES) registry.registerStatType(type, dragon_armor);
    }
}
