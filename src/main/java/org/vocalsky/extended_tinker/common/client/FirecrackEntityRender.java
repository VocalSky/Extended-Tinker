package org.vocalsky.extended_tinker.common.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.inventory.InventoryMenu;
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
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        if (entity.isShotAtAngle()) {
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        }

        this.itemRenderer.renderStatic(entity.getItem(), ItemTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, buffIn, entity.getId());
        matrixStackIn.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, buffIn, packedLightIn);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull FirecrackEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
