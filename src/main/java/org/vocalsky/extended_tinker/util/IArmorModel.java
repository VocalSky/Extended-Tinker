package org.vocalsky.extended_tinker.util;

import net.minecraft.resources.ResourceLocation;

public interface IArmorModel {
    int textureSize();
    ResourceLocation getModelTexture(int partIndex);
}
