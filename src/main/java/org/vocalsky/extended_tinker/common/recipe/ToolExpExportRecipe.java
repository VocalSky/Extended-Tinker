package org.vocalsky.extended_tinker.common.recipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.ModModifiers;
import pyre.tinkerslevellingaddon.ImprovableModifier;
import pyre.tinkerslevellingaddon.setup.Registration;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.ITinkerableContainer;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.worktable.AbstractWorktableRecipe;
import slimeknights.tconstruct.library.tools.nbt.*;

import java.util.List;

public class ToolExpExportRecipe extends AbstractWorktableRecipe {
    int currentLevel = 0;
    int currentExperience = 0;

    private static final Component TITLE = Extended_tinker.makeTranslation("recipe", "exp_export.title");
    private static final Component DESCRIPTION = Extended_tinker.makeTranslation("recipe", "exp_export.description");
    private static final Component NOT_EXIST_MODIFIERS = Extended_tinker.makeTranslation("recipe", "exp_export.not_exist_modifiers");

    public static final RecordLoadable<ToolExpExportRecipe> LOADER = RecordLoadable.create(ContextKey.ID.requiredField(), INPUTS_FIELD, ToolExpExportRecipe::new);

    private static ItemStack getTarget(ITinkerableContainer inv) {
        ItemStack target = null;
        for (int i = 0; i < inv.getTinkerableSize(); ++i)
            if (inv.getInput(i).is(ModCore.ExpTransfer.asItem())) {
                target = inv.getInput(i);
                break;
            }
        return target;
    }

    private static boolean checkTargetValid(ItemStack target) {
        if (target == null) return false;
        CompoundTag tag = target.getOrCreateTag();
        return tag.getInt(ImprovableModifier.LEVEL_KEY.toString()) == 0 && tag.getInt(ImprovableModifier.EXPERIENCE_KEY.toString()) == 0;
    }

    public ToolExpExportRecipe(ResourceLocation id, List<SizedIngredient> inputs) {
        super(id, inputs);
    }

    @Override
    public boolean matches(ITinkerableContainer inv, @NotNull Level world) {
        if (!inv.getTinkerableStack().is(TinkerTags.Items.MODIFIABLE)) return false;
        if (inv.getTinkerable().getModifierLevel(Registration.IMPROVABLE.getId()) == 0) return false;
        return checkTargetValid(getTarget(inv));
    }

    @Override
    public @NotNull Component getTitle() {
        return TITLE;
    }

    @Override
    public @NotNull Component getDescription(@Nullable ITinkerableContainer inv) {
        if (inv != null && inv.getTinkerable().getModifierLevel(Registration.IMPROVABLE.getId()) == 0) {
            return NOT_EXIST_MODIFIERS;
        }
        return DESCRIPTION;
    }

    @Override
    public @NotNull List<ModifierEntry> getModifierOptions(@Nullable ITinkerableContainer inv) {
        if (inv == null) return ModifierRecipeLookup.getRecipeModifierList();
        if (!checkTargetValid(getTarget(inv))) return inv.getTinkerable().getUpgrades().getModifiers();
        return List.of(inv.getTinkerable().getModifier(Registration.IMPROVABLE.getId()));
    }

    @Override
    public @NotNull RecipeResult<LazyToolStack> getResult(@NotNull ITinkerableContainer inv, @NotNull ModifierEntry modifier) {
        if (!modifier.getId().toString().equals(Registration.IMPROVABLE.getId().toString()))
            throw new IllegalStateException("Modifier must be improvable");
        ToolStack tool = inv.getTinkerable().copy();
        ModDataNBT data = tool.getPersistentData();
        currentLevel = data.getInt(ImprovableModifier.LEVEL_KEY);
        currentExperience = data.getInt(ImprovableModifier.EXPERIENCE_KEY);
        data.putInt(ImprovableModifier.LEVEL_KEY, 0);
        data.putInt(ImprovableModifier.EXPERIENCE_KEY, 0);
        return LazyToolStack.success(tool, inv.getTinkerableSize());
    }

    @Override
    public void updateInputs(@NotNull LazyToolStack result, ITinkerableContainer.@NotNull Mutable inv, @NotNull ModifierEntry selected, boolean isServer) {
        if (isServer) {
            ItemStack target = getTarget(inv);
            if (!checkTargetValid(target)) throw new IllegalStateException("Exp Transfer item not found in inputs or is not valid");
            CompoundTag tag = target.getOrCreateTag();
            tag.putInt(ImprovableModifier.LEVEL_KEY.toString(), currentLevel);
            tag.putInt(ImprovableModifier.EXPERIENCE_KEY.toString(), currentExperience);
        }
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModModifiers.TOOL_EXP_EXPORT_SERIALIZER.get();
    }
}