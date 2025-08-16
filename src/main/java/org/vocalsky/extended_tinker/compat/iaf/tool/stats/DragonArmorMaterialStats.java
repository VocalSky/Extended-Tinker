package org.vocalsky.extended_tinker.compat.iaf.tool.stats;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public record DragonArmorMaterialStats(int durability, int armor) implements IRepairableMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(Extended_tinker.getResource("dragonarmor"));
    public static final MaterialStatType<DragonArmorMaterialStats> TYPE = new MaterialStatType<>(ID, new DragonArmorMaterialStats(1, 0), RecordLoadable.create(
            IRepairableMaterialStats.DURABILITY_FIELD,
            IntLoadable.FROM_ZERO.defaultField("armor", 0, true, DragonArmorMaterialStats::armor),
            DragonArmorMaterialStats::new));
    // tooltip descriptions
    private static final List<Component> DESCRIPTION = List.of(ToolStats.DURABILITY.getDescription(), ToolStats.ARMOR.getDescription());

    @Override
    public @NotNull MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public @NotNull List<Component> getLocalizedInfo() {
        return List.of(
                ToolStats.DURABILITY.formatValue(this.durability),
                ToolStats.ARMOR.formatValue(this.armor)
        );
    }

    @Override
    public @NotNull List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.update(builder, durability * scale);
        ToolStats.ARMOR.update(builder, armor * scale);
    }
}
