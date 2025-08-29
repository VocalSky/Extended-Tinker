package org.vocalsky.extended_tinker.data.Provider;

import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;

public class PartSpriteProvider extends AbstractPartSpriteProvider {
    public PartSpriteProvider() {
        super(Extended_tinker.MODID);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Part Sprite Provider";
    }

    @Override
    protected void addAllSpites() {
        addSprite("parts/dragonarmor_head", IafMaterials.dragon_armor);
        addSprite("parts/dragonarmor_body", IafMaterials.dragon_armor);
        addSprite("parts/dragonarmor_neck", IafMaterials.dragon_armor);
        addSprite("parts/dragonarmor_tail", IafMaterials.dragon_armor);
    }
}
