package org.vocalsky.extended_tinker.compat.golem;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.compat.golem.tool.GolemArmor;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.item.DummyMaterialItem;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.ArmorDefinitions;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GolemItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, Extended_tinker.MODID);
    public static final RegistryObject<CreativeModeTab> GolemTab = CREATIVE_TABS.register(
    "golem", () -> CreativeModeTab.builder().title(Extended_tinker.makeTranslation("itemGroup", "golem_items"))
                                        .icon(() -> Tools.GOLEM_ARMOR.get(ArmorItem.Type.HELMET).getRenderTool())
                                        .displayItems(GolemItems::addTabItems)
                                        .withTabsBefore(TinkerTables.tabTables.getId())
                                        .withSearchBar()
                                        .build());
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1);
    private static final Item.Properties CommonItem = new Item.Properties();
    public static boolean Loadable() { return ModList.get().isLoaded("modulargolems"); }
    public static void registers(IEventBus eventBus)  {
        if (!Loadable()) return;
        Parts.init();
        Casts.init();
        Tools.init();
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
    }

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Parts.addTabItems(itemDisplayParameters, tab);
        Casts.addTabItems(itemDisplayParameters, tab);
        Tools.addTabItems(itemDisplayParameters, tab);
    }

    public static class Parts {
        public static void init() {}

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            for (ArmorItem.Type type : ArmorItem.Type.values()) {
                tab.accept(DUMMY_GOLEM_PLATING.get(type));
                GOLEM_PLATING.get(type).addVariants(output, "");
            }
        }

        private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
            item.get().addVariants(output, "");
        }

        private static final Item.Properties PART_PROP = CommonItem;

        public static final EnumObject<ArmorItem.Type, ToolPartItem> GOLEM_PLATING = ITEMS.registerEnum(ArmorItem.Type.values(), "golem_plating", type -> new ToolPartItem(PART_PROP, PlatingMaterialStats.TYPES.get(type.ordinal()).getId()));
        public static final EnumObject<ArmorItem.Type, DummyMaterialItem> DUMMY_GOLEM_PLATING = ITEMS.registerEnum(ArmorItem.Type.values(), "golem_plating_dummy", type -> new DummyMaterialItem(PART_PROP));
    }

    public static class Casts {
        public static void init() {}

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            addCasts(tab, CastItemObject::get);
            addCasts(tab, CastItemObject::getSand);
            addCasts(tab, CastItemObject::getRedSand);
        }

        private static void addCasts(CreativeModeTab.Output output, Function<CastItemObject, ItemLike> getter) {
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

        public static final EnumObject<ArmorItem.Type, CastItemObject> GOLEM_PLATING_CAST = new EnumObject.Builder<ArmorItem.Type, CastItemObject>(ArmorItem.Type.class)
                .put(ArmorItem.Type.HELMET, registerCast("helmet_golem_plating", () -> new PartCastItem(CAST_PROPS, () -> Parts.GOLEM_PLATING.get(ArmorItem.Type.HELMET))))
                .put(ArmorItem.Type.CHESTPLATE, registerCast("chestplate_golem_plating", () -> new PartCastItem(CAST_PROPS, () -> Parts.GOLEM_PLATING.get(ArmorItem.Type.CHESTPLATE))))
                .put(ArmorItem.Type.LEGGINGS, registerCast("leggings_golem_plating", () -> new PartCastItem(CAST_PROPS, () -> Parts.GOLEM_PLATING.get(ArmorItem.Type.LEGGINGS))))
                .build();
    }

    public static class Tools {
        public static void init() {}

        private static final Item.Properties TOOL_PROP = Stack1Item;

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            acceptTools(output, GOLEM_ARMOR);
        }

        public static final EnumObject<ArmorItem.Type, GolemArmor> GOLEM_ARMOR = ITEMS.registerEnum("golem", new ArmorItem.Type[]{ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS}, type -> new GolemArmor(GolemToolDefinitions.GOLEM_ARMOR_MATERIAL, type, TOOL_PROP));

        private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
            ToolBuildHandler.addVariants(output, tool.get(), "");
        }

        /** Adds a tool to the tab */
        private static void acceptTools(Consumer<ItemStack> output, EnumObject<?,? extends IModifiable> tools) {
            tools.forEach(tool -> ToolBuildHandler.addVariants(output, tool, ""));
        }
    }
}