package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.library.data.AbstractToolItemModelProvider;

import java.io.IOException;

public class ToolItemModelProvider extends AbstractToolItemModelProvider {
    public ToolItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, existingFileHelper, Extended_tinker.MODID);
    }

    @Override
    protected void addModels() throws IOException {
        armor("golem", GolemItems.Tools.GOLEM_ARMOR, "0", "1", "2", "3", "4");
    }

    protected void armor(@NotNull String name, @NotNull EnumObject<ArmorItem.Type, ? extends Item> armor, String @NotNull ... textures) throws IOException {
        armor.forEach((slot, item) -> {
            System.out.println(slot);
            System.out.println(item);
            try {
                this.transformTool("armor/" + name + "/" + slot.getName() + "_broken", this.readJson(BuiltInRegistries.ITEM.getKey(item)), "", false, "broken", textures);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Tool Model Provider";
    }
}
