package org.vocalsky.extended_tinker.compat.iaf.tool.stats;

import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.modules.ArmorModuleBuilder;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.EnumMap;
import java.util.List;

public record DragonArmorMaterialStats(MaterialStatType<?> getType, int durability, float armor, float toughness, float knockbackResistance) implements IRepairableMaterialStats {
    private static final LoadableField<Float, DragonArmorMaterialStats> TOUGHNESS = FloatLoadable.FROM_ZERO.defaultField("toughness", 0f, DragonArmorMaterialStats::toughness);
    private static final LoadableField<Float, DragonArmorMaterialStats> KNOCKBACK_RESISTANCE = FloatLoadable.FROM_ZERO.defaultField("knockback_resistance", 0f, DragonArmorMaterialStats::knockbackResistance);
    private static final RecordLoadable<DragonArmorMaterialStats> LOADABLE = RecordLoadable.create(
            MaterialStatType.CONTEXT_KEY.requiredField(),
            IRepairableMaterialStats.DURABILITY_FIELD,
            FloatLoadable.FROM_ZERO.defaultField("armor", 0f, true, DragonArmorMaterialStats::armor),
            TOUGHNESS,
            KNOCKBACK_RESISTANCE,
            DragonArmorMaterialStats::new);
    private static final List<Component> DESCRIPTION = List.of(
            ToolStats.DURABILITY.getDescription(),
            ToolStats.ARMOR.getDescription(),
            ToolStats.ARMOR_TOUGHNESS.getDescription(),
            ToolStats.KNOCKBACK_RESISTANCE.getDescription());
    private static final List<Component> SHIELD_DESCRIPTION = List.of(
            ToolStats.DURABILITY.getDescription(),
            ToolStats.ARMOR_TOUGHNESS.getDescription(),
            ToolStats.KNOCKBACK_RESISTANCE.getDescription());
    /* Types */
    public static final MaterialStatType<DragonArmorMaterialStats> HEAD = makeType("head_core");
    public static final MaterialStatType<DragonArmorMaterialStats> BODY = makeType("body_core");
    public static final MaterialStatType<DragonArmorMaterialStats> NECK = makeType("neck_core");
    public static final MaterialStatType<DragonArmorMaterialStats> TAIL = makeType("tail_core");
    /** Shield loadable does not support armor */
    public static final MaterialStatType<DragonArmorMaterialStats> BANNER = new MaterialStatType<DragonArmorMaterialStats>(new MaterialStatsId(Extended_tinker.MODID, "banner_core"), type -> new DragonArmorMaterialStats(type, 1, 0, 0, 0), RecordLoadable.create(
            MaterialStatType.CONTEXT_KEY.requiredField(), IRepairableMaterialStats.DURABILITY_FIELD, TOUGHNESS, KNOCKBACK_RESISTANCE,
            (type, durability, toughness, knockbackResistance) -> new DragonArmorMaterialStats(type, durability, 0, toughness, knockbackResistance)));
    /** All types including shield */
    public static final List<MaterialStatType<DragonArmorMaterialStats>> TYPES = List.of(HEAD, NECK, BODY, TAIL, BANNER);

    @Override
    public @NotNull List<Component> getLocalizedInfo() {
        Component durability = ToolStats.DURABILITY.formatValue(this.durability);
        Component toughness = ToolStats.ARMOR_TOUGHNESS.formatValue(this.toughness);
        Component knockbackResistance = ToolStats.KNOCKBACK_RESISTANCE.formatValue(this.knockbackResistance * 10); // multiply by 10 as vanilla multiplies toughness by 10 for display
        if (getType == BANNER) {
            return List.of(durability, toughness, knockbackResistance);
        }
        return List.of(durability, ToolStats.ARMOR.formatValue(this.armor), toughness, knockbackResistance);
    }

    @Override
    public @NotNull List<Component> getLocalizedDescriptions() {
        return getType == BANNER ? SHIELD_DESCRIPTION : DESCRIPTION;
    }

    @Override
    public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.update(builder, durability * scale);
        ToolStats.ARMOR.update(builder, armor * scale);
        ToolStats.ARMOR_TOUGHNESS.update(builder, toughness * scale);
        ToolStats.KNOCKBACK_RESISTANCE.update(builder, knockbackResistance * scale);
    }

    /** Makes a stat type for the given name */
    private static MaterialStatType<DragonArmorMaterialStats> makeType(String name) {
        return new MaterialStatType<DragonArmorMaterialStats>(new MaterialStatsId(Extended_tinker.MODID, name), type -> new DragonArmorMaterialStats(type, 1, 0, 0, 0), LOADABLE);
    }


    public static DragonArmorMaterialStats.Builder builder() {
        return new DragonArmorMaterialStats.Builder();
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder implements ArmorModuleBuilder.ArmorShieldModuleBuilder<DragonArmorMaterialStats> {
        private final int[] durability = new int[4];
        private int shieldDurability = 0;
        private final float[] armor = new float[4];
        private float toughness = 0;
        private float knockbackResistance = 0;

        private Builder() {}

        public DragonArmorMaterialStats.Builder durabilityFactor(float maxDamageFactor) {
            for (ArmorItem.Type slotType : ArmorItem.Type.values()) {
                int index = slotType.ordinal();
                durability[index] = (int)(ArmorModuleBuilder.MAX_DAMAGE_ARRAY[index] * maxDamageFactor);
            }
            if (shieldDurability == 0) {
                shieldDurability = (int)(maxDamageFactor * 18);
            }
            return this;
        }

        public DragonArmorMaterialStats.Builder armor(float boots, float leggings, float chestplate, float helmet) {
            armor[ArmorItem.Type.BOOTS.ordinal()] = boots;
            armor[ArmorItem.Type.LEGGINGS.ordinal()] = leggings;
            armor[ArmorItem.Type.CHESTPLATE.ordinal()] = chestplate;
            armor[ArmorItem.Type.HELMET.ordinal()] = helmet;
            return this;
        }

        @Override
        public @NotNull DragonArmorMaterialStats build(ArmorItem.Type slot) {
            int index = slot.ordinal();
            return new DragonArmorMaterialStats(TYPES.get(index), durability[index], armor[index], toughness, knockbackResistance);
        }

        @Override
        public @NotNull DragonArmorMaterialStats buildShield() {
            return new DragonArmorMaterialStats(DragonArmorMaterialStats.BANNER, shieldDurability, 0, toughness, knockbackResistance);
        }
    }
}
