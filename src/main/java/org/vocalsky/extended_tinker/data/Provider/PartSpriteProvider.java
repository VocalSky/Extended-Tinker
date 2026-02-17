package org.vocalsky.extended_tinker.data.Provider;

import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
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
        addSprite("parts/dragonarmor_head", IafCore.Materials.dragon_armor);
        addSprite("parts/dragonarmor_body", IafCore.Materials.dragon_armor);
        addSprite("parts/dragonarmor_neck", IafCore.Materials.dragon_armor);
        addSprite("parts/dragonarmor_tail", IafCore.Materials.dragon_armor);
    }
}
