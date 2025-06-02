package org.vocalsky.extended_tinker.util;

import net.minecraft.resources.ResourceLocation;
import org.vocalsky.extended_tinker.common.tool.IArmorModel;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;

public class TextureInformation {
    public ResourceLocation resourceLocation;
    public int color;

    public TextureInformation(ResourceLocation resourceLocation, int color) {
        this.resourceLocation = resourceLocation;
        this.color = color;
    }

    public static TextureInformation getTexture(ToolStack tool, int partIndex) {
        MaterialVariant material = tool.getMaterial(partIndex);
        Optional<MaterialRenderInfo> optional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getVariant());
        if (optional.isPresent()) {
            MaterialRenderInfo info = optional.get();
            return new TextureInformation(((IArmorModel)tool.getItem()).getModelTexture(partIndex), info.vertexColor());
        }
        return new TextureInformation(null, -1);
    }
}