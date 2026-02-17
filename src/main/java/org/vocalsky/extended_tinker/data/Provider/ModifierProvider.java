package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FireworkInventoryModule;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FireworkRocketModule;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.ShootFireworkModule;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierSlotModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier.TooltipDisplay;
import slimeknights.tconstruct.library.tools.capability.inventory.InventoryMenuModule;

import static slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial.ARMOR_SLOTS;

public class ModifierProvider extends AbstractModifierProvider {

    public ModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        EquipmentSlot[] handSlots = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
        EquipmentSlot[] armorSlots = ARMOR_SLOTS;
        EquipmentSlot[] armorMainHand = {EquipmentSlot.MAINHAND, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};
        ModifierSlotModule UPGRADE = ModifierSlotModule.slot(SlotType.UPGRADE).eachLevel(1);
        ModifierSlotModule ABILITY = ModifierSlotModule.slot(SlotType.ABILITY).eachLevel(1);
        buildModifier(ModCore.Modifiers.PAINLESS.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(ABILITY);
        buildModifier(ModCore.Modifiers.AS_ONE.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(ABILITY);
        buildModifier(ModCore.Modifiers.FIREWORK_FLIGHT.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.DEFAULT).addModule(UPGRADE);
        buildModifier(ModCore.Modifiers.FIREWORK_STAR.getId()).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.DEFAULT).addModule(ABILITY);
        buildModifier(ModCore.Modifiers.shoot_firework).priority(200)
            .addModule(FireworkInventoryModule.builder().pattern(pattern("fireworks")).flatLimit(1).slotsPerLevel(1))
            .addModule(ShootFireworkModule.INSTANCE)
            .addModule(InventoryMenuModule.ANY)
            .addModule(ABILITY)
            .tooltipDisplay(TooltipDisplay.TINKER_STATION)
            .levelDisplay(ModifierLevelDisplay.DEFAULT);
        buildModifier(ModCore.Modifiers.firework_rocket).priority(200)
            .addModule(FireworkRocketModule.INSTANCE);
        buildModifier(ModCore.Modifiers.safety_firework).addModule(ABILITY).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
    }

    private static Pattern pattern(String name) {
        return new Pattern(Extended_tinker.MODID, name);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Modifier Provider";
    }
}
