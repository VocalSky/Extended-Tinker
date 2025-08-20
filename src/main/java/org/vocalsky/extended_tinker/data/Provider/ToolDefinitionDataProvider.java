package org.vocalsky.extended_tinker.data.Provider;

import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.ModToolDefinitions;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import org.vocalsky.extended_tinker.compat.golem.GolemToolDefinitions;
import org.vocalsky.extended_tinker.compat.iaf.IafToolDefinitions;
//import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonPlatingMaterialStats;
import org.vocalsky.extended_tinker.compat.iaf.tool.stats.DragonArmorMaterialStats;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.json.predicate.modifier.ModifierPredicate;
import slimeknights.tconstruct.library.json.predicate.modifier.SingleModifierPredicate;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.ToolModule;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.SetStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.interaction.PreferenceSetInteraction;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.MaterialStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartsModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.ArmorDefinitions;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import java.util.List;

public class ToolDefinitionDataProvider extends AbstractToolDefinitionDataProvider {
    RandomMaterial tier1Material = RandomMaterial.random().tier(1).build();
    RandomMaterial randomMaterial = RandomMaterial.random().build();
    DefaultMaterialsModule defaultTwoParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material).build();
    DefaultMaterialsModule defaultThreeParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material).build();
    DefaultMaterialsModule defaultFourParts = DefaultMaterialsModule.builder().material(tier1Material, tier1Material, tier1Material, tier1Material).build();
    DefaultMaterialsModule defaultAncient = DefaultMaterialsModule.builder().material(randomMaterial, randomMaterial).build();

    public ToolDefinitionDataProvider(PackOutput packOutput) {
        super(packOutput, Extended_tinker.MODID);
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
            .module(PartStatsModule.parts().part(TinkerToolParts.maille).part(TinkerToolParts.shieldCore).part(TinkerToolParts.maille).part(TinkerToolParts.shieldCore).part(ModItems.Parts.BRIDLE).build())
            .module(DefaultMaterialsModule.builder().material(MaterialIds.cobalt).material(MaterialIds.ancientHide).build())
            .module(ArmorItem.Type.CHESTPLATE, new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).set(ToolStats.ARMOR, 2.0f).set(ToolStats.ARMOR_TOUGHNESS, 1.5f).set(ToolStats.KNOCKBACK_RESISTANCE, 1.25f).build()))
            .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());

        defineArmor(GolemToolDefinitions.GOLEM_ARMOR_MATERIAL)
            .modules(slots -> PartStatsModule.armor(slots)
                    .part(GolemItems.Parts.GOLEM_PLATING, 1)
                    .part(TinkerToolParts.maille, 1)
                    .part(TinkerToolParts.maille, 1)
                    .part(TinkerToolParts.shieldCore, 1)
                    .part(TinkerToolParts.shieldCore, 1)
            )
            .module(DefaultMaterialsModule.builder().material(MaterialIds.cobalt).material(MaterialIds.ancientHide).build())
            .module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 2.0f).set(ToolStats.ARMOR, 2.0f).set(ToolStats.ARMOR_TOUGHNESS, 1.5f).set(ToolStats.KNOCKBACK_RESISTANCE, 1.5f).build()))
            .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());

        for (ItemDragonArmor.DragonArmorType type : ItemDragonArmor.DragonArmorType.values())
            defineArmor(IafToolDefinitions.DRAGON_ARMOR_MATERIAL.get(type))
            .module(slots -> MaterialStatsModule.stats().stat(DragonArmorMaterialStats.stats.get(type)).stat(PlatingMaterialStats.TYPES.get(slots.ordinal())).build())
            .module(DefaultMaterialsModule.builder().material(MaterialIds.cobalt).material(MaterialIds.ancientHide).build())
            .module(new MultiplyStatsModule(MultiplierNBT.builder().set(ToolStats.DURABILITY, 5.0f).set(ToolStats.ARMOR, 2.5f).set(ToolStats.ARMOR_TOUGHNESS, 1.75f).set(ToolStats.KNOCKBACK_RESISTANCE, 1.5f).build()))
            .module(ToolSlotsModule.builder().slots(SlotType.ABILITY, 1).slots(SlotType.UPGRADE, 3).slots(SlotType.DEFENSE, 1).build());
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Tool Definition Data Provider";
    }
}
