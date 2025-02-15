package org.vocalsky.extended_tinker.content.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.client.armor.AbstractArmorModel;
import slimeknights.tconstruct.library.client.armor.ArmorModelManager;
import slimeknights.tconstruct.library.client.armor.MultilayerArmorModel;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;

public class HorseArmorModel extends MultilayerArmorModel {
    public static final HorseArmorModel INSTANCE = new HorseArmorModel();

    protected ItemStack armorStack = ItemStack.EMPTY;
    protected ArmorModelManager.ArmorModel model = ArmorModelManager.ArmorModel.EMPTY;

    protected HorseArmorModel() {
        super();
    }

    /** Prepares this model */
    public Model setup(LivingEntity living, ItemStack stack, HumanoidModel<?> base, ArmorModelManager.ArmorModel model) {
        super.setup(living, stack, EquipmentSlot.CHEST, base, model);
        return this;
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.renderToBuffer(matrices, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}


