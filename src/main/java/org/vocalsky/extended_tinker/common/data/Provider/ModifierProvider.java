package org.vocalsky.extended_tinker.common.data.Provider;

//import net.minecraft.data.DataGenerator;
//import net.minecraft.world.entity.EquipmentSlot;
//import org.jetbrains.annotations.NotNull;
//import org.vocalsky.extended_tinker.common.ModModifiers;
//import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
//import slimeknights.tconstruct.library.modifiers.modules.build.ModifierSlotModule;
//import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
//import slimeknights.tconstruct.library.tools.SlotType;
//import slimeknights.tconstruct.library.modifiers.impl.BasicModifier.TooltipDisplay;
//
//import static slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial.ARMOR_SLOTS;
//
//public class ModifierProvider extends AbstractModifierProvider {
//    public ModifierProvider(DataGenerator generator) {
//        super(generator);
//    }
//
//    @Override
//    protected void addModifiers() {    EquipmentSlot[] handSlots = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
//        EquipmentSlot[] armorSlots = ARMOR_SLOTS;
//        EquipmentSlot[] armorMainHand = {EquipmentSlot.MAINHAND, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};
//        ModifierSlotModule UPGRADE = new ModifierSlotModule(SlotType.UPGRADE);
//        ModifierSlotModule ABILITY = new ModifierSlotModule(SlotType.ABILITY);
//        buildModifier(ModModifiers.PAINLESS.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(ABILITY);
//        buildModifier(ModModifiers.ASONE.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(ABILITY);
//        buildModifier(ModModifiers.FLIGHT.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.DEFAULT).addModule(UPGRADE);
//        buildModifier(ModModifiers.STAR.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(ABILITY);
//    }
//
//    @Override
//    public @NotNull String getName() {
//        return "Extended Tinker Modifier Provider";
//    }
//}
