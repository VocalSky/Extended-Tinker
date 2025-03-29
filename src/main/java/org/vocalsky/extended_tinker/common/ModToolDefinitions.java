package org.vocalsky.extended_tinker.common;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class ModToolDefinitions {
    public static final ToolDefinition FIRECRACK;

    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;

    public ModToolDefinitions() {}

    static {
        FIRECRACK = ToolDefinition.create(ModItems.Tools.FIRECRACK);
        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
    }
}
