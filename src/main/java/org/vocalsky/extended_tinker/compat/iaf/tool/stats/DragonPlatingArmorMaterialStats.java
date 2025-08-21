package org.vocalsky.extended_tinker.compat.iaf.tool.stats;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modules.ArmorModuleBuilder;

import java.util.List;

public record DragonPlatingArmorMaterialStats(MaterialStatType<?> getType, int durability, float armor, float toughness, float knockbackResistance) implements IRepairableMaterialStats {
    private static final LoadableField<Float, DragonPlatingArmorMaterialStats> TOUGHNESS = FloatLoadable.FROM_ZERO.defaultField("toughness", 0f, DragonPlatingArmorMaterialStats::toughness);
    private static final LoadableField<Float, DragonPlatingArmorMaterialStats> KNOCKBACK_RESISTANCE = FloatLoadable.FROM_ZERO.defaultField("knockback_resistance", 0f, DragonPlatingArmorMaterialStats::knockbackResistance);
    private static final RecordLoadable<DragonPlatingArmorMaterialStats> LOADABLE = RecordLoadable.create(
            MaterialStatType.CONTEXT_KEY.requiredField(),
            IRepairableMaterialStats.DURABILITY_FIELD,
            FloatLoadable.FROM_ZERO.defaultField("dragon_plating_armor", 0f, true, DragonPlatingArmorMaterialStats::armor),
            TOUGHNESS,
            KNOCKBACK_RESISTANCE,
            DragonPlatingArmorMaterialStats::new);
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
    public static final MaterialStatType<DragonPlatingArmorMaterialStats> HEAD = makeType("dragon_plating_head");
    public static final MaterialStatType<DragonPlatingArmorMaterialStats> BODY = makeType("dragon_plating_body");
    public static final MaterialStatType<DragonPlatingArmorMaterialStats> NECK = makeType("dragon_plating_neck");
    public static final MaterialStatType<DragonPlatingArmorMaterialStats> TAIL = makeType("dragon_plating_tail");
    /** Shield loadable does not support armor */
    public static final MaterialStatType<DragonPlatingArmorMaterialStats> BANNER = new MaterialStatType<DragonPlatingArmorMaterialStats>(new MaterialStatsId(TConstruct.MOD_ID, "dragon_plating_banner"), type -> new DragonPlatingArmorMaterialStats(type, 1, 0, 0, 0), RecordLoadable.create(
            MaterialStatType.CONTEXT_KEY.requiredField(), IRepairableMaterialStats.DURABILITY_FIELD, TOUGHNESS, KNOCKBACK_RESISTANCE,
            (type, durability, toughness, knockbackResistance) -> new DragonPlatingArmorMaterialStats(type, durability, 0, toughness, knockbackResistance)));
    /** All types including shield */
    public static final List<MaterialStatType<DragonPlatingArmorMaterialStats>> TYPES = List.of(HEAD, BODY, NECK, TAIL, BANNER);

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

    private static MaterialStatType<DragonPlatingArmorMaterialStats> makeType(String name) {
        return new MaterialStatType<DragonPlatingArmorMaterialStats>(new MaterialStatsId(Extended_tinker.MODID, name), type -> new DragonPlatingArmorMaterialStats(type, 1, 0, 0, 0), LOADABLE);
    }


    public static Builder builder() {
        return new Builder();
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder implements ArmorModuleBuilder.ArmorShieldModuleBuilder<DragonPlatingArmorMaterialStats> {
        private final int[] durability = new int[4];
        private int shieldDurability = 0;
        private final float[] armor = new float[4];
        private float toughness = 0;
        private float knockbackResistance = 0;

        private Builder() {}

        public Builder durabilityFactor(float maxDamageFactor) {
            for (ArmorItem.Type slotType : ArmorItem.Type.values()) {
                int index = slotType.ordinal();
                durability[index] = (int)(ArmorModuleBuilder.MAX_DAMAGE_ARRAY[index] * maxDamageFactor);
            }
            if (shieldDurability == 0) {
                shieldDurability = (int)(maxDamageFactor * 18);
            }
            return this;
        }

        public Builder armor(float boots, float leggings, float chestplate, float helmet) {
            armor[ArmorItem.Type.BOOTS.ordinal()] = boots;
            armor[ArmorItem.Type.LEGGINGS.ordinal()] = leggings;
            armor[ArmorItem.Type.CHESTPLATE.ordinal()] = chestplate;
            armor[ArmorItem.Type.HELMET.ordinal()] = helmet;
            return this;
        }

        @Override
        public DragonPlatingArmorMaterialStats build(ArmorItem.Type slot) {
            int index = slot.ordinal();
            return new DragonPlatingArmorMaterialStats(TYPES.get(index), durability[index], armor[index], toughness, knockbackResistance);
        }

        @Override
        public DragonPlatingArmorMaterialStats buildShield() {
            return new DragonPlatingArmorMaterialStats(BANNER, shieldDurability, 0, toughness, knockbackResistance);
        }
    }
}
