package org.vocalsky.extended_tinker.common.menu;

import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.block.entity.UltraPartBuilderBlockEntity;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tables.block.entity.inventory.LazyResultContainer;
import slimeknights.tconstruct.tables.menu.TabbedContainerMenu;
import slimeknights.tconstruct.tables.menu.slot.LazyResultSlot;

public class UltraPartBuilderContainerMenu extends TabbedContainerMenu<UltraPartBuilderBlockEntity> {
    @Getter
    private final Slot patternSlot;
    @Getter
    private final Slot inputSlot;
    @Getter
    private final Slot fuelSlot;
    @Getter
    private final LazyResultSlot outputSlot;

    public UltraPartBuilderContainerMenu(int id, @Nullable Inventory inv, @Nullable UltraPartBuilderBlockEntity ultraPartBuilderBlockEntity) {
        super(ModCore.Menus.ultraPartBuilderContainer.get(), id, inv, ultraPartBuilderBlockEntity);

        // unfortunately, nothing works with no tile
        if (tile != null) {
            // slots
            this.addSlot(this.outputSlot = new LazyResultSlot(tile.getCraftingResult(), 138, 45));
            // inputs
            this.addSlot(this.patternSlot = new PatternSlot(tile, 75, 45));
            this.addSlot(this.inputSlot = new MaterialSlot(tile, UltraPartBuilderBlockEntity.MATERIAL_SLOT, 96, 45));
            this.addSlot(this.fuelSlot = new Slot(tile, UltraPartBuilderBlockEntity.FUEL_SLOT, 38, 67));

            // other inventories
            this.addChestSideInventory();
            this.addInventorySlots();

            // update for the first time
            this.updateScreen();
        } else {
            this.patternSlot = null;
            this.inputSlot = null;
            this.fuelSlot = null;
            this.outputSlot = null;
        }
    }

    public UltraPartBuilderContainerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, getTileEntityFromBuf(buf, UltraPartBuilderBlockEntity.class));
    }

    @Override
    protected int getInventoryYOffset() {
        return 102;
    }

    @Override
    public void slotsChanged(@NotNull Container inventoryIn) {}

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slotIn) {
        return slotIn != this.outputSlot && super.canTakeItemForPickAll(stack, slotIn);
    }

    /** Slot to update recipe on change */
    private static class PartBuilderSlot extends Slot {
        private final LazyResultContainer craftResult;
        public PartBuilderSlot(UltraPartBuilderBlockEntity tile, int index, int xPosition, int yPosition) {
            super(tile, index, xPosition, yPosition);
            craftResult = tile.getCraftingResult();
        }

        @Override
        public void setChanged() {
            craftResult.clearContent();
            super.setChanged();
        }
    }

    /** Slot for the material, which wants to force a screen update */
    private class MaterialSlot extends PartBuilderSlot {
        public MaterialSlot(UltraPartBuilderBlockEntity tile, int index, int xPosition, int yPosition) {
            super(tile, index, xPosition, yPosition);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            updateScreen(); // no other good way to detect stack size decreasing, e.g. on right click
        }
    }

    /**
     * Slot for the pattern, updates buttons on change
     */
    private static class PatternSlot extends PartBuilderSlot {
        private PatternSlot(UltraPartBuilderBlockEntity tile, int x, int y) {
            super(tile, UltraPartBuilderBlockEntity.PATTERN_SLOT, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return (stack.is(TinkerTags.Items.GOLD_CASTS) || stack.is(TinkerTags.Items.SAND_CASTS) || stack.is(TinkerTags.Items.RED_SAND_CASTS)) && (!stack.is(TinkerSmeltery.blankSandCast.get()) && !stack.is(TinkerSmeltery.blankRedSandCast.get()));
        }
    }
}
