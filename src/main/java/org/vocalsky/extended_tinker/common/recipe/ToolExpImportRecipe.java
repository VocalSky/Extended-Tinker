package org.vocalsky.extended_tinker.common.recipe;

import lombok.Getter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.item.ExpTransferOrb;
import pyre.tinkerslevellingaddon.ImprovableModifier;
import pyre.tinkerslevellingaddon.setup.Registration;
import pyre.tinkerslevellingaddon.util.ToolLevellingUtil;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;


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
            if (input.is(ModCore.ExpTransferOrb.asItem())) {
                exp_transfer = input;
            } else if (!input.isEmpty()) {
                return false;
            }
        }
        return !exp_transfer.isEmpty();
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
            if (input.is(ModCore.ExpTransferOrb.asItem())) {
                ToolStack newTool = tool.copy();
                int transferLevel = input.getOrCreateTag().getInt(ImprovableModifier.LEVEL_KEY.toString());
                int transferExperience = input.getOrCreateTag().getInt(ImprovableModifier.EXPERIENCE_KEY.toString());
                boolean transferIsBoard = input.getOrCreateTag().getBoolean(ExpTransferOrb.IS_BOARD_KEY.toString());
                for (int lv = 1; lv <= transferLevel; lv++) {
                    ToolLevellingUtil.addExperience(newTool, ToolLevellingUtil.getXpNeededForLevel(lv, transferIsBoard), null);
                }
                ToolLevellingUtil.addExperience(newTool, transferExperience, null);
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