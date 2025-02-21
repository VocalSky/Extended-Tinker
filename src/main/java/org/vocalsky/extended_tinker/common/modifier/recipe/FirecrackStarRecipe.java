package org.vocalsky.extended_tinker.common.modifier.recipe;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.Items;
import org.vocalsky.extended_tinker.common.Modifiers;
import org.vocalsky.extended_tinker.common.modifier.FireworkRocket.FirecrackStarModifier;
import slimeknights.mantle.data.loadable.common.IngredientLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.vocalsky.extended_tinker.common.tool.Firecrack.TAG_EXPLOSION;
import static slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe.withModifiers;

public class FirecrackStarRecipe implements ITinkerStationRecipe, IDisplayModifierRecipe {
    private static final Ingredient STAR_INGREDIENT = Ingredient.of(net.minecraft.world.item.Items.FIREWORK_STAR);
    public static final RecordLoadable<FirecrackStarRecipe> LOADER = RecordLoadable.create(
            ContextKey.ID.requiredField(),
            IngredientLoadable.DISALLOW_EMPTY.requiredField("ingredient", r -> r.ingredient),
            FirecrackStarRecipe::new);

    @Getter
    private final ResourceLocation id;
    private final Ingredient ingredient;

    public FirecrackStarRecipe(ResourceLocation id, Ingredient ingredient) {
        this.id = id;
        this.ingredient = ingredient;
        ModifierRecipeLookup.addRecipeModifier(null, Modifiers.firecrackStar);
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, @NotNull Level world) {
        if (!inv.getTinkerableStack().is(Items.FIRECRACK.asItem())) {
            return false;
        }
        return IncrementalModifierRecipe.containsOnlyIngredient(inv, ingredient);
    }

    @Override
    public @NotNull RecipeResult<ItemStack> getValidatedResult(ITinkerStationContainer inv) {
        ToolStack tool = inv.getTinkerable();
        FirecrackStarModifier firecrackStar = Modifiers.firecrackStar.get();
        ModifierId firecrackStarId = Modifiers.firecrackStar.getId();
        ModifierEntry entry = tool.getModifier(firecrackStarId);
        if (tool.getUpgrades().getLevel(firecrackStarId) == 0) {
            tool = tool.copy();
            tool.addModifier(firecrackStarId, 1);
        } else {
            tool = tool.copy();
        }
        ListTag listTag = new ListTag();
        for (int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack stack = inv.getInput(i);
            if (!stack.isEmpty() && STAR_INGREDIENT.test(stack)) {
                CompoundTag tag = stack.getTagElement(TAG_EXPLOSION);
                if (tag != null) listTag.add(tag);
            }
        }
        firecrackStar.addStar(tool, entry, listTag);
        return RecipeResult.success(tool.createStack(Math.min(inv.getTinkerableSize(), shrinkToolSlotBy())));
    }

    /** @deprecated use {@link #assemble(ITinkerStationContainer)} */
    @Deprecated
    @Override
    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Modifiers.firecrackStarSerializer.get();
    }

    /* JEI display */
    /** Cache of modifier result, same for all overslime */
    private static final ModifierEntry RESULT = new ModifierEntry(Modifiers.firecrackStar, 1);
    /** Cache of input and output tools for display */
    private List<ItemStack> toolWithoutModifier, toolWithModifier = null;

    @Override
    public int getInputCount() {
        return 1;
    }

    @Override
    public List<ItemStack> getDisplayItems(int slot) {
        if (slot == 0) {
            return Arrays.asList(ingredient.getItems());
        }
        return Collections.emptyList();
    }
    @Override
    public List<ItemStack> getToolWithoutModifier() {
        if (toolWithoutModifier == null)
            toolWithoutModifier = Arrays.stream((new ItemStack[]{Items.FIRECRACK.get().getRenderTool()})).toList();
        return toolWithoutModifier;
    }

    @Override
    public List<ItemStack> getToolWithModifier() {
        if (toolWithModifier == null) {
            FirecrackStarModifier firecrackStar = Modifiers.firecrackStar.get();
            List<ModifierEntry> result = List.of(RESULT);
            toolWithModifier = toolWithoutModifier.stream().map(stack -> withModifiers(stack, result, data -> firecrackStar.setStar(data))).toList();
        }
        return toolWithModifier;
    }

    @Override
    public ModifierEntry getDisplayResult() {
        return RESULT;
    }
}
