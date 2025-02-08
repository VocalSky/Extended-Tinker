package org.vocalsky.extended_tinker.register;

import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class Definitions {
    public static final ToolDefinition FISHING_ROD;

    public static final ToolDefinition HORSE_ARMOR;

    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;
    public Definitions() {}
    static {
        FISHING_ROD = ToolDefinition.create(Items.FISHING_ROD);

        HORSE_ARMOR = ToolDefinition.create(Items.HORSE_ARMOR);

        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), Sounds.EQUIP_SLIME.getSound());
    }
}
