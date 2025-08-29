package org.vocalsky.extended_tinker.data.Provider;

import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

public class MaterialSpriteProvider extends AbstractMaterialSpriteProvider {
    @Override
    public @NotNull String getName() {
        return "Extended Tinker Material Sprite Provider";
    }

    @Override
    protected void addAllMaterials() {
//        buildMaterial(IafMaterials.copper)
//            .fallbacks("metal")
//            .statType(IafMaterials.dragon_armor)
//            .colorMapper(
//                GreyToColorMapping.builderFromBlack()
//                .addARGB(63, -9620447)
//                .addARGB(102, -7716567)
//                .addARGB(140, -6533583)
//                .addARGB(178, -4105674)
//                .addARGB(216, -1606570)
//                .addARGB(255, -222846)
//                .build()
//            );
    }
}
