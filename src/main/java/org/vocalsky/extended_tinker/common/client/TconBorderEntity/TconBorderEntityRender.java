package org.vocalsky.extended_tinker.common.client.TconBorderEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.entity.TconBorderEntity;

@OnlyIn(Dist.CLIENT)
public class TconBorderEntityRender extends MobRenderer<TconBorderEntity, TconBorderEntityModel> {
    public TconBorderEntityRender(EntityRendererProvider.Context context) {
        super(context, new TconBorderEntityModel(context.bakeLayer(TconBorderEntityModel.LAYER_LOCATION)), 1f);
        this.shadowRadius = 0.8F;
    }


    public void render(@NotNull TconBorderEntity entity, float p_113930_, float p_113931_, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int p_113934_) {
        super.render(entity, p_113930_, p_113931_, poseStack, multiBufferSource, p_113934_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TconBorderEntity entity) {
        return Extended_tinker.getResource("textures/entity/tcon_border/base.png");
    }
}
