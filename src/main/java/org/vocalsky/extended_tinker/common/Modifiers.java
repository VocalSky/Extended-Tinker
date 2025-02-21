package org.vocalsky.extended_tinker.common;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.modifier.FireworkRocket.FirecrackStarModifier;
import org.vocalsky.extended_tinker.common.modifier.recipe.FirecrackStarRecipe;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorAsoneModifier;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorPainlessModifier;
import org.vocalsky.extended_tinker.common.modifier.recipe.ModifierRecipeProvider;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerModule;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class Modifiers extends TinkerModule {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Extended_tinker.MODID);
    private static final ItemDeferredRegisterExtension ITEM = new ItemDeferredRegisterExtension(Extended_tinker.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Extended_tinker.MODID);
    public static void Init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        ITEM.register(bus);
        MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    protected static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TConstruct.MOD_ID);

    public static StaticModifier<Modifier> PAINLESS = MODIFIERS.register("painless_horsearmor", HorseArmorPainlessModifier::new);
    public static StaticModifier<Modifier> ASONE = MODIFIERS.register("asone_horsearmor", HorseArmorAsoneModifier::new);
    public static StaticModifier<FirecrackStarModifier> firecrackStar = MODIFIERS.register("firecrack_star", FirecrackStarModifier::new);
    public static final RegistryObject<RecipeSerializer<FirecrackStarRecipe>> firecrackStarSerializer = RECIPE_SERIALIZERS.register("firecrack_star_modifier", () -> LoadableRecipeSerializer.of(FirecrackStarRecipe.LOADER));

    @SubscribeEvent
    void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        boolean server = event.includeServer();
        generator.addProvider(server, new ModifierRecipeProvider(generator));
    }
}
