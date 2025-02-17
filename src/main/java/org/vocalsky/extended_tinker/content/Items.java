package org.vocalsky.extended_tinker.content;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.content.tools.HorseArmor;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.item.ArmorSlotType;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

public class Items {
    public static final CreativeModeTab Extended_Tinker_Tab = new CreativeModeTab("extended_tinker") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return HORSE_ARMOR.get().getRenderTool();
        }
    };
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1).tab(Extended_Tinker_Tab);
    private static final Item.Properties CommonItem = new Item.Properties().tab(Extended_Tinker_Tab);
    private static Item register() {return new Item(CommonItem);}

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);

    public static final ItemObject<HorseArmor> HORSE_ARMOR = ITEMS.register( "horse_armor_chestplate", () -> new HorseArmor(Definitions.HORSE_ARMOR_MATERIAL, ArmorSlotType.CHESTPLATE, Stack1Item));

    public static final ItemObject<ToolPartItem> BRIDLE = ITEMS.register("bridle", () -> new ToolPartItem(CommonItem, PlatingMaterialStats.CHESTPLATE.getId()));

    public static final ItemObject<Item> BRIDLE_SAND_CAST = ITEMS.register("bridle_sand_cast", Items::register);
    public static final ItemObject<Item> BRIDLE_RED_SAND_CAST = ITEMS.register("bridle_red_sand_cast", Items::register);
    public static final ItemObject<Item> BRIDLE_GOLD_CAST = ITEMS.register("bridle_gold_cast", Items::register);

    public static void registers(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}
