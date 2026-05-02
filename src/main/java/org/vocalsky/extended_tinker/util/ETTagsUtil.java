package org.vocalsky.extended_tinker.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.TConstruct;

public class ETTagsUtil {
    public static TagKey<Item> local(String name) {
        return TagKey.create(Registries.ITEM, Extended_tinker.getResource(name));
    }
}
