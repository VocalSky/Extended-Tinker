package org.vocalsky.extended_tinker.compat.golem.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPaths;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.tool.IArmorModel;
import org.vocalsky.extended_tinker.compat.golem.tool.GolemArmor;
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

    @Override
    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource source, int i, @NotNull MetalGolemEntity entity, float f1, float f2, float f3, float f4, float f5, float f6) {
        for (var e : EquipmentSlot.values()) {
            ItemStack stack = ((Mob)entity).getItemBySlot(e);
            if (stack.getItem() instanceof GolemArmor armor) {
                ResourceLocation modelType = null;
                if (armor.getType() == ArmorItem.Type.HELMET)
                    modelType = GolemModelPaths.HELMETS;
                if (armor.getType() == ArmorItem.Type.CHESTPLATE)
                    modelType = GolemModelPaths.CHESTPLATES;
                if (armor.getType() == ArmorItem.Type.LEGGINGS)
                    modelType = GolemModelPaths.LEGGINGS;
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
                    TextureInformation information = TextureInformation.getTexture(ToolStack.from(stack), 0);
                    int color = information.color;
                    part.render(pose, source.getBuffer(RenderType.armorCutoutNoCull(information.resourceLocation)),
                            i, OverlayTexture.NO_OVERLAY, (float) ((color >>> 16 & 255) / 255.0), (float) ((color >>> 8 & 255) / 255.0), (float) ((color & 255) / 255.0), (float) ((color >>> 24 & 255) / 255.0));
                    pose.popPose();
                }
            }
        }
    }
}
