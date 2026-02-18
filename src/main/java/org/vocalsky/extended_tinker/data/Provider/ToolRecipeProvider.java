package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.compat.golem.GolemCore;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
import org.vocalsky.extended_tinker.compat.iaf.tool.DragonArmorItem;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;

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
        ToolBuildingRecipeBuilder.toolBuildingRecipe(ModCore.Tools.FIRECRACK.get()).addExtraRequirement(Ingredient.of(Items.GUNPOWDER)).addExtraRequirement(Ingredient.of(Items.PAPER)).save(consumer, this.prefix(this.id(ModCore.Tools.FIRECRACK.get()), folder));
        ToolBuildingRecipeBuilder.toolBuildingRecipe(ModCore.Tools.FIREWORK_ROCKET.get()).addExtraRequirement(Ingredient.of(Items.PAPER)).save(consumer, this.prefix(this.id(ModCore.Tools.FIREWORK_ROCKET.get()), folder));
        toolBuilding(consumer, ModCore.Tools.HORSE_ARMOR, armorFolder);

        // golems
        GolemCore.Tools.GOLEM_ARMOR.forEach(item -> toolBuilding(consumer, item, armorFolder, Extended_tinker.getResource("golem_armor")));

        // iaf
        for (DragonArmorItem.Type type : DragonArmorItem.Type.values()) {
            MaterialCastingRecipeBuilder.tableRecipe(IafCore.Tools.DRAGON_ARMOR.get(type)).
            setCast(MaterialIngredient.of(IafCore.Parts.DRAGON_ARMOR_CORE.get(type)), true).
            setItemCost(9).
            save(consumer, this.location(armorFolder + "dragonarmor_" + type.getName()));
        }
    }

    private void addToolPartRecipes(Consumer<FinishedRecipe> consumer) {
        String partFolder = "tools/parts/";
        String castFolder = "smeltery/casts/";

        partRecipes(consumer, ModCore.Parts.BRIDLE, ModCore.Casts.BRIDLE_CAST, 4, partFolder, castFolder);

        int[] golem_plating_cost = new int[]{3, 6, 5, 2};
        GolemCore.Casts.GOLEM_PLATING_CAST.forEach((slot, item) -> partWithDummy(consumer, GolemCore.Parts.GOLEM_PLATING.get(slot), GolemCore.Parts.DUMMY_GOLEM_PLATING.get(slot), GolemCore.Casts.GOLEM_PLATING_CAST.get(slot), golem_plating_cost[slot.ordinal()] * 8, partFolder, castFolder));
    }

    @Override
    public @NotNull String getModId() {
        return Extended_tinker.MODID;
    }
}
