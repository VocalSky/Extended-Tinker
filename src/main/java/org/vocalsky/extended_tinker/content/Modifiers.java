package org.vocalsky.extended_tinker.content;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.content.modifiers.HorseArmor.HorseArmorAsoneModifier;
import org.vocalsky.extended_tinker.content.modifiers.HorseArmor.HorseArmorPainlessModifier;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class Modifiers {
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
}
