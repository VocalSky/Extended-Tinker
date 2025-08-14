package org.vocalsky.extended_tinker.compat.iaf;

import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.part.PartCastItem;

import java.util.EnumMap;

public class IafToolDefinitions {
    public static final EnumMap<ItemDragonArmor.DragonArmorType, ModifiableArmorMaterial> DRAGON_ARMOR_MATERIAL= new EnumMap<>(ItemDragonArmor.DragonArmorType.class);

    public IafToolDefinitions() {}

    static {
        for (ItemDragonArmor.DragonArmorType type : ItemDragonArmor.DragonArmorType.values())
            DRAGON_ARMOR_MATERIAL.put(type, ModifiableArmorMaterial.create(Extended_tinker.getResource("dragon_" + type.name().toLowerCase() + "_armor"), SoundEvents.ARMOR_EQUIP_GENERIC));
    }
}
