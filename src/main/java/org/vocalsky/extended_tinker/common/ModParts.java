package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

public class ModParts {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    private static final Item.Properties PART_PROP = new Item.Properties().tab(ModTab.INSTANCE);
    public static void registers(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }

    public static final ItemObject<ToolPartItem> BRIDLE = ITEMS.register("bridle", () -> new ToolPartItem(PART_PROP, PlatingMaterialStats.CHESTPLATE.getId()));
}
