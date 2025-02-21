package org.vocalsky.extended_tinker.common.modifier.FireworkRocket;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.Modifiers;
import org.vocalsky.extended_tinker.common.tool.FireworkRocket;
import slimeknights.mantle.data.loadable.common.IngredientLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.IMultiRecipe;
import slimeknights.mantle.util.RegistryHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.json.IntRange;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.vocalsky.extended_tinker.common.tool.FireworkRocket.TAG_EXPLOSION;
import static org.vocalsky.extended_tinker.common.tool.FireworkRocket.TAG_EXPLOSIONS;

public class FireworkRocketStarRecipe implements ITinkerStationRecipe, IMultiRecipe<IDisplayModifierRecipe> {
    private static final Ingredient STAR_INGREDIENT = Ingredient.of(new ItemLike[]{Items.FIREWORK_STAR});

    public static final RecordLoadable<FireworkRocketStarRecipe> LOADER = RecordLoadable.create(ContextKey.ID.requiredField(), IngredientLoadable.DISALLOW_EMPTY.requiredField("tools", r -> r.toolRequirement), FireworkRocketStarRecipe::new);

    @Getter
    private final ResourceLocation id;
    private final Ingredient toolRequirement;

    public FireworkRocketStarRecipe(ResourceLocation id, Ingredient toolRequirement) {
        this.id = id;
        this.toolRequirement = toolRequirement;
        ModifierRecipeLookup.addRecipeModifier(SlotType.ABILITY, Modifiers.fireworkRocketStar);
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, @NotNull Level world) {
        if (!this.toolRequirement.test(inv.getTinkerableStack()))
            return false;
        if (!inv.getTinkerableStack().is(org.vocalsky.extended_tinker.common.Items.FIREWORK_ROCKET.get()))
            return false;
        boolean found = false;
        for (int i = 0; i < inv.getInputCount(); i++) {
            ItemStack input = inv.getInput(i);
            if (!input.isEmpty()) {
                if (STAR_INGREDIENT.test(input)) return false;
                if (found) return false;
                found = true;
            }
        }
        return found;
    }

    @Override
    public @NotNull ItemStack assemble(ITinkerStationContainer inv) {
        ToolStack tool = inv.getTinkerable().copy();
        ModDataNBT persistentData = tool.getPersistentData();
        ResourceLocation key = Modifiers.fireworkRocketStar.getId();
        ListTag list = new ListTag();
        for (int i = 0; i < inv.getInputCount(); i++) {
            ItemStack stack = inv.getInput(i);
            if (!stack.isEmpty()) {
                if (STAR_INGREDIENT.test(stack)) {
                    CompoundTag tag = stack.getTagElement(TAG_EXPLOSION);
                    if (tag != null) list.add(tag);
                }
            }
        }
        persistentData.put(key, list);
        ModifierId modifier = Modifiers.fireworkRocketStar.getId();
        if (tool.getModifierLevel(modifier) == 0)
            tool.addModifier(modifier, 1);
        return tool.createStack(Math.min(inv.getTinkerableSize(), shrinkToolSlotBy()));
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Modifiers.fireworkRocketStarSerializer.get();
    }


    /* JEI */

    @Nullable
    private List<IDisplayModifierRecipe> displayRecipes;

    @Override
    public @NotNull List<IDisplayModifierRecipe> getRecipes() {
        if (displayRecipes == null) {
            List<ItemStack> toolInputs = Arrays.stream(this.toolRequirement.getItems()).map(stack -> {
                if (stack.getItem() instanceof IModifiableDisplay) {
                    return ((IModifiableDisplay)stack.getItem()).getRenderTool();
                }
                return stack;
            }).toList();
            ModifierEntry result = new ModifierEntry(Modifiers.fireworkRocketStar.get(), 1);
            displayRecipes = Arrays.stream(DyeColor.values()).map(dye -> new FireworkRocketStarRecipe.DisplayRecipe(result, toolInputs, dye)).collect(Collectors.toList());
        }
        return displayRecipes;
    }


    /* Required */

    /** @deprecated use {@link #assemble(ITinkerStationContainer)}  */
    @Deprecated
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    /** Finished recipe */
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    public static class Finished implements FinishedRecipe {
        @Getter
        private final ResourceLocation id;
        private final Ingredient toolRequirement;

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("tools", toolRequirement.toJson());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return TinkerModifiers.armorDyeingSerializer.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    private static class DisplayRecipe implements IDisplayModifierRecipe {
        /** Cache of tint colors to save calculating it twice */
        private static final int[] TINT_COLORS = new int[16];

        /** Gets the tint color for the given dye */
        private static int getTintColor(DyeColor color) {
            int id = color.getId();
            // taking advantage of the fact no color is pure black
            if (TINT_COLORS[id] == 0) {
                float[] colors = color.getTextureDiffuseColors();
                TINT_COLORS[id] = ((int)(colors[0] * 255) << 16) | ((int)(colors[1] * 255) << 8) | (int)(colors[2] * 255);
            }
            return TINT_COLORS[id];
        }

        private final List<ItemStack> dyes;
        @Getter
        private final ModifierEntry displayResult;
        @Getter
        private final List<ItemStack> toolWithoutModifier;
        @Getter
        private final List<ItemStack> toolWithModifier;
        @Getter
        private final Component variant;
        public DisplayRecipe(ModifierEntry result, List<ItemStack> tools, DyeColor color) {
            this.displayResult = result;
            this.toolWithoutModifier = tools;
            this.dyes = RegistryHelper.getTagValueStream(Registry.ITEM, color.getTag()).map(ItemStack::new).toList();
            this.variant = Component.translatable("color.minecraft." + color.getSerializedName());

            ResourceLocation id = result.getModifier().getId();
            int tintColor = getTintColor(color);
            List<ModifierEntry> results = List.of(result);
            toolWithModifier = tools.stream().map(stack -> IDisplayModifierRecipe.withModifiers(stack, results, data -> data.putInt(id, tintColor))).toList();
        }

        @Override
        public int getInputCount() {
            return 1;
        }

        @Override
        public List<ItemStack> getDisplayItems(int slot) {
            if (slot == 0) {
                return dyes;
            }
            return Collections.emptyList();
        }

        @Override
        public IntRange getLevel() {
            return new IntRange(1, 1);
        }
    }
}
