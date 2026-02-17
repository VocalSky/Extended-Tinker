package org.vocalsky.extended_tinker.compat.iaf;

import com.csdy.tcondiadema.frames.CsdyRegistries;
import com.csdy.tcondiadema.frames.diadema.DiademaType;
import com.csdy.tcondiadema.modifier.CommonDiademaModifier;
import com.csdy.tcondiadema.modifier.DiademaModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
//import org.vocalsky.extended_tinker.compat.iaf.materials.IafMaterialRegistry;
import org.vocalsky.extended_tinker.compat.iaf.diadema.BurnstheSky.BurnstheSkyDiadema;
import org.vocalsky.extended_tinker.compat.iaf.diadema.MagneticStormSurge.MagneticStormSurgeDiadema;
import org.vocalsky.extended_tinker.compat.iaf.diadema.Permafrost.PermaforstDiadema;
import org.vocalsky.extended_tinker.compat.iaf.tool.DragonArmorItem;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tables.TinkerTables;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IafCore {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Extended_tinker.MODID);
    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, Extended_tinker.MODID);
    public static final RegistryObject<CreativeModeTab> IafTab = CREATIVE_TABS.register(
    "iaf", () -> CreativeModeTab.builder().title(Extended_tinker.makeTranslation("itemGroup", "iaf_items"))
                                        .icon(() -> Tools.DRAGON_ARMOR.get(DragonArmorItem.Type.HEAD).getRenderTool())
                                        .displayItems(IafCore::addTabItems)
                                        .withTabsBefore(TinkerTables.tabTables.getId())
                                        .withSearchBar()
                                        .build());
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1);
    private static final Item.Properties CommonItem = new Item.Properties();
    public static void registers(IEventBus eventBus)  {
        Parts.init();
        Casts.init();
        Tools.init();
        Modifiers.init();
        Materials.init();
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        Modifiers.MODIFIERS.register(eventBus);
        if (Extended_tinker.DiademaLoadable())
            Modifiers.Diademas.DIADEMA_TYPES.register(eventBus);
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
            DRAGON_ARMOR_CORE.forEach((type, toolPartItem) -> {
                    toolPartItem.addVariants(output, "");
            });
        }

        private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
            item.get().addVariants(output, "");
        }

        private static final Item.Properties PART_PROP = CommonItem;
        public static final EnumObject<DragonArmorItem.Type, ToolPartItem> DRAGON_ARMOR_CORE = ITEMS.registerEnum("dragon_armor_core", DragonArmorItem.Type.values(), type -> new ToolPartItem(PART_PROP, DragonArmorMaterialStats.TYPES.get(type.getOrder()).getId()));
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
        public static class Definitions {
            public static void init() {}

            public static final ModifiableArmorMaterial DRAGON_ARMOR_MATERIAL;

            static  {
                DRAGON_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("dragon_armor"), SoundEvents.ARMOR_EQUIP_GENERIC);
            }
        }

        public static void init() {
            Definitions.init();
        }

        private static final Item.Properties TOOL_PROP = Stack1Item;

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            acceptTools(output, DRAGON_ARMOR);
        }

        public static final EnumObject<DragonArmorItem.Type, DragonArmorItem> DRAGON_ARMOR = ITEMS.registerEnum("dragonarmor", DragonArmorItem.Type.values(), type -> new DragonArmorItem(Definitions.DRAGON_ARMOR_MATERIAL, type, TOOL_PROP));

        private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
            ToolBuildHandler.addVariants(output, tool.get(), "");
        }

        /** Adds a tool to the tab */
        private static void acceptTools(Consumer<ItemStack> output, EnumObject<?,? extends IModifiable> tools) {
            tools.forEach(tool -> ToolBuildHandler.addVariants(output, tool, ""));
        }
    }

    public static class Modifiers {
        public static void init() {
            if (Extended_tinker.DiademaLoadable()) Diademas.init();
        }
        public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Extended_tinker.MODID);
        public static final StaticModifier<DiademaModifier> MagneticStormSurge = MODIFIERS.register("magnetic_storm_surge", CommonDiademaModifier.Create(Diademas.MagneticStormSurge));
        public static final StaticModifier<DiademaModifier> BurnstheSky = MODIFIERS.register("burns_the_sky", CommonDiademaModifier.Create(Diademas.BurnstheSky));
        public static final StaticModifier<DiademaModifier> Permafrost = MODIFIERS.register("permafrost", CommonDiademaModifier.Create(Diademas.Permafrost));

        public static class Diademas {
            public static void init() {}

            public static final DeferredRegister<DiademaType> DIADEMA_TYPES = DeferredRegister.create(CsdyRegistries.DIADEMA_TYPE, Extended_tinker.MODID);

            public static final RegistryObject<DiademaType> MagneticStormSurge = DIADEMA_TYPES.register("magnetic_storm_surge", () -> DiademaType.create(MagneticStormSurgeDiadema::new));
            public static final RegistryObject<DiademaType> BurnstheSky = DIADEMA_TYPES.register("burns_the_sky", () -> DiademaType.create(BurnstheSkyDiadema::new));
            public static final RegistryObject<DiademaType> Permafrost = DIADEMA_TYPES.register("permafrost", () -> DiademaType.create(PermaforstDiadema::new));
        }
    }

    public static class Materials {
        public static void init() {}
        private static MaterialId id(String name) {
            return new MaterialId(Extended_tinker.MODID, name);
        }

        public static final MaterialId iron = id("dragon_armor_iron");
        public static final MaterialId copper = id("dragon_armor_copper");
        public static final MaterialId silver = id("dragon_armor_silver");
        public static final MaterialId gold = id("dragon_armor_gold");
        public static final MaterialId diamond = id("dragon_armor_diamond");
        public static final MaterialId fire = id("dragon_armor_fire");
        public static final MaterialId ice = id("dragon_armor_ice");
        public static final MaterialId lightning = id("dragon_armor_lightning");

        public static final MaterialStatsId dragon_armor = new MaterialStatsId(Extended_tinker.getResource("dragon_armor"));

        static public void registry() {
            IMaterialRegistry registry = MaterialRegistry.getInstance();
            for (MaterialStatType<?> type : DragonArmorMaterialStats.TYPES) registry.registerStatType(type, dragon_armor);
        }
    }

}