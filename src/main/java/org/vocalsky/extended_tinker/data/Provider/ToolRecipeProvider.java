package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;

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
    }

    private void addToolPartRecipes(Consumer<FinishedRecipe> consumer) {
        String partFolder = "tools/parts/";
        String castFolder = "smeltery/casts/";

        partRecipes(consumer, ModItems.Parts.BRIDLE, ModItems.Casts.BRIDLE_CAST, 4, partFolder, castFolder);

        int golem_plating_cost[] = new int[]{3, 6, 5};
        GolemItems.Casts.GOLEM_PLATING_CAST.forEach((slot, item) -> {
            if (slot == ArmorItem.Type.BOOTS) return;
            partWithDummy(consumer, GolemItems.Parts.GOLEM_PLATING.get(slot), GolemItems.Parts.DUMMY_GOLEM_PLATING.get(slot), GolemItems.Casts.GOLEM_PLATING_CAST.get(slot), golem_plating_cost[slot.ordinal()] * 8, partFolder, castFolder);
        });
//        partWithDummy(consumer, GolemItems.Parts.GOLEM_PLATING.get(ArmorItem.Type.CHESTPLATE), GolemItems.Parts.DUMMY_GOLEM_PLATING.get(ArmorItem.Type.CHESTPLATE), GolemItems.Casts.GOLEM_PLATING_CAST.get(ArmorItem.Type.CHESTPLATE), 6, partFolder, castFolder);
//        partWithDummy(consumer, GolemItems.Parts.GOLEM_PLATING.get(ArmorItem.Type.LEGGINGS),   GolemItems.Parts.DUMMY_GOLEM_PLATING.get(ArmorItem.Type.LEGGINGS),   GolemItems.Casts.GOLEM_PLATING_CAST.get(ArmorItem.Type.LEGGINGS),   5, partFolder, castFolder);
//        partRecipes(consumer, GolemItems.Parts.GOLEM_PLATING.get(ArmorItem.Type.HELMET), GolemItems.Casts.HELMET_GOLEM_PLATING_CAST, 27, partFolder, castFolder);
//        partRecipes(consumer, GolemItems.Parts.GOLEM_PLATING.get(ArmorItem.Type.CHESTPLATE), GolemItems.Casts.CHESTPLATE_GOLEM_PLATING_CAST, 27, partFolder, castFolder);
//        partRecipes(consumer, GolemItems.Parts.GOLEM_PLATING.get(ArmorItem.Type.LEGGINGS), GolemItems.Casts.LEGGINGS_GOLEM_PLATING_CAST, 27, partFolder, castFolder);
    }

    @Override
    public @NotNull String getModId() {
        return Extended_tinker.MODID;
    }
}
