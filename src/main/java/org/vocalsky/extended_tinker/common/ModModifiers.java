package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
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
import slimeknights.tconstruct.library.recipe.modifiers.ModifierSalvage;

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

    public static final RegistryObject<RecipeSerializer<FirecrackStarModifierRecipe>> STAR_SERIALIZER = RECIPE_SERIALIZERS.register("star_modifier", () -> LoadableRecipeSerializer.of(FirecrackStarModifierRecipe.LOADER));
    public static final RegistryObject<RecipeSerializer<ModifierSalvage>> STAR_SALVAGE_SERIALIZER = RECIPE_SERIALIZERS.register("star_modifier_salvage", () -> LoadableRecipeSerializer.of(ModifierSalvage.LOADER));
}
