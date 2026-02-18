package org.vocalsky.extended_tinker.mixin.golem;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import dev.xkmc.modulargolems.content.entity.metalgolem.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.compat.golem.client.GolemArmorRender;
import org.vocalsky.extended_tinker.compat.golem.client.GolemBeaconRender;

import java.util.function.Supplier;

@Mixin(MetalGolemRenderer.class)
public abstract class MetalGolemRenderMixin extends AbstractGolemRenderer<MetalGolemEntity, MetalGolemPartType, MetalGolemModel> {
    public MetalGolemRenderMixin(EntityRendererProvider.Context ctx, MetalGolemModel model, float f, Supplier<MetalGolemPartType[]> list) {
        super(ctx, model, f, list);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void initMixin(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        this.addLayer(new GolemArmorRender(this, ctx));
    }

    @Inject(method = "render(Ldev/xkmc/modulargolems/content/entity/metalgolem/MetalGolemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"), remap = false)
    public void renderMixin(MetalGolemEntity entity, float f1, float f2, PoseStack pose, MultiBufferSource source, int i, CallbackInfo ci) {
        GolemBeaconRender.renderGolemBeacon(entity, pose, source, f2);
    }
}
