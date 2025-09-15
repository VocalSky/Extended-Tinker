package org.vocalsky.extended_tinker.common.client.TconBorderEntity;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.entity.TconBorderEntity;

public class TconBorderEntityModel extends EntityModel<TconBorderEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Extended_tinker.MODID, "tcon_border_model"), "main");
	private final ModelPart body;
	private final ModelPart wheel1;
	private final ModelPart bone1;
	private final ModelPart wheel2;
	private final ModelPart bone2;
	private final ModelPart base;

	public TconBorderEntityModel(ModelPart root) {
		this.body = root.getChild("body");
		this.wheel1 = root.getChild("wheel1");
		this.bone1 = this.wheel1.getChild("bone1");
		this.wheel2 = root.getChild("wheel2");
		this.bone2 = this.wheel2.getChild("bone2");
		this.base = root.getChild("base");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -16.0F, -24.0F, 48.0F, 16.0F, 48.0F, new CubeDeformation(0.0F))
		.texOffs(0, 64).addBox(-16.0F, -24.0F, -16.0F, 40.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

		PartDefinition wheel1 = partdefinition.addOrReplaceChild("wheel1", CubeListBuilder.create().texOffs(144, 75).addBox(-20.0F, 2.0F, -21.0F, 44.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 140).addBox(-24.0F, -1.0F, -21.0F, 48.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition bone1 = wheel1.addOrReplaceChild("bone1", CubeListBuilder.create().texOffs(148, 95).addBox(-20.0F, -1.0F, -25.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(148, 107).addBox(-11.0F, -1.0F, -25.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(148, 119).addBox(-2.0F, -1.0F, -25.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 150).addBox(7.0F, -1.0F, -25.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 150).addBox(16.0F, -1.0F, -25.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wheel2 = partdefinition.addOrReplaceChild("wheel2", CubeListBuilder.create().texOffs(144, 85).addBox(-20.0F, 2.0F, -21.0F, 44.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(108, 140).addBox(-24.0F, -1.0F, -21.0F, 48.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 36.0F));

		PartDefinition bone2 = wheel2.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(48, 150).addBox(-20.0F, -1.0F, 21.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(72, 150).addBox(-11.0F, -1.0F, 21.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(96, 150).addBox(-2.0F, -1.0F, 21.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(120, 150).addBox(7.0F, -1.0F, 21.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(144, 150).addBox(16.0F, -1.0F, 21.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -36.0F));

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 104).addBox(-20.0F, -9.0F, -15.0F, 44.0F, 6.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(144, 64).addBox(-24.0F, -10.0F, -2.0F, 48.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(TconBorderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}