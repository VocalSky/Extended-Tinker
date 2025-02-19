package org.vocalsky.extended_tinker.content;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class Definitions {
    // Tool Definitions
    public static final ToolDefinition FIRECRACK;

    // Armor Definitions
    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;

    public Definitions() {}

    static {
        FIRECRACK = ToolDefinition.create(Items.FIRECRACK);
        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
    }
}
