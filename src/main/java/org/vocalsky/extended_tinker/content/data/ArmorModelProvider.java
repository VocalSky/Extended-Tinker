package org.vocalsky.extended_tinker.content.data;

import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.content.Definitions;
import slimeknights.tconstruct.library.client.armor.texture.ArmorTextureSupplier;
import slimeknights.tconstruct.library.client.armor.texture.MaterialArmorTextureSupplier;
import slimeknights.tconstruct.library.client.data.AbstractArmorModelProvider;

public class ArmorModelProvider extends AbstractArmorModelProvider {
    public ArmorModelProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void addModels() {
        addModel(Definitions.HORSE_ARMOR_MATERIAL, name -> new ArmorTextureSupplier[] {
                new MaterialArmorTextureSupplier.Material(name, "/maille1_", 0),
                new MaterialArmorTextureSupplier.Material(name, "/shield1_", 1),
                new MaterialArmorTextureSupplier.Material(name, "/maille2_", 2),
                new MaterialArmorTextureSupplier.Material(name, "/shield2_", 3),
                new MaterialArmorTextureSupplier.Material(name, "/head_", 4)
        });
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Armor Model Provider";
    }
}
