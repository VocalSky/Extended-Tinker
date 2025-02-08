package org.vocalsky.extended_tinker.register;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.content.tools.FishingRod;
import org.vocalsky.extended_tinker.content.armors.HorseArmor;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

public class Items {
    public static final CreativeModeTab Extended_Tinker_Tab = new CreativeModeTab("extended_tinker") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Items.FISHING_ROD.get());
        }
    };
    private static final Item.Properties Stack1Item = new Item.Properties().stacksTo(1).tab(Extended_Tinker_Tab);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Extended_tinker.MODID);
    public static final RegistryObject<ModifiableItem>  FISHING_ROD = ITEMS.register( "fishing_rod", () -> new FishingRod(Stack1Item, Definitions.FISHING_ROD));
    public static final RegistryObject<HorseArmor>  HORSE_ARMOR = ITEMS.register( "horse_armor", () -> new HorseArmor(Definitions.HORSE_ARMOR_MATERIAL, EquipmentSlot.CHEST, Stack1Item, Definitions.HORSE_ARMOR));

    public static void registers(IEventBus eventBus)  {
        ITEMS.register(eventBus);
    }
}
