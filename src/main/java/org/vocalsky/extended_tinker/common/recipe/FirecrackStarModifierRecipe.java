package org.vocalsky.extended_tinker.common.recipe;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FirecrackStarModifier;
import slimeknights.mantle.data.loadable.common.IngredientLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.IMultiRecipe;
import slimeknights.mantle.util.RegistryHelper;
import slimeknights.tconstruct.library.json.IntRange;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolPartsHook;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.recipe.ArmorDyeingRecipe;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

//public class FirecrackStarModifierRecipe implements ITinkerStationRecipe, IMultiRecipe<IDisplayModifierRecipe> {
//    public static final RecordLoadable<FirecrackStarModifierRecipe> LOADER = RecordLoadable.create(ContextKey.ID.requiredField(), IngredientLoadable.DISALLOW_EMPTY.requiredField("tools", r -> r.toolRequirement), FirecrackStarModifierRecipe::new);
//
//    @Getter
//    private final ResourceLocation id;
//    private final Ingredient toolRequirement;
//
//    public FirecrackStarModifierRecipe(ResourceLocation id, Ingredient toolRequirement) {
//        this.id = id;
//        this.toolRequirement = toolRequirement;
//        ModifierRecipeLookup.addRecipeModifier(SlotType.ABILITY, ModModifiers.STAR);
//    }
//
//    private List<IDisplayModifierRecipe> displayRecipes;
//
//    @Override
//    public List<IDisplayModifierRecipe> getRecipes() {
//        if (displayRecipes == null) {
//            List<ItemStack> toolInputs = Arrays.stream(this.toolRequirement.getItems()).map(stack -> {
//                if (stack.getItem() instanceof IModifiableDisplay) {
//                    return ((IModifiableDisplay)stack.getItem()).getRenderTool();
//                }
//                return stack;
//            }).toList();
//            ModifierEntry result = new ModifierEntry(ModModifiers.STAR.get(), 1);
//            displayRecipes = Arrays.stream(DyeColor.values()).map(dye -> new FirecrackStarModifierRecipe.DisplayRecipe(result, toolInputs, dye)).collect(Collectors.toList());
//        }
//        return displayRecipes;
//    }
//
//    @Override
//    public boolean matches(ITinkerStationContainer inv, @NotNull Level level) {
//        // ensure this modifier can be applied
//        if (!this.toolRequirement.test(inv.getTinkerableStack())) {
//            return false;
//        }
//        // slots must be only dyes, and have at least 1 dye
//        boolean found = false;
//        for (int i = 0; i < inv.getInputCount(); i++) {
//            ItemStack input = inv.getInput(i);
//            if (!input.isEmpty()) {
//                if (!input.is(Tags.Items.DYES)) {
//                    return false;
//                }
//                found = true;
//            }
//        }
//        return found;
//    }
//
//    @Deprecated
//    @Override
//    public @NotNull ItemStack getResultItem() {
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return ModModifiers.STAR_SERIALIZER.get();
//    }
//
//    @RequiredArgsConstructor
//    public static class Finished implements FinishedRecipe {
//        @Getter
//        private final ResourceLocation id;
//        private final Ingredient toolRequirement;
//
//        @Override
//        public void serializeRecipeData(JsonObject json) {
//            json.add("tools", toolRequirement.toJson());
//        }
//
//        @Override
//        public RecipeSerializer<?> getType() {
//            return ModModifiers.STAR_SERIALIZER.get();
//        }
//
//        @Nullable
//        @Override
//        public JsonObject serializeAdvancement() {
//            return null;
//        }
//
//        @Nullable
//        @Override
//        public ResourceLocation getAdvancementId() {
//            return null;
//        }
//    }
//
//    private static class DisplayRecipe implements IDisplayModifierRecipe {
//        /** Cache of tint colors to save calculating it twice */
//        private static final int[] TINT_COLORS = new int[16];
//
//        /** Gets the tint color for the given dye */
//        private static int getTintColor(DyeColor color) {
//            int id = color.getId();
//            // taking advantage of the fact no color is pure black
//            if (TINT_COLORS[id] == 0) {
//                float[] colors = color.getTextureDiffuseColors();
//                TINT_COLORS[id] = ((int)(colors[0] * 255) << 16) | ((int)(colors[1] * 255) << 8) | (int)(colors[2] * 255);
//            }
//            return TINT_COLORS[id];
//        }
//
//        private final List<ItemStack> dyes;
//        @Getter
//        private final ModifierEntry displayResult;
//        @Getter
//        private final List<ItemStack> toolWithoutModifier;
//        @Getter
//        private final List<ItemStack> toolWithModifier;
//        @Getter
//        private final Component variant;
//        public DisplayRecipe(ModifierEntry result, List<ItemStack> tools, DyeColor color) {
//            this.displayResult = result;
//            this.toolWithoutModifier = tools;
//            this.dyes = RegistryHelper.getTagValueStream(Registry.ITEM, color.getTag()).map(ItemStack::new).toList();
//            this.variant = Component.translatable("color.minecraft." + color.getSerializedName());
//
//            ResourceLocation id = result.getModifier().getId();
//            int tintColor = getTintColor(color);
//            List<ModifierEntry> results = List.of(result);
//            toolWithModifier = tools.stream().map(stack -> IDisplayModifierRecipe.withModifiers(stack, results, data -> data.putInt(id, tintColor))).toList();
//        }
//
//        @Override
//        public int getInputCount() {
//            return 1;
//        }
//
//        @Override
//        public @NotNull List<ItemStack> getDisplayItems(int slot) {
//            if (slot == 0) {
//                return dyes;
//            }
//            return Collections.emptyList();
//        }
//
//        @Override
//        public @NotNull IntRange getLevel() {
//            return new IntRange(1, 1);
//        }
//    }
//}

