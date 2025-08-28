package org.vocalsky.extended_tinker.compat.iaf;

import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.sounds.SoundEvents;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;

import java.util.EnumMap;

public class IafToolDefinitions {
    public static final ModifiableArmorMaterial DRAGON_ARMOR_MATERIAL= ModifiableArmorMaterial.create(Extended_tinker.getResource("dragon_armor"), SoundEvents.ARMOR_EQUIP_GENERIC);

    public IafToolDefinitions() {}
}
