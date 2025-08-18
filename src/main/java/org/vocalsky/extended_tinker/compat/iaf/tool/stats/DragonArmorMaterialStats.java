package org.vocalsky.extended_tinker.compat.iaf.tool.stats;

import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
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
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import java.util.EnumMap;
import java.util.List;

public enum DragonArmorMaterialStats implements IRepairableMaterialStats {
    IRON("dragonarmor_iron", 1650, 2),
    COPPER("dragonarmor_copper", 1450, 2),
    GOLD("dragonarmor_gold", 1250, 0),
    SILVER("dragonarmor_silver", 1650, 2),
    DIAMOND("dragonarmor_diamond", 2150, 4),
    FIRE("dragonarmor_fire", 2150, 4),
    ICE("dragonarmor_ice", 2150, 4),
    LIGHTNING("dragonarmor_lightning", 2150, 4);

    public static final EnumMap<ItemDragonArmor.DragonArmorType, DragonArmorMaterialStats> stats = new EnumMap<>(ItemDragonArmor.DragonArmorType.class);

    static {
        stats.put(ItemDragonArmor.DragonArmorType.IRON, IRON);
        stats.put(ItemDragonArmor.DragonArmorType.COPPER, COPPER);
        stats.put(ItemDragonArmor.DragonArmorType.GOLD, GOLD);
        stats.put(ItemDragonArmor.DragonArmorType.SILVER, SILVER);
        stats.put(ItemDragonArmor.DragonArmorType.DIAMOND, DIAMOND);
        stats.put(ItemDragonArmor.DragonArmorType.FIRE, FIRE);
        stats.put(ItemDragonArmor.DragonArmorType.ICE, ICE);
        stats.put(ItemDragonArmor.DragonArmorType.LIGHTNING, LIGHTNING);
    }

    // tooltip descriptions
    private static final List<Component> DESCRIPTION = List.of(ToolStats.DURABILITY.getDescription(), ToolStats.ARMOR.getDescription());

    private final MaterialStatType<DragonArmorMaterialStats> type;
    private final int durability;
    private final int armor;

    DragonArmorMaterialStats(String name, int durability, int armor) {
        this.type = MaterialStatType.singleton(new MaterialStatsId(Extended_tinker.getResource(name)), this);
        this.durability = durability;
        this.armor = armor;
    }

    @Override
    public @NotNull MaterialStatType<?> getType() {
        return type;
    }

    public @NotNull MaterialStatsId getId() {
        return type.getId();
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

    @Override
    public int durability() {
        return durability;
    }
}