public class FirecrackStarModifierRecipe implements ITinkerStationRecipe {
    private final ResourceLocation id;
    public static final ResourceLocation GLORIOUS_KEY = Extended_tinker.getResource("glorious_seal_modifiable");
    public static final ResourceLocation GLORIOUS_META_KEY = Extended_tinker.getResource("glorious_seal_meta");

    public FirecrackStarModifierRecipe(ResourceLocation id) {
        this.id = id;
    }

    private boolean checkMeta(ITinkerStationContainer inv) {
        ItemStack paper = ItemStack.EMPTY;
        ItemStack gold_block = ItemStack.EMPTY;
        ItemStack sea_dream_ingo = ItemStack.EMPTY;
        ItemStack manyullyn_ingot = ItemStack.EMPTY;

        for(int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.is(Items.PAPER)) {
                paper = input;
            } else if (input.is(Items.GOLD_BLOCK)) {
                gold_block = input;
//            } else if (input.is((Item)TIItems.SEA_DREAM_INGOT.get())) {
//                sea_dream_ingo = input;
            } else if (input.is(TinkerMaterials.manyullyn.getIngot())) {
                manyullyn_ingot = input;
            }
        }

        return !paper.isEmpty() && !gold_block.isEmpty() && !sea_dream_ingo.isEmpty() && !manyullyn_ingot.isEmpty();
    }

    public static boolean hasMetaIn(ToolStack tool, String id) {
        Iterator var2 = tool.getMaterials().getList().iterator();
        if (var2.hasNext()) {
            MaterialVariant material = (MaterialVariant)var2.next();
            return material.get().getIdentifier().toString().equals(id);
        } else {
            return false;
        }
    }

    public boolean matches(ITinkerStationContainer inv, @NotNull Level level) {
        ToolStack tool = inv.getTinkerable();
        ItemStack stack = inv.getTinkerableStack();
        if (!stack.isEmpty() && stack.is(slimeknights.tconstruct.common.TinkerTags.Items.MULTIPART_TOOL)) {
            if (tool.getPersistentData().getBoolean(GLORIOUS_KEY)) {
                return false;
            } else {
                if (hasMetaIn(tool, "tinkers_ingenuity:splendid")) {
                    for(int i = 0; i < inv.getInputCount(); ++i) {
                        ItemStack input = inv.getInput(i);
                        if (input.getItem() instanceof IToolPart) {
                            List<IToolPart> parts = ToolPartsHook.parts(tool.getDefinition());
                            if (parts.isEmpty()) {
                                return false;
                            }

                            return this.checkMeta(inv) && parts.stream().anyMatch((p) -> p.asItem() == input.getItem());
                        }
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public @NotNull RecipeResult<ItemStack> getValidatedResult(ITinkerStationContainer inv) {
        ToolStack tool = inv.getTinkerable();

        for(int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            Item newToolItem = input.getItem();
            if (newToolItem instanceof IToolPart part) {
                ToolStack newTool = tool.copy();

                for(ModifierEntry trait : MaterialRegistry.getInstance().getTraits(part.getMaterial(input).getId(), part.getStatType())) {
                    newTool.addModifier(trait.getId(), trait.getLevel());
                }

                newTool.getPersistentData().putBoolean(GLORIOUS_KEY, true);
                newTool.getPersistentData().putString(GLORIOUS_META_KEY, part.getMaterial(input).getId().toLanguageKey());
                return RecipeResult.success(newTool.createStack());
            }
        }

        return RecipeResult.pass();
    }

    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModModifiers.STAR_SERIALIZER.get();
    }

    public @NotNull RecipeType<?> getType() {
        return TinkerRecipeTypes.TINKER_STATION.get();
    }

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
        public @NotNull RecipeSerializer<?> getType() {
            return ModModifiers.STAR_SERIALIZER.get();
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
}