package org.vocalsky.extended_tinker.golem.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPaths;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import dev.xkmc.modulargolems.content.item.equipments.GolemModelItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.golem.tool.GolemArmor;
import org.vocalsky.extended_tinker.util.TextureInformation;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels.LIST;

public class GolemArmorRender extends RenderLayer<MetalGolemEntity, MetalGolemModel>  {
    public HashMap<ModelLayerLocation, MetalGolemModel> map = new HashMap<>();

    public GolemArmorRender(RenderLayerParent<MetalGolemEntity, MetalGolemModel> r, EntityRendererProvider.Context e) {
        super(r);
        for (var l : LIST) {
            map.put(l, new MetalGolemModel(e.bakeLayer(l)));
        }
    }

    private static final ResourceLocation[] textureLocations = new ResourceLocation[]{Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate.png")};

    @Unique
    public TextureInformation getTextureLocation(ToolStack tool, int partIndex) {
//        if (partIndex >= 0 && partIndex < extended_tinker$textureLocations.length) {
//            MaterialVariant material = extended_tinker$getTool(horse).getMaterial(partIndex);
//            Optional<MaterialRenderInfo> optional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getVariant());
//            if (optional.isPresent()) {
//                MaterialRenderInfo info = optional.get();
//                return new TextureInformation(extended_tinker$textureLocations[partIndex], info.vertexColor());
//            }
//        }
//        return new TextureInformation(this.getTextureLocation(horse), -1);
        MaterialVariant material = tool.getMaterial(partIndex);
        Optional<MaterialRenderInfo> optional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getVariant());
        if (optional.isPresent()) {
            MaterialRenderInfo info = optional.get();
            return new TextureInformation(Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate.png"), info.vertexColor());
        }
        return new TextureInformation(null, -1);
    }

    @Override
    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource source, int i, @NotNull MetalGolemEntity entity, float f1, float f2, float f3, float f4, float f5, float f6) {
        for (var e : EquipmentSlot.values()) {
            ItemStack stack = ((Mob)entity).getItemBySlot(e);
            if (stack.getItem() instanceof GolemArmor armor) {
//                this.getParentModel().copyPropertiesTo(this.model);
//                this.model.prepareMobModel(horse, p_117036_, p_117037_, p_117038_);
//                this.model.setupAnim(horse, p_117036_, p_117037_, p_117039_, p_117040_, p_117041_);
//                ToolStack toolStack = extended_tinker$getTool(horse);
//                for (int partIndex = 0; partIndex < toolStack.getMaterials().size(); ++partIndex)
//                    extended_tinker$renderPart(poseStack, multiBufferSource, packedLightIn, horse, partIndex);
//
                ResourceLocation modelType = null;
                ResourceLocation modelTexture = null;
                if (armor.getType() == ArmorItem.Type.HELMET) {
                    modelType = GolemModelPaths.HELMETS;
                    modelTexture = Extended_tinker.getResource("textures/tinker_armor/golem_armor/helmet.png");
                }
                if (armor.getType() == ArmorItem.Type.CHESTPLATE) {
                    modelType = GolemModelPaths.CHESTPLATES;
                    modelTexture = Extended_tinker.getResource("textures/tinker_armor/golem_armor/chestplate.png");
                }
                if (armor.getType() == ArmorItem.Type.LEGGINGS) {
                    modelType = GolemModelPaths.LEGGINGS;
                    modelTexture = Extended_tinker.getResource("textures/tinker_armor/golem_armor/shinguard.png");
                }
                System.out.println("DASDAS");
                System.out.println(modelType);
                System.out.println(modelTexture);
                if (modelType == null) continue;
                GolemModelPath path = GolemModelPath.get(modelType);
                for (List<String> ls : path.paths()) {
                    MetalGolemModel model = map.get(path.models());
                    model.copyFrom(getParentModel());
                    ModelPart part = model.root();
                    pose.pushPose();
                    for (String s : ls) {
                        part.translateAndRotate(pose);
                        part = part.getChild(s);
                    }
                    part.render(pose, source.getBuffer(RenderType.armorCutoutNoCull(modelTexture)),
                            i, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                    pose.popPose();
                }
            }
        }
    }
}
