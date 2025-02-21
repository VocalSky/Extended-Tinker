package org.vocalsky.extended_tinker.common.modifier.recipe;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import slimeknights.mantle.recipe.data.AbstractRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.OverslimeModifierRecipe;

import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "modifier")
public class FirecrackStarRecipeBuilder extends AbstractRecipeBuilder<FirecrackStarRecipeBuilder> {
    private final Ingredient ingredient;

    /** Creates a new builder for the given item */
    public static FirecrackStarRecipeBuilder modifier(ItemLike item) {
        return modifier(Ingredient.of(item));
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer) {
        ItemStack[] stacks = ingredient.getItems();
        if (stacks.length == 0) {
            throw new IllegalStateException("Empty ingredient not allowed");
        }
        save(consumer, Registry.ITEM.getKey(stacks[0].getItem()));
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        if (ingredient == Ingredient.EMPTY) {
            throw new IllegalStateException("Empty ingredient not allowed");
        }
        ResourceLocation advancementId = buildOptionalAdvancement(id, "modifiers");
        consumer.accept(new LoadableFinishedRecipe<>(new FirecrackStarRecipe(id, ingredient), FirecrackStarRecipe.LOADER, advancementId));
    }
}

