package org.vocalsky.extended_tinker.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.Loadable;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsManager;
import slimeknights.tconstruct.library.materials.traits.MaterialTraitsManager;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MaterialRegistryImpl implements IMaterialRegistry {
    private final MaterialManager materialManager;
    private final MaterialStatsManager materialStatsManager;
    private final MaterialTraitsManager materialTraitsManager;

    public MaterialRegistryImpl(MaterialManager materialManager, MaterialStatsManager materialStatsManager, MaterialTraitsManager materialTraitsManager) {
        this.materialManager = materialManager;
        this.materialStatsManager = materialStatsManager;
        this.materialTraitsManager = materialTraitsManager;
    }

    public @NotNull MaterialId resolve(@NotNull MaterialId id) {
        return this.materialManager.resolveRedirect(id);
    }

    public @NotNull IMaterial getMaterial(@NotNull MaterialId id) {
        return (IMaterial)this.materialManager.getMaterial(id).orElse(IMaterial.UNKNOWN);
    }

    public @NotNull Collection<IMaterial> getVisibleMaterials() {
        return this.materialManager.getVisibleMaterials();
    }

    public @NotNull Collection<IMaterial> getAllMaterials() {
        return this.materialManager.getAllMaterials();
    }

    public boolean isInTag(@NotNull MaterialId id, @NotNull TagKey<IMaterial> tag) {
        return this.materialManager.isIn(id, tag);
    }

    public @NotNull List<IMaterial> getTagValues(@NotNull TagKey<IMaterial> tag) {
        return this.materialManager.getValues(tag);
    }

    public @NotNull Loadable<MaterialStatType<?>> getStatTypeLoader() {
        return this.materialStatsManager.getStatTypes();
    }

    public @NotNull Collection<ResourceLocation> getAllStatTypeIds() {
        return this.materialStatsManager.getAllStatTypeIds();
    }

    @Nullable
    public <T extends IMaterialStats> MaterialStatType<T> getStatType(@NotNull MaterialStatsId statsId) {
        return this.materialStatsManager.getStatType(statsId);
    }

    public <T extends IMaterialStats> @NotNull Optional<T> getMaterialStats(@NotNull MaterialId materialId, @NotNull MaterialStatsId statsId) {
        return this.materialStatsManager.getStats(materialId, statsId);
    }

    public @NotNull Collection<IMaterialStats> getAllStats(@NotNull MaterialId materialId) {
        return this.materialStatsManager.getAllStats(materialId);
    }

    public void registerStatType(@NotNull MaterialStatType<?> type) {
        this.materialStatsManager.registerStatType(type);
    }

    public void registerStatType(@NotNull MaterialStatType<?> type, @Nullable MaterialStatsId fallback) {
        this.registerStatType(type);
        if (fallback != null) {
            this.materialTraitsManager.registerStatTypeFallback(type.getId(), fallback);
        }

    }

    public @NotNull List<ModifierEntry> getDefaultTraits(@NotNull MaterialId materialId) {
        return this.materialTraitsManager.getDefaultTraits(materialId);
    }

    public boolean hasUniqueTraits(@NotNull MaterialId materialId, @NotNull MaterialStatsId statsId) {
        return this.materialTraitsManager.hasUniqueTraits(materialId, statsId);
    }

    public @NotNull List<ModifierEntry> getTraits(@NotNull MaterialId materialId, @NotNull MaterialStatsId statsId) {
        return this.materialTraitsManager.getTraits(materialId, statsId);
    }
}
