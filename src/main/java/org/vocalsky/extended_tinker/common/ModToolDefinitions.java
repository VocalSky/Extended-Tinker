package org.vocalsky.extended_tinker.common;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class ModToolDefinitions {
    public static final ToolDefinition FIRECRACK;
    public static final ToolDefinition FIREWORK_ROCKET;

    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;

    public ModToolDefinitions() {}

    static {
        FIRECRACK = ToolDefinition.create(ModCore.Tools.FIRECRACK);
        FIREWORK_ROCKET = ToolDefinition.create(ModCore.Tools.FIREWORK_ROCKET);
        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
    }
}
