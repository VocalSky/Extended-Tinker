package org.vocalsky.extended_tinker.golems;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.common.ModToolDefinitions;
import org.vocalsky.extended_tinker.common.tool.Firecrack;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
import org.vocalsky.extended_tinker.golems.tool.GolemArmor;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.item.armor.MultilayerArmorItem;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.ArmorDefinitions;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GolemItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, TConstruct.MOD_ID);
    public static final RegistryObject<CreativeModeTab> CommonTab = CREATIVE_TABS.register(
    "golem", () -> CreativeModeTab.builder().title(Extended_tinker.makeTranslation("itemGroup", "items"))
                                        .icon(() -> Tools.GOLEM_ARMOR.get(ArmorItem.Type.CHESTPLATE).getRenderTool())
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
        }

        private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
            item.get().addVariants(output, "");
        }

        private static final Item.Properties PART_PROP = CommonItem;
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
    }

    public static class Tools {
        public static void init() {}

        private static final Item.Properties TOOL_PROP = Stack1Item;

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            acceptTools(output, GOLEM_ARMOR);
        }

        public static final EnumObject<ArmorItem.Type, GolemArmor> GOLEM_ARMOR = ITEMS.registerEnum("golem", ArmorItem.Type.values(), type -> new GolemArmor(ArmorDefinitions.PLATE, type, TOOL_PROP));

        private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
            ToolBuildHandler.addVariants(output, tool.get(), "");
        }

        /** Adds a tool to the tab */
        private static void acceptTools(Consumer<ItemStack> output, EnumObject<?,? extends IModifiable> tools) {
            tools.forEach(tool -> ToolBuildHandler.addVariants(output, tool, ""));
        }
    }
}