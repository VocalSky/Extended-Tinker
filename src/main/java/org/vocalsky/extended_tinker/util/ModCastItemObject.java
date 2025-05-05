package org.vocalsky.extended_tinker.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registration.CastItemObject;

import java.util.function.Supplier;

public class ModCastItemObject extends CastItemObject {
    private final TagKey<Item> singleUseTag;
    private final TagKey<Item> multiUseTag;

    public ModCastItemObject(ResourceLocation name, Item gold, Item sand, Item redSand) {
        super(name, gold, sand, redSand);
        singleUseTag = makeTag("single_use");
        multiUseTag = makeTag("multi_use");
    }

    public ModCastItemObject(ResourceLocation name, ItemObject<? extends Item> gold, Supplier<? extends Item> sand, Supplier<? extends Item> redSand) {
        super(name, gold, sand, redSand);
        singleUseTag = makeTag("single_use");
        multiUseTag = makeTag("multi_use");
    }

    @Override
    protected @NotNull TagKey<Item> makeTag(@NotNull String type) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(TConstruct.MOD_ID, "casts/" + type + "/" + getName().getPath()));
    }

    @Override
    public @NotNull TagKey<Item> getSingleUseTag() {
        return singleUseTag;
    }

    @Override
    public @NotNull TagKey<Item> getMultiUseTag() {
        return multiUseTag;
    }
}
