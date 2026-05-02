package org.vocalsky.extended_tinker.mixin.iaf;

import com.github.alexthe666.iceandfire.client.model.ModelHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.tool.HorseArmorItem;
import org.vocalsky.extended_tinker.util.TextureInformation;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(targets = "com.github.alexthe666.iceandfire.client.render.entity.RenderHippocampus$LayerHippocampusSaddle", remap = false)
public abstract class RenderRenderHippocampusMixin extends RenderLayer<EntityHippocampus, ModelHippocampus> {
    @Unique
    private final static RenderType eT$type = RenderType.entityCutout(Extended_tinker.getResource("textures/entity/hippocampus/armor.png"));

    public RenderRenderHippocampusMixin(RenderLayerParent<EntityHippocampus, ModelHippocampus> p_117346_) {
        super(p_117346_);
    }

    @Inject(method = "render*", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/EntityHippocampus;getArmor()I"), cancellable = true, remap = false)
    public void renderMixin(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityHippocampus hippo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo cir) {
        if (hippo.getArmor() == 4) {
            ItemStack armorStack = hippo.getItemBySlot(EquipmentSlot.CHEST);
            if (!(armorStack.getItem() instanceof HorseArmorItem)) {
                cir.cancel();
                return;
            }
            VertexConsumer vertex = bufferIn.getBuffer(eT$type);
            ToolStack tool = ToolStack.from(armorStack);
            TextureInformation info = TextureInformation.getTexture(tool, 0);
            int color = info.color();
            this.getParentModel().renderToBuffer(matrixStackIn, vertex, packedLightIn, OverlayTexture.NO_OVERLAY, (float) ((color >>> 16 & 255) / 255.0), (float) ((color >>> 8 & 255) / 255.0), (float) ((color & 255) / 255.0), (float) ((color >>> 24 & 255) / 255.0));
            cir.cancel();
        }
    }
}
