package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.modifier.FireworkRocket.FireworkRocketStarModifier;
import org.vocalsky.extended_tinker.common.modifier.FireworkRocket.FireworkRocketStarRecipe;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorAsoneModifier;
import org.vocalsky.extended_tinker.common.modifier.HorseArmor.HorseArmorPainlessModifier;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.tconstruct.common.TinkerModule;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.tools.recipe.ArmorDyeingRecipe;

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

    public static StaticModifier<Modifier> PAINLESS = MODIFIERS.register("painless_horsearmor", HorseArmorPainlessModifier::new);
    public static StaticModifier<Modifier> ASONE = MODIFIERS.register("asone_horsearmor", HorseArmorAsoneModifier::new);
    public static StaticModifier<Modifier> fireworkRocketStar = MODIFIERS.register("firework_rocket_star", FireworkRocketStarModifier::new);
    public static final RegistryObject<RecipeSerializer<FireworkRocketStarRecipe>> fireworkRocketStarSerializer = RECIPE_SERIALIZERS.register("firework_rocket_star_modifier", () -> LoadableRecipeSerializer.of(FireworkRocketStarRecipe.LOADER));
}
