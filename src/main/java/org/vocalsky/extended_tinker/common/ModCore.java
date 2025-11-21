package org.vocalsky.extended_tinker.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.item.ExpTransfer;
import org.vocalsky.extended_tinker.common.tool.Firecrack;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
//import org.vocalsky.extended_tinker.util.ModCastItemObject;
import pyre.tinkerslevellingaddon.TinkersLevellingAddon;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModCore {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, Extended_tinker.MODID);
    public static final RegistryObject<CreativeModeTab> CommonTab = CREATIVE_TABS.register(
    "common", () -> CreativeModeTab.builder().title(Extended_tinker.makeTranslation("itemGroup", "common_items"))
                                        .icon(() -> Tools.HORSE_ARMOR.get().getRenderTool())
                                        .displayItems(ModCore::addTabItems)
                                        .withTabsBefore(TinkerTables.tabTables.getId())
                                        .withSearchBar()
                                        .build());
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1);
    private static final Item.Properties CommonItem = new Item.Properties();
    public static void registers(IEventBus eventBus)  {
        Parts.init();
        Casts.init();
        Tools.init();
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        ModEntity.registers(eventBus);
        ModModifiers.registers(eventBus);
    }

    public static boolean LevellingAddonLoaded() { return ModList.get().isLoaded("tinkerslevellingaddon"); }
    public static ItemObject<ExpTransfer> ExpTransfer = ITEMS.register("exp_transfer", () -> new ExpTransfer(CommonItem));

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Parts.addTabItems(itemDisplayParameters, tab);
        Casts.addTabItems(itemDisplayParameters, tab);
        Tools.addTabItems(itemDisplayParameters, tab);
        if (LevellingAddonLoaded()) tab.accept(ExpTransfer);
    }

    public static class Parts {
        public static void init() {}

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            accept(output, BRIDLE);
        }

        private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
            item.get().addVariants(output, "");
        }

        private static final Item.Properties PART_PROP = CommonItem;

        public static final ItemObject<ToolPartItem> BRIDLE = ITEMS.register("bridle", () -> new ToolPartItem(PART_PROP, PlatingMaterialStats.CHESTPLATE.getId()));
    }

    public static class Casts {
        public static void init() {}

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            addCasts(tab, CastItemObject::get);
            addCasts(tab, CastItemObject::getSand);
            addCasts(tab, CastItemObject::getRedSand);
        }

        private static void addCasts(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter) {
            accept(output, getter, BRIDLE_CAST);
        }

        private static void accept(CreativeModeTab.Output output, Function<CastItemObject,ItemLike> getter, CastItemObject cast) {
            output.accept(getter.apply(cast));
        }

        private static CastItemObject registerCast(String name, Supplier<? extends Item> constructor) {
            ItemObject<Item> cast = ITEMS.register(name + "_cast", constructor);
            ItemObject<Item> sandCast = ITEMS.register(name + "_sand_cast", constructor);
            ItemObject<Item> redSandCast = ITEMS.register(name + "_red_sand_cast", constructor);
            return new CastItemObject(Extended_tinker.getResource(name), cast, sandCast, redSandCast);
        }

        private static CastItemObject registerCast(String name, Item.Properties props) {
            return registerCast(name, (() -> new Item(props)));
        }

        private static CastItemObject registerCast(ItemObject<? extends IMaterialItem> item, Item.Properties props) {
            return registerCast(item.getId().getPath(), (() -> new PartCastItem(props, item)));
        }

        private static final Item.Properties CAST_PROPS = CommonItem;

        public static final CastItemObject BRIDLE_CAST = registerCast(Parts.BRIDLE, CAST_PROPS);
    }

    public static class Tools {
        public static void init() {}

        private static final Item.Properties TOOL_PROP = Stack1Item;

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            acceptTool(output, HORSE_ARMOR);
            acceptTool(output, FIRECRACK);
        }

        public static final ItemObject<Firecrack> FIRECRACK = ITEMS.register("firecrack", () -> new Firecrack(TOOL_PROP, ModToolDefinitions.FIRECRACK));
        public static final ItemObject<HorseArmor> HORSE_ARMOR = ITEMS.register( "horse_armor_chestplate", () -> new HorseArmor(ModToolDefinitions.HORSE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, TOOL_PROP));

        private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
            ToolBuildHandler.addVariants(output, tool.get(), "");
        }

        /** Adds a tool to the tab */
        private static void acceptTools(Consumer<ItemStack> output, EnumObject<?,? extends IModifiable> tools) {
            tools.forEach(tool -> ToolBuildHandler.addVariants(output, tool, ""));
        }
    }
}