package org.vocalsky.extended_tinker.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.content.tools.HorseArmor;

import java.util.Objects;

@Mixin(HorseArmorLayer.class)
public abstract class ForHorseArmorRender extends RenderLayer<Horse, HorseModel<Horse>>  {
    @Shadow @Final private HorseModel<Horse> model;

    public ForHorseArmorRender(RenderLayerParent<Horse, HorseModel<Horse>> p_117346_) {
        super(p_117346_);
    }

    private ResourceLocation getTexture(ItemStack stack) {
        if (stack == null || stack.getTag() == null) return Extended_tinker.getResource("textures/tinker_armor/horse_armor/head_armor.png");
        String input = Objects.requireNonNull(stack.getTag().get("tic_materials")).toString();
        if (input.length() <= 2) return Extended_tinker.getResource("textures/tinker_armor/horse_armor/head_armor.png");
        String trimmed = input.substring(1, input.length() - 1);
        String[] parts = trimmed.split(",");
        String lastElement = parts[parts.length - 1].replace("\"", "");
        return Extended_tinker.getResource(String.format("textures/tinker_armor/horse_armor/head_armor_%s.png", lastElement.replace(":", "_")));
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int p_117034_, Horse horse, float p_117036_, float p_117037_, float p_117038_, float p_117039_, float p_117040_, float p_117041_, CallbackInfo ci) {
        ItemStack armor = horse.getArmor();
        if (armor.getItem() instanceof HorseArmor) {
            ((HorseModel)this.getParentModel()).copyPropertiesTo(this.model);
            this.model.prepareMobModel(horse, p_117036_, p_117037_, p_117038_);
            this.model.setupAnim(horse, p_117036_, p_117037_, p_117039_, p_117040_, p_117041_);
            float $$13 = 1.0F;
            float $$14 = 1.0F;
            float $$15 = 1.0F;
            VertexConsumer $$19 = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(getTexture(armor)));
            this.model.renderToBuffer(poseStack, $$19, p_117034_, OverlayTexture.NO_OVERLAY, $$13, $$14, $$15, 1.0F);
            ci.cancel();
        }
    }
}