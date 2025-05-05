package org.vocalsky.extended_tinker.common.recipe;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
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
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FirecrackStarModifier;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe.withModifiers;

@Getter
public class FirecrackStarModifierRecipe implements ITinkerStationRecipe, IDisplayModifierRecipe {
    public static final RecordLoadable<FirecrackStarModifierRecipe> LOADER = RecordLoadable.create(
        ContextKey.ID.requiredField(),
        FirecrackStarModifierRecipe::new
    );
    private final ResourceLocation id;

    public FirecrackStarModifierRecipe(ResourceLocation id) {
        this.id = id;
        ModifierRecipeLookup.addRecipeModifier(SlotType.ABILITY, ModModifiers.STAR);
    }

    private boolean checkMeta(ITinkerStationContainer inv) {
        ItemStack star = ItemStack.EMPTY;

        for(int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.is(Items.FIREWORK_STAR)) {
                star = input;
            } else if (!input.isEmpty()) {
                return false;
            }
        }

        return !star.isEmpty();
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, @NotNull Level level) {
        if (!(inv.getTinkerableStack().getItem() instanceof ModifiableItem))
            return false;
        ToolStack tool = inv.getTinkerable();
        ItemStack stack = inv.getTinkerableStack();
        if (!stack.isEmpty() && stack.is(ModItems.Tools.FIRECRACK.get())) {
            if (tool.getFreeSlots(SlotType.ABILITY) == 0) {
                return false;
            } else if (tool.getModifierLevel(ModModifiers.STAR.getId()) != 0) {
                return false;
            } else {
                return this.checkMeta(inv);
            }
        } else {
            return false;
        }
    }

    @Override
    public @NotNull RecipeResult<LazyToolStack> getValidatedResult(ITinkerStationContainer inv, @NotNull RegistryAccess registryAccess) {
        ToolStack tool = inv.getTinkerable();

        for(int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.is(Items.FIREWORK_STAR)) {
                ToolStack newTool = tool.copy();
                CompoundTag tag = input.getTagElement("Explosion");
                ListTag listTag = new ListTag();
                if (tag != null) listTag.add(tag);
                if (newTool.getModifierLevel(ModModifiers.STAR.getId()) == 0) newTool.addModifier(ModModifiers.STAR.getId(), 1);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put("Explosions", listTag);
                newTool.getPersistentData().put(FirecrackStarModifier.fireworks, compoundTag);
                FirecrackStarModifier.setStar(newTool, compoundTag);
                newTool.getPersistentData().addSlots(SlotType.ABILITY, -1);
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

    @RequiredArgsConstructor
    public static class Finished implements FinishedRecipe {
        @Getter
        private final ResourceLocation id;
        private final Ingredient toolRequirement;
        private final SlotType.SlotCount slots;

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("tools", toolRequirement.toJson());
            json.addProperty("allow_crystal", false);
            JsonObject slotJson = new JsonObject();
            slotJson.addProperty(this.slots.type().getName(), this.slots.count());
            json.add("slots", slotJson);
            json.addProperty("result", ModModifiers.STAR.getId().toString());
            json.addProperty("level", 1);
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

    @RequiredArgsConstructor
    public static class FinishedSalvage implements FinishedRecipe {
        @Getter
        private final ResourceLocation id;
        private final Ingredient toolRequirement;
        private final SlotType.SlotCount slots;

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("tools", toolRequirement.toJson());
            JsonObject slotJson = new JsonObject();
            slotJson.addProperty(this.slots.type().getName(), this.slots.count());
            json.add("slots", slotJson);
            json.addProperty("modifier", ModModifiers.STAR.getId().toString());
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ModModifiers.STAR_SALVAGE_SERIALIZER.get();
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

    /* JEI display */
    /** Cache of modifier result, same for all overslime */
    private static final ModifierEntry RESULT = new ModifierEntry(ModModifiers.STAR, 1);
    /** Cache of input and output tools for display */
    private List<ItemStack> toolWithoutModifier, toolWithModifier = null;

    @Override
    public int getInputCount() {
        return 1;
    }

    @Override
    public @NotNull List<ItemStack> getDisplayItems(int slot) {
        if (slot == 0) {
            return Arrays.asList(Ingredient.of(Items.FIREWORK_STAR).getItems());
        }
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<ItemStack> getToolWithoutModifier() {
        if (toolWithoutModifier == null) {
            toolWithoutModifier = RegistryHelper.getTagValueStream(BuiltInRegistries.ITEM, TinkerTags.Items.DURABILITY).map(MAP_TOOL_FOR_RENDERING).toList();
        }
        return toolWithoutModifier;
    }

    @Override
    public @NotNull List<ItemStack> getToolWithModifier() {
        if (toolWithModifier == null) {
            FirecrackStarModifier starModifier = ModModifiers.STAR.get();
            List<ModifierEntry> result = List.of(RESULT);
            toolWithModifier = RegistryHelper.getTagValueStream(BuiltInRegistries.ITEM, TinkerTags.Items.DURABILITY)
                    .map(MAP_TOOL_FOR_RENDERING)
                    .map(stack -> withModifiers(stack, result, data -> {}))
                    .toList();
        }
        return toolWithModifier;
    }

    @Override
    public @NotNull ModifierEntry getDisplayResult() {
        return RESULT;
    }
}