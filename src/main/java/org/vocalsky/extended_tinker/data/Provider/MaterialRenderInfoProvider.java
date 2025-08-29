package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class MaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public MaterialRenderInfoProvider(PackOutput packOutput, @Nullable AbstractMaterialSpriteProvider materialSprites, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, materialSprites, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        buildRenderInfo(IafMaterials.copper).color(0xE77C56).fallbacks("metal");
        buildRenderInfo(IafMaterials.iron).color(0xD8D8D8).fallbacks("metal");
        buildRenderInfo(IafMaterials.silver).color(0xDAF3ED).fallbacks("metal");
        buildRenderInfo(IafMaterials.gold).color(0xFDF55F).fallbacks("metal");
        buildRenderInfo(IafMaterials.diamond).color(0x55FFFF).fallbacks("metal");
        buildRenderInfo(IafMaterials.fire).color(0x555555).fallbacks("metal");
        buildRenderInfo(IafMaterials.ice).color(0xD3DFE8).fallbacks("metal");
        buildRenderInfo(IafMaterials.lightning).color(0x17365D).fallbacks("metal");
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Material Render Info Provider";
    }
}
