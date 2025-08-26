package org.vocalsky.extended_tinker.data.Provider;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import org.vocalsky.extended_tinker.compat.iaf.IafItems;
import org.vocalsky.extended_tinker.compat.iaf.IafMaterials;
import org.vocalsky.extended_tinker.compat.iaf.tool.DragonArmor;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.helper.TypeAwareRecipeSerializer;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.function.Consumer;

public class ToolRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
    public ToolRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Tool Recipe Provider";
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        this.addToolBuildingRecipes(consumer);
        this.addToolPartRecipes(consumer);
    }

    private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/building/";
        String armorFolder = "tools/armor/";

        // common
        ToolBuildingRecipeBuilder.toolBuildingRecipe(ModItems.Tools.FIRECRACK.get()).addExtraRequirement(Ingredient.of(Items.GUNPOWDER)).addExtraRequirement(Ingredient.of(Items.PAPER)).save(consumer, this.prefix(this.id(ModItems.Tools.FIRECRACK.get()), folder));
        toolBuilding(consumer, ModItems.Tools.HORSE_ARMOR, armorFolder);

        // golems
        GolemItems.Tools.GOLEM_ARMOR.forEach(item -> toolBuilding(consumer, item, armorFolder, Extended_tinker.getResource("golem_armor")));

        // iaf
        for (ItemDragonArmor.DragonArmorType armorType : ItemDragonArmor.DragonArmorType.values())
            for (DragonArmor.Type type : DragonArmor.Type.values()) {
                MaterialCastingRecipeBuilder.tableRecipe(IafItems.Tools.DRAGON_ARMOR.get(armorType).get(type)).
                setCast(MaterialIngredient.of(IafItems.Parts.DRAGON_ARMOR_CORE.get(armorType).get(type)), true).
                setItemCost(9).
                save(consumer, this.location(armorFolder + "dragonarmor_" + armorType.name().toLowerCase() + "_" + type.getName()));
            }
    }

    private <T, U> T getReflection(String name, Class<U> obj) {
        Field field;
        try {
            field = obj.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        Object target;
        try {
            target = field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) target;
    }

    private void addToolPartRecipes(Consumer<FinishedRecipe> consumer) {
        String partFolder = "tools/parts/";
        String castFolder = "smeltery/casts/";

        partRecipes(consumer, ModItems.Parts.BRIDLE, ModItems.Casts.BRIDLE_CAST, 4, partFolder, castFolder);

        int[] golem_plating_cost = new int[]{3, 6, 5};
        GolemItems.Casts.GOLEM_PLATING_CAST.forEach((slot, item) -> {
            if (slot == ArmorItem.Type.BOOTS) return;
            partWithDummy(consumer, GolemItems.Parts.GOLEM_PLATING.get(slot), GolemItems.Parts.DUMMY_GOLEM_PLATING.get(slot), GolemItems.Casts.GOLEM_PLATING_CAST.get(slot), golem_plating_cost[slot.ordinal()] * 8, partFolder, castFolder);
        });

//        for (ItemDragonArmor.DragonArmorType armorType : ItemDragonArmor.DragonArmorType.values())
//            for (DragonArmor.Type type : DragonArmor.Type.values()) {
//                RegistryObject<ItemDragonArmor> reg = getReflection("DRAGONARMOR_" + DragonArmor.fullNameOfArmorType(armorType) + "_" + type.getOrder(), IafItemRegistry.class);
//                ;
//            }
    }

    @Override
    public @NotNull String getModId() {
        return Extended_tinker.MODID;
    }
}
