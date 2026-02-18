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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FireworkStarModifier;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.util.RegistryHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe.withModifiers;

@Getter
public class FireworkStarModifierRecipe implements ITinkerStationRecipe, IDisplayModifierRecipe {
    public static int maxLevel() {
        return 5;
    }

    public static final RecordLoadable<FireworkStarModifierRecipe> LOADER = RecordLoadable.create(
        ContextKey.ID.requiredField(),
        FireworkStarModifierRecipe::new
    );
    private final ResourceLocation id;

    public FireworkStarModifierRecipe(ResourceLocation id) {
        this.id = id;
        ModifierRecipeLookup.addRecipeModifier(SlotType.ABILITY, ModCore.Modifiers.FIREWORK_STAR);
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
        if (!stack.isEmpty() && stack.is(ModCore.Tools.FIRECRACK.get())) {
            if (tool.getFreeSlots(SlotType.ABILITY) == 0) {
                return false;
            } else if (tool.getModifierLevel(ModCore.Modifiers.Ids.firework_star) >= maxLevel()) {
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
                newTool.addModifier(ModCore.Modifiers.Ids.firework_star, 1);
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put("Explosions", listTag);
                FireworkStarModifier.setStar(newTool, compoundTag);
                newTool.getPersistentData().addSlots(SlotType.ABILITY, -1);
                return RecipeResult.success(LazyToolStack.from(newTool.createStack()));
            }
        }
        return RecipeResult.pass();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModCore.Modifiers.STAR_SERIALIZER.get();
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
            json.addProperty("result", ModCore.Modifiers.Ids.firework_star.toString());
            json.addProperty("level", maxLevel());
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ModCore.Modifiers.STAR_SERIALIZER.get();
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
            json.addProperty("modifier", ModCore.Modifiers.Ids.firework_star.toString());
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ModCore.Modifiers.STAR_SALVAGE_SERIALIZER.get();
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

//    /* JEI display */
//    private static final ModifierEntry RESULT = new ModifierEntry(ModCore.Modifiers.FIREWORK_STAR, 1);
//    private List<ItemStack> toolWithoutModifier, toolWithModifier = null;
//
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
//
//    @Override
//    public @NotNull List<ItemStack> getToolWithoutModifier() {
//        if (toolWithoutModifier == null) {
//            Ingredient.of(ModCore.Tools.FIRECRACK);
////            toolWithoutModifier = RegistryHelper.getTagValueStream(BuiltInRegistries.ITEM, TinkerTags.Items.DURABILITY).map(MAP_TOOL_FOR_RENDERING).toList();
//        }
//        return toolWithoutModifier;
//    }
//
//    @Override
//    public @NotNull List<ItemStack> getToolWithModifier() {
//        if (toolWithModifier == null) {
//            List<ModifierEntry> result = List.of(RESULT);
//            toolWithModifier = RegistryHelper.getTagValueStream(BuiltInRegistries.ITEM, TinkerTags.Items.DURABILITY)
//                    .map(MAP_TOOL_FOR_RENDERING)
//                    .map(stack -> withModifiers(stack, result, data -> {}))
//                    .toList();
//        }
//        return toolWithModifier;
//    }
//
//    @Override
//    public @NotNull ModifierEntry getDisplayResult() {
//        return RESULT;
//    }

    /* JEI display */
    private static final ModifierEntry result = new ModifierEntry(ModCore.Modifiers.Ids.firework_star, 1);
    private static final Ingredient toolRequirement = Ingredient.of(ModCore.Tools.FIRECRACK);
    private static final SlotType.SlotCount slotCount = new SlotType.SlotCount(SlotType.ABILITY, 1);
    @Nullable
    private List<ItemStack> toolInputs = null;
    @Nullable
    protected List<SlotType.SlotCount> resultSlots = null;

    @Override
    public ResourceLocation getRecipeId() {
        return getId();
    }

    /** Gets or builds the list of tool inputs */
    protected List<ItemStack> getToolInputs() {
        if (toolInputs == null) {
            toolInputs = Arrays.stream(toolRequirement.getItems()).map(MAP_TOOL_STACK_FOR_RENDERING).collect(Collectors.toList());
        }
        return toolInputs;
    }

    /** Cache of display tool inputs */
    private List<ItemStack> displayInputs = null;

    /** Cache of display output */
    List<ItemStack> toolWithModifier = null;

    @Override
    public @NotNull ModifierEntry getDisplayResult() {
        return result;
    }

    @Override
    public @NotNull List<ItemStack> getToolWithoutModifier() {
        if (displayInputs == null) {
            ModifierEntry existing = null;
            ModifierEntry displayResult = getDisplayResult();
            displayInputs = getToolInputs().stream().map(stack -> withModifiers(stack, 1, IDisplayModifierRecipe.modifiersForResult(displayResult, existing))).collect(Collectors.toList());
        }
        return displayInputs;
    }

    @Override
    public @NotNull List<ItemStack> getToolWithModifier() {
        if (toolWithModifier == null) {
            ModifierEntry result = getDisplayResult();
            toolWithModifier = getToolInputs().stream().map(stack -> withModifiers(stack, 1, IDisplayModifierRecipe.modifiersForResult(result, result))).collect(Collectors.toList());
        }
        return toolWithModifier;
    }

    /**
     * Helper to get the result slots for a given modifier recipe by querying volatile data.
     * Does not handle modifier traits for simplicity.
     * @param result  Modifier crafted by this recipe.
     * @param tool    Representative tool for this recipe
     * @param variant Swappable variant, for recipes like rebalanced.
     */
    public static List<SlotType.SlotCount> getResultSlots(ModifierEntry result, Item tool, String variant) {
        // add variant info for the sake of rebalanced
        ModDataNBT persistentData = new ModDataNBT();
        if (!variant.isEmpty()) {
            persistentData.putString(result.getId(), variant);
        }
        // build volatile data, will read that for slot info
        ToolDataNBT volatileData = new ToolDataNBT();
        result.getHook(ModifierHooks.VOLATILE_DATA).addVolatileData(new DummyToolStack(tool, ModifierNBT.EMPTY, persistentData), result, volatileData);
        return SlotType.getAllSlotTypes().stream().map(type -> {
            int count = volatileData.getSlots(type);
            if (count > 0) {
                return new SlotType.SlotCount(type, count);
            }
            return null;
        }).filter(Objects::nonNull).toList();
    }

    @Override
    public @NotNull List<SlotType.SlotCount> getResultSlots() {
        if (resultSlots == null) {
            // we need to decide a tool for the dummy stack. Could just use air, but might as well use a tool that shows up
            // on the odd chance the behavior differs per tool this might be wrong, but practically that just affects ancient tools on input right now
            ItemStack[] tools = toolRequirement.getItems();
            resultSlots = getResultSlots(getDisplayResult(), tools.length > 0 ? tools[0].getItem() : Items.AIR, "");
        }
        return resultSlots;
    }

    @Override
    public @NotNull SlotType.SlotCount getSlots() {
        return slotCount;
    }
}