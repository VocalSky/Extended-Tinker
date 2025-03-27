package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.util.ModCastItemObject;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import java.util.function.Supplier;

public class ModCasts {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegisterExtension(Extended_tinker.MODID);
    private static ModCastItemObject registerCast(String name, Supplier<? extends Item> constructor) {
        ItemObject<Item> cast = ITEMS.register(name + "_cast", constructor);
        ItemObject<Item> sandCast = ITEMS.register(name + "_sand_cast", constructor);
        ItemObject<Item> redSandCast = ITEMS.register(name + "_red_sand_cast", constructor);
        return new ModCastItemObject(Extended_tinker.getResource(name), cast, sandCast, redSandCast);
    }

    private static ModCastItemObject registerCast(String name, Item.Properties props) {
        return registerCast(name, (Supplier)(() -> new Item(props)));
    }

    private static ModCastItemObject registerCast(ItemObject<? extends IMaterialItem> item, Item.Properties props) {
        return registerCast(item.getId().getPath(), (Supplier)(() -> new PartCastItem(props, item)));
    }

    private static final Item.Properties CAST_PROPS = new Item.Properties().tab(ModTab.INSTANCE);

    public static final ModCastItemObject BRIDLE_CAST = registerCast(ModParts.BRIDLE, CAST_PROPS);

    public static void registers(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}
