package org.vocalsky.extended_tinker.common.data.Provider;

//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.recipes.FinishedRecipe;
//import net.minecraft.data.recipes.RecipeProvider;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.Ingredient;
//import org.jetbrains.annotations.NotNull;
//import org.vocalsky.extended_tinker.Extended_tinker;
//import org.vocalsky.extended_tinker.common.ModItems;
//import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
//import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
//import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
//
//import java.util.function.Consumer;
//
//public class ToolRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
//    public ToolRecipeProvider(DataGenerator generator) {
//        super(generator);
//    }
//
//    @Override
//    public @NotNull String getName() {
//        return "Extended Tinker Tool Recipe Provider";
//    }
//
//    @Override
//    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
//        this.addToolBuildingRecipes(consumer);
//        this.addToolPartRecipes(consumer);
//    }
//
//    private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
//        String folder = "tools/building/";
//        String armorFolder = "tools/armor/";
//
//        ToolBuildingRecipeBuilder.toolBuildingRecipe(ModItems.Tools.FIRECRACK.get()).addExtraRequirement(Ingredient.of(Items.GUNPOWDER)).addExtraRequirement(Ingredient.of(Items.PAPER)).save(consumer, this.prefix(this.id(ModItems.Tools.FIRECRACK.get()), folder));
//        toolBuilding(consumer, ModItems.Tools.HORSE_ARMOR, armorFolder);
//    }
//
//    private void addToolPartRecipes(Consumer<FinishedRecipe> consumer) {
//        String partFolder = "tools/parts/";
//        String castFolder = "smeltery/casts/";
//
//        partRecipes(consumer, ModItems.Parts.BRIDLE, ModItems.Casts.BRIDLE_CAST, 4, partFolder, castFolder);
//    }
//
//    @Override
//    public @NotNull String getModId() {
//        return Extended_tinker.MODID;
//    }
//}
