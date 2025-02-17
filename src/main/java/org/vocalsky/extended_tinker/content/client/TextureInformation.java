package org.vocalsky.extended_tinker.content.client;

import net.minecraft.resources.ResourceLocation;

public class TextureInformation {
    public ResourceLocation resourceLocation;
    public int color;

    public TextureInformation(ResourceLocation resourceLocation, int color) {
        this.resourceLocation = resourceLocation;
        this.color = color;
    }
}