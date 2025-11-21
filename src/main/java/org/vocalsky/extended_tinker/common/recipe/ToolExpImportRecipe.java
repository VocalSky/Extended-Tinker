package org.vocalsky.extended_tinker.common.recipe;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FirecrackStarModifier;
import pyre.tinkerslevellingaddon.ImprovableModifier;
import pyre.tinkerslevellingaddon.TinkersLevellingAddon;
import pyre.tinkerslevellingaddon.setup.Registration;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.util.RegistryHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import javax.tools.Tool;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe.withModifiers;

@Getter
public class ToolExpImportRecipe implements ITinkerStationRecipe {
    public static final RecordLoadable<ToolExpImportRecipe> LOADER = RecordLoadable.create(
        ContextKey.ID.requiredField(),
        ToolExpImportRecipe::new
    );
    private final ResourceLocation id;

    public ToolExpImportRecipe(ResourceLocation id) {
        this.id = id;
    }

    private boolean checkMeta(ToolStack tool, ITinkerStationContainer inv) {
        ItemStack exp_transfer = ItemStack.EMPTY;
        for(int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.is(ModCore.ExpTransfer.asItem())) {
                exp_transfer = input;
            } else if (!input.isEmpty()) {
                return false;
            }
        }
        if (exp_transfer.isEmpty()) return false;
        int currentLevel = tool.getPersistentData().getInt(ImprovableModifier.LEVEL_KEY);
        int currentExperience = tool.getPersistentData().getInt(ImprovableModifier.EXPERIENCE_KEY);
        int transferLevel = exp_transfer.getOrCreateTag().getInt(ImprovableModifier.LEVEL_KEY.toString());
        int transferExperience = exp_transfer.getOrCreateTag().getInt(ImprovableModifier.EXPERIENCE_KEY.toString());
        return currentLevel < transferLevel || (currentLevel == transferLevel && currentExperience < transferExperience);
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, @NotNull Level level) {
        if (!(inv.getTinkerableStack().getItem() instanceof ModifiableItem)) return false;
        ToolStack tool = inv.getTinkerable();
        return tool.getModifierLevel(Registration.IMPROVABLE.get()) != 0 && this.checkMeta(tool, inv);
    }

    @Override
    public @NotNull RecipeResult<LazyToolStack> getValidatedResult(ITinkerStationContainer inv, @NotNull RegistryAccess registryAccess) {
        ToolStack tool = inv.getTinkerable();
        for(int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.is(ModCore.ExpTransfer.asItem())) {
                ToolStack newTool = tool.copy();
                int transferLevel = input.getOrCreateTag().getInt(ImprovableModifier.LEVEL_KEY.toString());
                int transferExperience = input.getOrCreateTag().getInt(ImprovableModifier.EXPERIENCE_KEY.toString());
                newTool.getPersistentData().putInt(ImprovableModifier.LEVEL_KEY, transferLevel);
                newTool.getPersistentData().putInt(ImprovableModifier.EXPERIENCE_KEY, transferExperience);
                return RecipeResult.success(LazyToolStack.from(newTool.createStack()));
            }
        }
        return RecipeResult.pass();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModModifiers.STAR_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TinkerRecipeTypes.TINKER_STATION.get();
    }
}