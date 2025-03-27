package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.tool.Firecrack;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

public class ModTools {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    private static final Item.Properties TOOL_PROP = new Item.Properties().stacksTo(1).tab(ModTab.INSTANCE);
    public static void registers(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }

    public static final ItemObject<Firecrack> FIRECRACK = ITEMS.register("firecrack", () -> new Firecrack(TOOL_PROP, ModToolDefinitions.FIRECRACK));

    public static final ItemObject<HorseArmor> HORSE_ARMOR = ITEMS.register( "horse_armor_chestplate", () -> new HorseArmor(ModToolDefinitions.HORSE_ARMOR_MATERIAL, ArmorSlotType.CHESTPLATE, TOOL_PROP));
}
