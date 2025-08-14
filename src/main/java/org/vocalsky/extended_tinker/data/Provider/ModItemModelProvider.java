package org.vocalsky.extended_tinker.data.Provider;


import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import org.vocalsky.extended_tinker.compat.iaf.IafItems;
import org.vocalsky.extended_tinker.compat.iaf.tool.DragonArmor;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.data.model.MaterialModelBuilder;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.MaterialItem;
import slimeknights.tconstruct.tools.TinkerToolParts;

import static slimeknights.tconstruct.common.TinkerTags.Items.TOOL_PARTS;

public class ModItemModelProvider extends ItemModelProvider {
    private final ModelFile.UncheckedModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");
    public ModItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        part(ModItems.Parts.BRIDLE);
        GolemItems.Parts.GOLEM_PLATING.forEach((slot, item) -> {
            if (slot == ArmorItem.Type.BOOTS) return;
            MaterialModelBuilder<ItemModelBuilder> b = this.part(item, "golem_armor/" + slot.getName() + "/" + slot.getName());
            if (slot == ArmorItem.Type.HELMET) {
                b.offset(0, 2);
            } else if (slot == ArmorItem.Type.LEGGINGS) {
                b.offset(0, 1);
            }
        });

        cast(ModItems.Casts.BRIDLE_CAST);
        GolemItems.Casts.GOLEM_PLATING_CAST.forEach((slot, item) -> {
            cast(item);
        });

        for (ItemDragonArmor.DragonArmorType armorType : ItemDragonArmor.DragonArmorType.values())
            for (DragonArmor.Type type : DragonArmor.Type.values()) {
                String texture = "dragonarmor_" + DragonArmor.fullNameOfArmorType(armorType).toLowerCase() + "_" + type.getName();
                basicItem(IafItems.Tools.DRAGON_ARMOR.get(armorType).get(type).asItem(), texture);
//                generated(id(IafItems.Tools.DRAGON_ARMOR.get(armorType).get(type).asItem()), new ResourceLocation(IceAndFire.MODID, "item/" + texture));
            }
    }

    private ResourceLocation id(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    /** Generated item with a texture */
    private ItemModelBuilder generated(ResourceLocation item, ResourceLocation texture) {
        return getBuilder(item.toString()).parent(GENERATED).texture("layer0", texture);
    }

    /** Generated item with a texture */
    private ItemModelBuilder generated(ResourceLocation item, String texture) {
        return generated(item, Extended_tinker.getResource(texture));
    }

    /** Generated item with a texture */
    private ItemModelBuilder generated(ItemLike item, String texture) {
        return generated(id(item), texture);
    }

    /** Generated item with a texture */
    private ItemModelBuilder basicItem(ResourceLocation item, String texture) {
        return generated(item, "item/" + texture);
    }

    /** Generated item with a texture */
    private ItemModelBuilder basicItem(ItemLike item, String texture) {
        return basicItem(id(item), texture);
    }


    /* Parts */

    /** Creates a part model with the given texture */
    private MaterialModelBuilder<ItemModelBuilder> part(ResourceLocation part, String texture) {
        return withExistingParent(part.getPath(), "forge:item/default")
                .texture("texture", Extended_tinker.getResource("item/tool/" + texture))
                .customLoader(MaterialModelBuilder::new);
    }

    /** Creates a part model in the parts folder */
    private MaterialModelBuilder<ItemModelBuilder> part(Item item, String texture) {
        return part(id(item), texture);
    }

    /** Creates a part model with the given texture */
    private MaterialModelBuilder<ItemModelBuilder> part(ItemObject<? extends MaterialItem> part, String texture) {
        return part(part.getId(), texture);
    }

    /** Creates a part model in the parts folder */
    private void part(ItemObject<? extends MaterialItem> part) {
        part(part, "parts/" + part.getId().getPath());
    }


    /** Creates models for the given cast object */
    private void cast(CastItemObject cast) {
        String name = cast.getName().getPath();
        basicItem(cast.getId(), "cast/" + name);
        basicItem(cast.getSand(), "sand_cast/" + name);
        basicItem(cast.getRedSand(), "red_sand_cast/" + name);
    }
}
