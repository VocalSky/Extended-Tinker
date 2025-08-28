package org.vocalsky.extended_tinker.util;

import net.minecraft.resources.ResourceLocation;

public interface IArmorModel {
    public int textureSize();
    public ResourceLocation getModelTexture(int partIndex);
}
