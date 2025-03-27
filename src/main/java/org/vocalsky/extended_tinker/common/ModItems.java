package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;

public class ModItems {
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1).tab(ModTab.INSTANCE);
    private static final Item.Properties CommonItem = new Item.Properties().tab(ModTab.INSTANCE);
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    public static void registers(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}