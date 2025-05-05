package org.vocalsky.extended_tinker.mixin.common;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.util.TextureInformation;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;

@Mixin(HorseArmorLayer.class)
public abstract class HorseArmorLayerMixin extends RenderLayer<Horse, HorseModel<Horse>>  {
    @Shadow @Final private HorseModel<Horse> model;

    @Unique
    private static final ResourceLocation[] extended_tinker$textureLocations = new ResourceLocation[]{Extended_tinker.getResource("textures/tinker_armor/horse_armor/maille1_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/shield1_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/maille2_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/shield2_armor.png"), Extended_tinker.getResource("textures/tinker_armor/horse_armor/head_armor.png")};

    public HorseArmorLayerMixin(RenderLayerParent<Horse, HorseModel<Horse>> p_117346_) {
        super(p_117346_);
    }

    @Unique
    private ToolStack extended_tinker$getTool(Horse horse) {
        return ToolStack.from(horse.getArmor());
    }

    @Unique
    public TextureInformation extended_tinker$getTextureLocation(Horse horse, int partIndex) {
        if (partIndex >= 0 && partIndex < extended_tinker$textureLocations.length) {
            MaterialVariant material = extended_tinker$getTool(horse).getMaterial(partIndex);
            Optional<MaterialRenderInfo> optional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getVariant());
            if (optional.isPresent()) {
                MaterialRenderInfo info = optional.get();
                return new TextureInformation(extended_tinker$textureLocations[partIndex], info.vertexColor());
            }
        }
        return new TextureInformation(this.getTextureLocation(horse), -1);
    }


    @Unique
    private void extended_tinker$performRendering(VertexConsumer vertexConsumer, PoseStack poseStack, int packedLightIn, int color) {
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, (float) ((color >>> 16 & 255) / 255.0), (float) ((color >>> 8 & 255) / 255.0), (float) ((color & 255) / 255.0), (float) ((color >>> 24 & 255) / 255.0));
    }

    @Unique
    private void extended_tinker$renderPart(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, Horse horse, int partIndex) {
        TextureInformation information = this.extended_tinker$getTextureLocation(horse, partIndex);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(information.resourceLocation));
        extended_tinker$performRendering(vertexConsumer, poseStack, packedLightIn, information.color);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, Horse horse, float p_117036_, float p_117037_, float p_117038_, float p_117039_, float p_117040_, float p_117041_, CallbackInfo ci) {
        ItemStack armor = horse.getArmor();
        if (armor.getItem() instanceof HorseArmor) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(horse, p_117036_, p_117037_, p_117038_);
            this.model.setupAnim(horse, p_117036_, p_117037_, p_117039_, p_117040_, p_117041_);
            ToolStack toolStack = extended_tinker$getTool(horse);
            for (int partIndex = 0; partIndex < toolStack.getMaterials().size(); ++partIndex)
                extended_tinker$renderPart(poseStack, multiBufferSource, packedLightIn, horse, partIndex);
            ci.cancel();
        }
    }
}