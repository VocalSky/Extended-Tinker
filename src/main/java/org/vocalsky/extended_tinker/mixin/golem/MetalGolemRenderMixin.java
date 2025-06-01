package org.vocalsky.extended_tinker.mixin.golem;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemRenderer;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.golem.client.GolemArmorRender;

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
}
