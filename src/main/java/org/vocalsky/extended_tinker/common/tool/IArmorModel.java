package org.vocalsky.extended_tinker.common.tool;

import net.minecraft.resources.ResourceLocation;

public interface IArmorModel {
    public int textureSize();
    public ResourceLocation getModelTexture(int partIndex);
}
