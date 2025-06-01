package org.vocalsky.extended_tinker.golem;

import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;

public class GolemToolDefinitions {
    public static final ModifiableArmorMaterial GOLEM_ARMOR_MATERIAL;

    public GolemToolDefinitions() {}

    static {
        GOLEM_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("golem_armor"), SoundEvents.ARMOR_EQUIP_GENERIC);
    }
}
