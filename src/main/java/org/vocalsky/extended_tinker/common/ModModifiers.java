package org.vocalsky.extended_tinker.common;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.data.Provider.*;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FirecrackFlightModifier;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FirecrackStarModifier;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorAsoneModifier;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorPainlessModifier;
import org.vocalsky.extended_tinker.common.recipe.FirecrackStarModifierRecipe;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

@Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModifiers {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Extended_tinker.MODID);
    public static void registers(IEventBus bus) {
        MODIFIERS.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
    protected static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Extended_tinker.MODID);

    public static StaticModifier<Modifier> PAINLESS = MODIFIERS.register("painless_horsearmor", HorseArmorPainlessModifier::new);
    public static StaticModifier<Modifier> ASONE = MODIFIERS.register("asone_horsearmor", HorseArmorAsoneModifier::new);
    public static StaticModifier<Modifier> FLIGHT = MODIFIERS.register("flight_firecrack", FirecrackFlightModifier::new);
    public static StaticModifier<FirecrackStarModifier> STAR = MODIFIERS.register("star_firecrack", FirecrackStarModifier::new);

    public static final RegistryObject<SimpleRecipeSerializer<FirecrackStarModifierRecipe>> STAR_SERIALIZER = RECIPE_SERIALIZERS.register("star_modifier", () -> new SimpleRecipeSerializer<>(FirecrackStarModifierRecipe::new));

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        if (STAR_SERIALIZER.isPresent()) {
        System.out.println("TTFTTFDATA");
        }
        else {
            System.out.println("XYXYADASD");
        }
//        System.out.println("TTFTTFDATA");
        DataGenerator generator = event.getGenerator();
        boolean server = event.includeServer();
        boolean client = event.includeClient();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(server, new ModifierProvider(generator));
        generator.addProvider(server, new ModifierRecipeProvider(generator));
        generator.addProvider(server, new ModifierTagProvider(generator, existingFileHelper));
    }
}
