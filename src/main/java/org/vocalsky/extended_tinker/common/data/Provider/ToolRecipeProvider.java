package org.vocalsky.extended_tinker.common.data.Provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;

import java.util.function.Consumer;

public class ToolRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
    public ToolRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Tool Recipe Provider";
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        this.addToolBuildingRecipes(consumer);
    }

    private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/building/";
        String armorFolder = "tools/armor/";
        ToolBuildingRecipeBuilder.toolBuildingRecipe(ModItems.FIRECRACK.get()).addExtraRequirement(Ingredient.of(Items.GUNPOWDER)).addExtraRequirement(Ingredient.of(Items.PAPER)).save(consumer, this.prefix(this.id(ModItems.FIRECRACK.get()), folder));
        toolBuilding(consumer, ModItems.HORSE_ARMOR, armorFolder);
    }

    @Override
    public String getModId() {
        return Extended_tinker.MODID;
    }
}
