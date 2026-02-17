package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.compat.golem.GolemCore;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
import org.vocalsky.extended_tinker.compat.iaf.tool.DragonArmorItem;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.display.FixedMaterialToolName;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.MaterialStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartsModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.List;

public class ToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    RandomMaterial tier1Material = RandomMaterial.random().tier(1).build();
    RandomMaterial randomMaterial = RandomMaterial.random().build();
    RandomMaterial anyMaterial = RandomMaterial.random().allowHidden().build();
    RandomMaterial nonHiddenMaterial = RandomMaterial.random().build();
    DefaultMaterialsModule defaultTwoParts = DefaultMaterialsModule.builder().material(nonHiddenMaterial, nonHiddenMaterial).build();
    DefaultMaterialsModule defaultThreeParts = DefaultMaterialsModule.builder().material(nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial).build();
    DefaultMaterialsModule defaultFourParts = DefaultMaterialsModule.builder().material(nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial).build();
    DefaultMaterialsModule defaultFiveParts = DefaultMaterialsModule.builder().material(nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial, nonHiddenMaterial).build();
    DefaultMaterialsModule defaultAncient = DefaultMaterialsModule.builder().material(nonHiddenMaterial, nonHiddenMaterial).build();

    public ToolDefinitionDataProvider(PackOutput packOutput) {
        super(packOutput, Extended_tinker.MODID);
    }

    @Override
    protected void addToolDefinitions() {
        define(ModCore.Tools.Definitions.FIRECRACK)
            .module(PartStatsModule.parts().part(TinkerToolParts.largePlate).part(TinkerToolParts.arrowShaft).build())
            .module(defaultTwoParts)
            .module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).build()))
            .module(ToolTraitsModule.builder().trait(ModCore.Modifiers.FIREWORK_FLIGHT.getId(), 1).build())
            .module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 3).slots(SlotType.ABILITY, 3).build());
        define(ModCore.Tools.Definitions.FIREWORK_ROCKET)
            .module(PartStatsModule.parts().part(TinkerToolParts.smallBlade).part(TinkerToolParts.arrowShaft).build())
            .module(defaultTwoParts)
            .module(FixedMaterialToolName.FIRST);

        defineArmor(ModCore.Tools.Definitions.HORSE_ARMOR_MATERIAL)
            .module(PartStatsModule.parts().part(TinkerToolParts.maille).part(TinkerToolParts.shieldCore).part(TinkerToolParts.maille).part(TinkerToolParts.shieldCore).part(ModCore.Parts.BRIDLE).build())
            .module(defaultFiveParts)
            .module(ArmorItem.Type.CHESTPLATE, new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).set(ToolStats.ARMOR, 2.0f).set(ToolStats.ARMOR_TOUGHNESS, 1.5f).set(ToolStats.KNOCKBACK_RESISTANCE, 1.25f).build()))
            .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());

        defineArmor(GolemCore.Tools.Definitions.GOLEM_ARMOR_MATERIAL)
            .modules(slots -> PartStatsModule.armor(slots)
                    .part(GolemCore.Parts.GOLEM_PLATING, 1)
                    .part(TinkerToolParts.maille, 1)
                    .part(TinkerToolParts.maille, 1)
                    .part(TinkerToolParts.shieldCore, 1)
                    .part(TinkerToolParts.shieldCore, 1)
            )
            .module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).set(ToolStats.ARMOR, 2.0f).set(ToolStats.ARMOR_TOUGHNESS, 1.5f).set(ToolStats.KNOCKBACK_RESISTANCE, 1.5f).build()))
                .module(defaultFiveParts)
                .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());

        defineArmor(IafCore.Tools.Definitions.DRAGON_ARMOR_MATERIAL)
        .module(slots -> MaterialStatsModule.stats()
                .stat(DragonArmorMaterialStats.TYPES.get(slots.ordinal()).getId())
                .stat(PlatingMaterialStats.TYPES.get(slots.ordinal()).getId())
                .primaryPart(1).build())
        .module(slots -> new PartsModule(List.of(IafCore.Parts.DRAGON_ARMOR_CORE.get(DragonArmorItem.Type.values()[slots.ordinal()]))))
        .module(DefaultMaterialsModule.builder().material(randomMaterial, randomMaterial).build())
        .module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 7.5f).set(ToolStats.ARMOR, 5.0f).set(ToolStats.ARMOR_TOUGHNESS, 2.75f).set(ToolStats.KNOCKBACK_RESISTANCE, 7.0f).build()))
        .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Tool Definition Data Provider";
    }
}
