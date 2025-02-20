package org.vocalsky.extended_tinker.common;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class Definitions {
    // Tool Definitions
    public static final ToolDefinition FIREWORK_ROCKET;

    // Armor Definitions
    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;

    public Definitions() {}

    static {
        FIREWORK_ROCKET = ToolDefinition.create(Items.FIREWORK_ROCKET);
        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
    }
}
