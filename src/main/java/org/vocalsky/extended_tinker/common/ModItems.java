package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.tool.Firecrack;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
import org.vocalsky.extended_tinker.util.ModCastItemObject;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.item.ArmorSlotType;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.function.Supplier;

public class ModItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    public static final CreativeModeTab TAB = new CreativeModeTab(Extended_tinker.MODID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return Tools.HORSE_ARMOR.get().getRenderTool();
        }
    };
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1).tab(TAB);
    private static final Item.Properties CommonItem = new Item.Properties().tab(TAB);
    public static void registers(IEventBus eventBus)  {
        Parts.init();
        Casts.init();
        Tools.init();
        ITEMS.register(eventBus);
    }

    public static class Parts {
        public static void init() {}

        private static final Item.Properties PART_PROP = new Item.Properties().tab(TAB);
        public static final ItemObject<ToolPartItem> BRIDLE = ITEMS.register("bridle", () -> new ToolPartItem(PART_PROP, PlatingMaterialStats.CHESTPLATE.getId()));
    }

    public static class Casts {
        public static void init() {}

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

        private static final Item.Properties CAST_PROPS = new Item.Properties().tab(TAB);

        public static final ModCastItemObject BRIDLE_CAST = registerCast(Parts.BRIDLE, CAST_PROPS);
    }

    public static class Tools {
        public static void init() {}

        private static final Item.Properties TOOL_PROP = new Item.Properties().stacksTo(1).tab(TAB);

        public static final ItemObject<Firecrack> FIRECRACK = ITEMS.register("firecrack", () -> new Firecrack(TOOL_PROP, ModToolDefinitions.FIRECRACK));
        public static final ItemObject<HorseArmor> HORSE_ARMOR = ITEMS.register( "horse_armor_chestplate", () -> new HorseArmor(ModToolDefinitions.HORSE_ARMOR_MATERIAL, ArmorSlotType.CHESTPLATE, TOOL_PROP));
    }
}