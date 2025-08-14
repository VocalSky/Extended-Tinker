package org.vocalsky.extended_tinker.compat.iaf.tool.stats;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.modules.ArmorModuleBuilder;

import java.util.List;

public record DragonPlatingMaterialStats(MaterialStatType<?> getType, int durability, float armor, float toughness, float knockbackResistance) implements IRepairableMaterialStats {
    private static final LoadableField<Float, DragonPlatingMaterialStats> TOUGHNESS;
    private static final LoadableField<Float, DragonPlatingMaterialStats> KNOCKBACK_RESISTANCE;
    private static final RecordLoadable<DragonPlatingMaterialStats> LOADABLE;
    private static final List<Component> DESCRIPTION;
    private static final List<Component> SHIELD_DESCRIPTION;
    public static final MaterialStatType<DragonPlatingMaterialStats> HEAD;
    public static final MaterialStatType<DragonPlatingMaterialStats> BODY;
    public static final MaterialStatType<DragonPlatingMaterialStats> NECK;
    public static final MaterialStatType<DragonPlatingMaterialStats> TAIL;
    public static final MaterialStatType<DragonPlatingMaterialStats> BANNER;
    public static final List<MaterialStatType<DragonPlatingMaterialStats>> TYPES;

    public @NotNull List<Component> getLocalizedInfo() {
        Component durability = ToolStats.DURABILITY.formatValue((float)this.durability);
        Component toughness = ToolStats.ARMOR_TOUGHNESS.formatValue(this.toughness);
        Component knockbackResistance = ToolStats.KNOCKBACK_RESISTANCE.formatValue(this.knockbackResistance * 10.0F);
        return this.getType == BANNER ? List.of(durability, toughness, knockbackResistance) : List.of(durability, ToolStats.ARMOR.formatValue(this.armor), toughness, knockbackResistance);
    }

    public @NotNull List<Component> getLocalizedDescriptions() {
        return this.getType == BANNER ? SHIELD_DESCRIPTION : DESCRIPTION;
    }

    public void apply(@NotNull ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.update(builder, (float)this.durability * scale);
        ToolStats.ARMOR.update(builder, this.armor * scale);
        ToolStats.ARMOR_TOUGHNESS.update(builder, this.toughness * scale);
        ToolStats.KNOCKBACK_RESISTANCE.update(builder, this.knockbackResistance * scale);
    }

    private static MaterialStatType<DragonPlatingMaterialStats> makeType(String name) {
        return new MaterialStatType<DragonPlatingMaterialStats>(new MaterialStatsId(Extended_tinker.MODID, name), (type) -> new DragonPlatingMaterialStats((MaterialStatType<?>) type, 1, 0.0F, 0.0F, 0.0F), LOADABLE);
    }

    public static DragonPlatingMaterialStats.Builder builder() {
        return new DragonPlatingMaterialStats.Builder();
    }

    static {
        TOUGHNESS = FloatLoadable.FROM_ZERO.defaultField("toughness", 0.0F, DragonPlatingMaterialStats::toughness);
        KNOCKBACK_RESISTANCE = FloatLoadable.FROM_ZERO.defaultField("knockback_resistance", 0.0F, DragonPlatingMaterialStats::knockbackResistance);
        LOADABLE = RecordLoadable.create(MaterialStatType.CONTEXT_KEY.requiredField(), IRepairableMaterialStats.DURABILITY_FIELD, FloatLoadable.FROM_ZERO.defaultField("armor", 0.0F, true, DragonPlatingMaterialStats::armor), TOUGHNESS, KNOCKBACK_RESISTANCE, DragonPlatingMaterialStats::new);
        DESCRIPTION = List.of(ToolStats.DURABILITY.getDescription(), ToolStats.ARMOR.getDescription(), ToolStats.ARMOR_TOUGHNESS.getDescription(), ToolStats.KNOCKBACK_RESISTANCE.getDescription());
        SHIELD_DESCRIPTION = List.of(ToolStats.DURABILITY.getDescription(), ToolStats.ARMOR_TOUGHNESS.getDescription(), ToolStats.KNOCKBACK_RESISTANCE.getDescription());
        HEAD = makeType("dragonplating_head");
        BODY = makeType("dragonplating_body");
        NECK = makeType("dragonplating_neck");
        TAIL = makeType("dragonplating_tail");
        BANNER = new MaterialStatType(new MaterialStatsId(Extended_tinker.MODID, "dragonplating_banner"), (type) -> new DragonPlatingMaterialStats((MaterialStatType<?>) type, 1, 0.0F, 0.0F, 0.0F), RecordLoadable.create(MaterialStatType.CONTEXT_KEY.requiredField(), IRepairableMaterialStats.DURABILITY_FIELD, TOUGHNESS, KNOCKBACK_RESISTANCE, (type, durability, toughness, knockbackResistance) -> new DragonPlatingMaterialStats(type, durability, 0.0F, toughness, knockbackResistance)));
        TYPES = List.of(HEAD, BODY, NECK, TAIL, BANNER);
    }

    public static class Builder implements ArmorModuleBuilder.ArmorShieldModuleBuilder<DragonPlatingMaterialStats> {
        private final int[] durability = new int[4];
        private int shieldDurability = 0;
        private final float[] armor = new float[4];
        private float toughness = 0.0F;
        private float knockbackResistance = 0.0F;

        private Builder() {
        }

        public DragonPlatingMaterialStats.Builder durabilityFactor(float maxDamageFactor) {
            for(ArmorItem.Type slotType : ArmorItem.Type.values()) {
                int index = slotType.ordinal();
                this.durability[index] = (int)((float)ArmorModuleBuilder.MAX_DAMAGE_ARRAY[index] * maxDamageFactor);
            }

            if (this.shieldDurability == 0) {
                this.shieldDurability = (int)(maxDamageFactor * 18.0F);
            }

            return this;
        }

        public DragonPlatingMaterialStats.Builder armor(float boots, float leggings, float chestplate, float helmet) {
            this.armor[ArmorItem.Type.BOOTS.ordinal()] = boots;
            this.armor[ArmorItem.Type.LEGGINGS.ordinal()] = leggings;
            this.armor[ArmorItem.Type.CHESTPLATE.ordinal()] = chestplate;
            this.armor[ArmorItem.Type.HELMET.ordinal()] = helmet;
            return this;
        }

        public DragonPlatingMaterialStats build(ArmorItem.Type slot) {
            int index = slot.ordinal();
            return new DragonPlatingMaterialStats((MaterialStatType) DragonPlatingMaterialStats.TYPES.get(index), this.durability[index], this.armor[index], this.toughness, this.knockbackResistance);
        }

        public DragonPlatingMaterialStats buildShield() {
            return new DragonPlatingMaterialStats(DragonPlatingMaterialStats.BANNER, this.shieldDurability, 0.0F, this.toughness, this.knockbackResistance);
        }

        public DragonPlatingMaterialStats.Builder shieldDurability(int shieldDurability) {
            this.shieldDurability = shieldDurability;
            return this;
        }

        public DragonPlatingMaterialStats.Builder toughness(float toughness) {
            this.toughness = toughness;
            return this;
        }

        public DragonPlatingMaterialStats.Builder knockbackResistance(float knockbackResistance) {
            this.knockbackResistance = knockbackResistance;
            return this;
        }
    }
}
