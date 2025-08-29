package org.vocalsky.extended_tinker.common.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.entity.TconBoatEntity;

public class TconBoatEntityRender extends EntityRenderer<TconBoatEntity> {
    protected TconBoatEntityRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TconBoatEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
