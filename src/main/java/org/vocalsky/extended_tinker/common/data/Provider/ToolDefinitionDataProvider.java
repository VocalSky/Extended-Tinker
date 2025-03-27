package org.vocalsky.extended_tinker.common.data.Provider;

import net.minecraft.data.DataGenerator;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.ModParts;
import org.vocalsky.extended_tinker.common.ModToolDefinitions;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.item.ArmorSlotType;

public class ToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    RandomMaterial tier1Material = RandomMaterial.random().tier(1).build();
    RandomMaterial randomMaterial = RandomMaterial.random().build();
    DefaultMaterialsModule defaultTwoParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material).build();
    DefaultMaterialsModule defaultThreeParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material).build();
    DefaultMaterialsModule defaultFourParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material, tier1Material).build();
    DefaultMaterialsModule defaultAncient = DefaultMaterialsModule.builder().material(randomMaterial, randomMaterial).build();

    public ToolDefinitionDataProvider(DataGenerator generator) {
        super(generator, Extended_tinker.MODID);
    }

    @Override
    protected void addToolDefinitions() {
        define(ModToolDefinitions.FIRECRACK)
            .module(PartStatsModule.parts().part(TinkerToolParts.toolHandle).part(TinkerToolParts.bowLimb).build())
            .module(defaultTwoParts)
            .module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).build()))
            .module(ToolTraitsModule.builder().trait(ModModifiers.FLIGHT.getId(), 1).build())
            .module(ToolSlotsModule.builder().slots(SlotType.UPGRADE, 3).slots(SlotType.ABILITY, 3).build());

        defineArmor(ModToolDefinitions.HORSE_ARMOR_MATERIAL)
            .module(PartStatsModule.parts().part(TinkerToolParts.maille).part(TinkerToolParts.shieldCore).part(TinkerToolParts.maille).part(TinkerToolParts.shieldCore).part(ModParts.BRIDLE).build())
            .module(DefaultMaterialsModule.builder().material(MaterialIds.cobalt).material(MaterialIds.ancientHide).build())
            .module(ArmorSlotType.CHESTPLATE, new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).set(ToolStats.ARMOR, 2.0f).set(ToolStats.ARMOR_TOUGHNESS, 1.5f).set(ToolStats.KNOCKBACK_RESISTANCE, 1.25f).build()))
            .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());

    }

    @Override
    public String getName() {
        return "Extended Tinker Tool Definition Data Provider";
    }
}
