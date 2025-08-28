package org.vocalsky.extended_tinker.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.TConstruct;

public class ETTagsUtil {
    public static final TagKey<Item> DRAGON_ARMOR = local("dragon_armor");
    public static final TagKey<Item> DRAGON_ARMOR_LIGHTNING = local("dragon_armor_lightning");
    public static final TagKey<Item> DRAGON_ARMOR_FIRE = local("dragon_armor_fire");
    public static final TagKey<Item> DRAGON_ARMOR_ICE = local("dragon_armor_ice");

    private static TagKey<Item> local(String name) {
        return TagKey.create(Registries.ITEM, Extended_tinker.getResource(name));
    }
}
