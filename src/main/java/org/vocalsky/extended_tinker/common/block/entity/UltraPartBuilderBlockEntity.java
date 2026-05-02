package org.vocalsky.extended_tinker.common.block.entity;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.block.entity.inventory.UltraPartBuilderContainerWrapper;
import org.vocalsky.extended_tinker.common.menu.UltraPartBuilderContainerMenu;
import org.vocalsky.extended_tinker.common.recipe.UltraPartRecipe;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.material.IMaterialValue;
import slimeknights.tconstruct.library.recipe.partbuilder.IPartBuilderRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.PartRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.shared.inventory.ConfigurableInvWrapperCapability;
import slimeknights.tconstruct.tables.block.entity.inventory.LazyResultContainer;
import slimeknights.tconstruct.tables.block.entity.table.RetexturedTableBlockEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UltraPartBuilderBlockEntity extends RetexturedTableBlockEntity implements LazyResultContainer.ILazyCrafter {
    /** First slot containing materials */
    public static final int MATERIAL_SLOT = 0;
    /** Second slot containing the patterns */
    public static final int PATTERN_SLOT = 1;
    /** Third slot containing the fuels */
    public static final int FUEL_SLOT = 2;
    /** Title for the GUI */
    private static final Component NAME = Extended_tinker.makeTranslation("gui", "ultra_part_builder");

    /** Result inventory, lazy loads results */
    @Getter
    private final LazyResultContainer craftingResult;
    /** Crafting inventory for the recipe calls */
    @Getter
    private final UltraPartBuilderContainerWrapper inventoryWrapper;

    /* Current buttons to display */
    @Nullable
    private Map<Pattern, IPartBuilderRecipe> recipes = null;
    @Nullable
    private List<Pattern> sortedButtons = null;
    /** Currently selected recipe index */
    private Pattern selectedPattern = null;

    public UltraPartBuilderBlockEntity(BlockPos pos, BlockState state) {
        super(ModCore.Blocks.ultraPartBuilderTile.get(), pos, state, NAME, 4);
        this.itemHandler = new ConfigurableInvWrapperCapability(this, false, false);
        this.itemHandlerCap = LazyOptional.of(() -> this.itemHandler);
        this.inventoryWrapper = new UltraPartBuilderContainerWrapper(this);
        this.craftingResult = new LazyResultContainer(this);
    }

    /**
     * Gets a map of all recipes for the current inputs
     * @return  List of recipes for the current inputs
     */
    protected Map<Pattern, IPartBuilderRecipe> getCurrentRecipes() {
        if (level == null) {
            return Collections.emptyMap();
        }
        if (recipes == null) {
            // no recipes if we lack a pattern
            if (getItem(PATTERN_SLOT).isEmpty()) {
                recipes = Collections.emptyMap();
                sortedButtons = Collections.emptyList();
            } else {
                record PatternRecipe(Pattern pattern, IPartBuilderRecipe recipe) {}
                // fetch all recipes that can match these inputs, the map ensures the patterns are unique
                recipes = level.getRecipeManager().byType(TinkerRecipeTypes.PART_BUILDER.get()).values().stream()
                        .filter(r -> (r instanceof PartRecipe ? new UltraPartRecipe((PartRecipe) r) : r).partialMatch(inventoryWrapper))
                        .sorted(Comparator.comparing(Recipe::getId))
                        .flatMap(r -> r.getPatterns(inventoryWrapper).map(p -> new PatternRecipe(p, r)))
                        .collect(Collectors.toMap(PatternRecipe::pattern, PatternRecipe::recipe, (a, b) -> a));
                sortedButtons = recipes.entrySet()
                        .stream()
                        .sorted(Comparator.<Map.Entry<Pattern,IPartBuilderRecipe>>comparingInt(ent -> ent.getValue().getCost()).thenComparing(Map.Entry::getKey))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
            }
        }
        return recipes;
    }

    /** Gets the list of sorted buttons */
    public List<Pattern> getSortedButtons() {
        if (level == null) {
            return Collections.emptyList();
        }
        if (sortedButtons == null) {
            getCurrentRecipes();
        }
        return sortedButtons;
    }

    /**
     * Gets the currently selected recipe
     * @return  Selected recipe, or null if invalid or no recipe
     */
    @Nullable
    public IPartBuilderRecipe getPartRecipe() {
        if (selectedPattern == null) return null;
        IPartBuilderRecipe recipe = getCurrentRecipes().get(selectedPattern);
        if (!(recipe instanceof PartRecipe)) return recipe;
        return new UltraPartRecipe((PartRecipe) recipe);
    }

    /** Gets the first available recipe */
    @Nullable
    public IPartBuilderRecipe getFirstRecipe() {
        List<Pattern> sortedButtons = getSortedButtons();
        if (sortedButtons.isEmpty()) {
            return null;
        }
        return getCurrentRecipes().get(sortedButtons.get(0));
    }

    /**
     * Gets the material recipe for the material slot
     * @return  Material slot
     */
    @Nullable
    public IMaterialValue getMaterialRecipe() {
        return inventoryWrapper.getMaterial();
    }

    /**
     * Refreshes the current recipe
     */
    private void refresh() {
        this.recipes = null;
        this.sortedButtons = null;
        this.craftingResult.clearContent();
        if (getFirstRecipe() != null) this.selectedPattern = getFirstRecipe().getPattern();
        syncScreenToRelevantPlayers();
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        ItemStack original = getItem(slot);
        super.setItem(slot, stack);
        if (slot == MATERIAL_SLOT) {
            // if item or NBT changed, update
            if (!ItemStack.isSameItemSameTags(original, stack)) {
                this.inventoryWrapper.refreshMaterial();
                refresh();
                // if size changed, we are still the same material but might no longer have enough
                // same stack calling this method typically indicates a size change, stacks being mutable is annoying
            } else if (original.getCount() != stack.getCount() || original == stack) {
                this.craftingResult.clearContent();
                syncScreenToRelevantPlayers();
            }
            // any other slot, only an item change means update
        } else if (original.getItem() != stack.getItem()) {
            refresh();
        }
        refresh();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int menuId, @NotNull Inventory playerInventory, @NotNull Player playerEntity) {
        return new UltraPartBuilderContainerMenu(menuId, playerInventory, this);
    }

    @Override
    public @NotNull ItemStack calcResult(@Nullable Player player) {
        if (level != null) {
            IPartBuilderRecipe recipe = getPartRecipe();
            if (recipe != null && recipe.matches(inventoryWrapper, level)) {
                return recipe.assemble(inventoryWrapper, level.registryAccess(), selectedPattern);
            }
        }
        return ItemStack.EMPTY;
    }

    private void shrinkSlot(int slot, int amount, Player player) {
        if (amount <= 0) {
            return;
        }
        ItemStack stack = getItem(slot);
        if (!stack.isEmpty()) {
            ItemStack container = stack.getCraftingRemainingItem().copy();
            if (amount > 1) {
                container.setCount(container.getCount() * amount);
            }
            if (stack.getCount() <= amount) {
                setItem(slot, container);
            } else {
                stack.shrink(amount);
                ItemHandlerHelper.giveItemToPlayer(player, container);
            }
        }
    }

    @Override
    public void onCraft(@NotNull Player player, @NotNull ItemStack result, int amount) {
        if (amount == 0 || this.level == null) {
            return;
        }
        IPartBuilderRecipe recipe = getPartRecipe();
        if (recipe == null) {
            return;
        }

        result.onCraftedBy(this.level, player, amount);
        ForgeEventFactory.firePlayerCraftingEvent(player, result, this.inventoryWrapper);
        this.playCraftSound(player);

        // give the player any leftovers
        if (level != null && !level.isClientSide) {
            ItemStack leftover = recipe.getLeftover(inventoryWrapper, selectedPattern);
            if (!leftover.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(player, leftover);
            }
        }

        // shrink the inputs
        shrinkSlot(MATERIAL_SLOT, recipe.getItemsUsed(inventoryWrapper), player);
        if (!getItem(PATTERN_SLOT).is(TinkerTags.Items.REUSABLE_PATTERNS)) {
            shrinkSlot(PATTERN_SLOT, 1, player);
        }

        // sync display, mainly for the material value
        syncScreenToRelevantPlayers();
    }
}
