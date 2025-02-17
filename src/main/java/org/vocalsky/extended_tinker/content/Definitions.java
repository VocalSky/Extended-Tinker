package org.vocalsky.extended_tinker.content;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;

public class Definitions {
    public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;
    public Definitions() {}
    static {
        HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
    }
}
