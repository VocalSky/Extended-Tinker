package org.vocalsky.extended_tinker.common.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.entity.FirecrackEntity;

public class FirecrackEntityRender extends EntityRenderer<FirecrackEntity> {
    private final ItemRenderer itemRenderer;

    public FirecrackEntityRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(FirecrackEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource buffIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        if (entity.isShotAtAngle()) {
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        }

        this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, buffIn, entity.level(), entity.getId());
        matrixStackIn.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, buffIn, packedLightIn);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull FirecrackEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
