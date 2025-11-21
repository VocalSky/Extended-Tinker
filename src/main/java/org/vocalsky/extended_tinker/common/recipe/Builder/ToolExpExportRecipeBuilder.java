package org.vocalsky.extended_tinker.common.recipe.Builder;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.recipe.ToolExpExportRecipe;
import slimeknights.tconstruct.library.recipe.worktable.AbstractSizedIngredientRecipeBuilder;

import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "export")
public class ToolExpExportRecipeBuilder extends AbstractSizedIngredientRecipeBuilder<ToolExpExportRecipeBuilder>  {
    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer) {
        save(consumer, BuiltInRegistries.ITEM.getKey((inputs.get(0).getMatchingStacks().get(0).getItem())));
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
        if (inputs.isEmpty()) throw new IllegalStateException("Must have at least one ingredient");
        ResourceLocation advancementId = buildOptionalAdvancement(id, "modifiers");
        consumer.accept(new LoadableFinishedRecipe<>(new ToolExpExportRecipe(id, inputs), ToolExpExportRecipe.LOADER, advancementId));
    }
}