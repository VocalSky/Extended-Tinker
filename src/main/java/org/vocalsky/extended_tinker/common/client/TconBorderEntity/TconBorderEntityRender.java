package org.vocalsky.extended_tinker.common.client.TconBorderEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.vocalsky.extended_tinker.common.entity.TconBorderEntity;

@OnlyIn(Dist.CLIENT)
public class TconBorderEntityRender extends EntityRenderer<TconBorderEntity> {
    public TconBorderEntityRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.8F;
    }


    public void render(TconBorderEntity entity, float p_113930_, float p_113931_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_113934_) {
        super.render(entity, p_113930_, p_113931_, poseStack, multiBufferSource, p_113934_);
    }

    @Override
    public ResourceLocation getTextureLocation(TconBorderEntity p_114482_) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
