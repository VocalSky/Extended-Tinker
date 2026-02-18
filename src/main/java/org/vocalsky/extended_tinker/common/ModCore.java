package org.vocalsky.extended_tinker.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.client.FirecrackEntityRender;
import org.vocalsky.extended_tinker.common.client.FireworkRocketEntityRender;
import org.vocalsky.extended_tinker.common.entity.FireworkEntity;
import org.vocalsky.extended_tinker.common.entity.FireworkRocketEntity;
import org.vocalsky.extended_tinker.common.item.ExpTransferOrb;
import org.vocalsky.extended_tinker.common.logic.FireworkDispenserBehavior;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.*;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorAsoneModifier;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorPainlessModifier;
import org.vocalsky.extended_tinker.common.recipe.FireworkStarModifierRecipe;
import org.vocalsky.extended_tinker.common.recipe.ToolExpExportRecipe;
import org.vocalsky.extended_tinker.common.recipe.ToolExpImportRecipe;
import org.vocalsky.extended_tinker.common.tool.Firecrack;
import org.vocalsky.extended_tinker.common.tool.FireworkRocketItem;
import org.vocalsky.extended_tinker.common.tool.HorseArmorItem;
//import org.vocalsky.extended_tinker.util.ModCastItemObject;
import slimeknights.mantle.Mantle;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.recipe.helper.SimpleRecipeSerializer;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.mantle.registration.deferred.ItemDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierSalvage;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.logic.ModifiableArrowDispenserBehavior;
import slimeknights.tconstruct.tools.logic.ModifiableShurikenDispenserBehavior;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
        Tags.init();
        Parts.init();
        Casts.init();
        Tools.init();
        Entities.init();
        Modifiers.init();
        ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        Entities.registers(eventBus);
        Modifiers.registers(eventBus);
    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        System.out.println("modCore commonSetup firecrack");
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(Tools.FIRECRACK.get(), FireworkDispenserBehavior.INSTANCE);
        });
    }

    public static boolean LevellingAddonLoaded() { return ModList.get().isLoaded("tinkerslevellingaddon"); }
    public static ItemObject<ExpTransferOrb> ExpTransferOrb = ITEMS.register("exp_transfer_orb", () -> new ExpTransferOrb(Stack1Item));

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Parts.addTabItems(itemDisplayParameters, tab);
        Casts.addTabItems(itemDisplayParameters, tab);
        Tools.addTabItems(itemDisplayParameters, tab);
        if (LevellingAddonLoaded()) tab.accept(ExpTransferOrb);
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

        public static class Definitions {
            public static void init() {}

            public static final ToolDefinition FIRECRACK;
            public static final ToolDefinition FIREWORK_ROCKET;

            public static final ModifiableArmorMaterial HORSE_ARMOR_MATERIAL;

            static {
                FIRECRACK = ToolDefinition.create(ModCore.Tools.FIRECRACK);
                FIREWORK_ROCKET = ToolDefinition.create(ModCore.Tools.FIREWORK_ROCKET);
                HORSE_ARMOR_MATERIAL = ModifiableArmorMaterial.create(Extended_tinker.getResource("horse_armor"), SoundEvents.HORSE_ARMOR);
            }
        }


        public static void init() {
            Definitions.init();
        }

        private static final Item.Properties TOOL_PROP = Stack1Item;

        private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
            Consumer<ItemStack> output = tab::accept;
            acceptTool(output, HORSE_ARMOR);
            acceptTool(output, FIRECRACK);
        }

        public static final ItemObject<Firecrack> FIRECRACK = ITEMS.register("firecrack", () -> new Firecrack(TOOL_PROP, Definitions.FIRECRACK));
        public static final ItemObject<FireworkRocketItem> FIREWORK_ROCKET = ITEMS.register("firework_rocket", () -> new FireworkRocketItem(CommonItem, Definitions.FIREWORK_ROCKET));
        public static final ItemObject<HorseArmorItem> HORSE_ARMOR = ITEMS.register( "horse_armor_chestplate", () -> new HorseArmorItem(Definitions.HORSE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, TOOL_PROP));

        private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
            ToolBuildHandler.addVariants(output, tool.get(), "");
        }

        /** Adds a tool to the tab */
        private static void acceptTools(Consumer<ItemStack> output, EnumObject<?,? extends IModifiable> tools) {
            tools.forEach(tool -> ToolBuildHandler.addVariants(output, tool, ""));
        }
    }

    public static class Tags {
        public static void init() {}

        public static final TagKey<Item> FIREWORK_FLINT = local("firework_flint");

        private static TagKey<Item> local(String name) {
            return TagKey.create(Registries.ITEM, TConstruct.getResource(name));
        }

        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, Mantle.commonResource(name));
        }
    }

    @Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Entities {
        public static void init() {}

        public static final EntityTypeDeferredRegister ENTITYS;
        public static RegistryObject<EntityType<FireworkEntity>> firecrackEntity;
        public static RegistryObject<EntityType<FireworkRocketEntity>> fireworkRocketEntity;

        static {
            ENTITYS = new EntityTypeDeferredRegister(Extended_tinker.MODID);
            firecrackEntity = ENTITYS.register("firecrack", () -> EntityType.Builder.<FireworkEntity>of(FireworkEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new FireworkEntity(firecrackEntity.get(), world)).setShouldReceiveVelocityUpdates(true));
            fireworkRocketEntity = ENTITYS.register("firework_rocket", () -> EntityType.Builder.<FireworkRocketEntity>of(FireworkRocketEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory(((spawnEntity, level) -> new FireworkRocketEntity(fireworkRocketEntity.get(), level))).setShouldReceiveVelocityUpdates(true));
        }

        public static void registers(IEventBus eventBus)  {
            ENTITYS.register(eventBus);
        }

        @SubscribeEvent
        public static void bakeAttributes(EntityAttributeCreationEvent creationEvent) {
        }
    }

    @Mod.EventBusSubscriber(modid = Extended_tinker.MODID, value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Renders {
        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModCore.Entities.firecrackEntity.get(), FirecrackEntityRender::new);
            event.registerEntityRenderer(ModCore.Entities.fireworkRocketEntity.get(), FireworkRocketEntityRender::new);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        }
    }

    @Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Modifiers {
        public static class Ids {
            private static ModifierId id(String name) { return new ModifierId(Extended_tinker.MODID, name); }
            public static ModifierId painless = id("painless_horsearmor");
            public static ModifierId asone = id("asone_horsearmor");
            public static ModifierId firework_flight = id("firework_flight");
            public static ModifierId shoot_firework = id("shoot_firework");
            public static ModifierId firework_star = id("firework_star");
            public static ModifierId firework_rocket = id("firework_rocket");
            public static ModifierId safety_firework = id("safety_firework");
        }

        public static void init() {}
        public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Extended_tinker.MODID);

        public static void registers(IEventBus bus) {
            MODIFIERS.register(bus);
            RECIPE_SERIALIZERS.register(bus);
        }
        protected static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Extended_tinker.MODID);

        @SubscribeEvent
        public static void registerSerializers(RegisterEvent event) {
            if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
                ModifierModule.LOADER.register(Extended_tinker.getResource("shoot_firework"), ShootFireworkModule.LOADER);
                ModifierModule.LOADER.register(Extended_tinker.getResource("firework_inventory"), FireworkInventoryModule.LOADER);
                ModifierModule.LOADER.register(Extended_tinker.getResource("firework_rocket"), FireworkRocketModule.LOADER);
            }
        }
        public static StaticModifier<FireworkFlight> FIREWORK_FLIGHT = MODIFIERS.register("firework_flight", FireworkFlight::new);
        public static StaticModifier<FireworkStarModifier> FIREWORK_STAR = MODIFIERS.register("firework_star", FireworkStarModifier::new);

        public static final RegistryObject<RecipeSerializer<FireworkStarModifierRecipe>> STAR_SERIALIZER = RECIPE_SERIALIZERS.register("star_modifier", () -> LoadableRecipeSerializer.of(FireworkStarModifierRecipe.LOADER));
        public static final RegistryObject<RecipeSerializer<ModifierSalvage>> STAR_SALVAGE_SERIALIZER = RECIPE_SERIALIZERS.register("star_modifier_salvage", () -> LoadableRecipeSerializer.of(ModifierSalvage.LOADER));
        public static final RegistryObject<RecipeSerializer<ToolExpExportRecipe>> TOOL_EXP_EXPORT_SERIALIZER = RECIPE_SERIALIZERS.register("exp_export", () -> LoadableRecipeSerializer.of(ToolExpExportRecipe.LOADER));
        public static final RegistryObject<RecipeSerializer<ToolExpImportRecipe>> TOOL_EXP_IMPORT_SERIALIZER = RECIPE_SERIALIZERS.register("exp_import", () -> new SimpleRecipeSerializer<>(ToolExpImportRecipe::new));
    }

}