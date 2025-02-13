package org.vocalsky.extended_tinker.content;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;

public class Definitions {
//    public static final ToolDefinition HORSE_ARMOR;

    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;
    public Definitions() {}
    static {
//        HORSE_ARMOR = ToolDefinition.create(Items.HORSE_ARMOR);

        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
    }
}
