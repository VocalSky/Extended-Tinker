package org.vocalsky.extended_tinker.common.menu;

import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.block.entity.UltraPartBuilderBlockEntity;
import slimeknights.tconstruct.common.TinkerTags;
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
            this.addSlot(this.outputSlot = new ResultSlot(tile.getCraftingResult(), 138, 45));
            // inputs
            this.addSlot(this.patternSlot = new PatternSlot(tile, 75, 45));
            this.addSlot(this.inputSlot = new MaterialSlot(tile, UltraPartBuilderBlockEntity.MATERIAL_SLOT, 96, 45));
            this.addSlot(this.fuelSlot = new Slot(tile, UltraPartBuilderBlockEntity.FUEL_SLOT, 38, 67));

            // other inventories
            this.addChestSideInventory();
            this.addInventorySlots();

            // update for the first time
            this.updateScreen();

            System.out.println("Menu create " + ultraPartBuilderBlockEntity.getLevel().isClientSide + " " + this.slots.size());
            for (int i = 0; i < this.slots.size(); i++) {
                Slot s = this.slots.get(i);
                System.out.println("[UPB] slot[" + i + "] class=" + s.getClass().getSimpleName() + " container=" + s.container.getClass().getSimpleName() + " indexInContainer=" + s.index);
            }
        } else {
            this.patternSlot = null;
            this.inputSlot = null;
            this.fuelSlot = null;
            this.outputSlot = null;
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        System.out.println("[UPB] quickMoveStack called on server? player=" + playerIn.level().isClientSide + " index=" + index);
        try {
            return super.quickMoveStack(playerIn, index);
        } catch (Exception e) {
            System.out.println("[UPB] quickMoveStack exception: " + e);
            throw e;
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
            return stack.is(TinkerTags.Items.REUSABLE_PATTERNS);
        }
    }

    private class ResultSlot extends LazyResultSlot {
        public ResultSlot(LazyResultContainer inventory, int xPosition, int yPosition) {
            super(inventory, xPosition, yPosition);
        }
        public ItemStack remove(int amount) {
            System.out.println("result slot remove called");
            return super.remove(amount);
        }

        public void onTake(Player player, ItemStack stack) {
            System.out.println("result slot onTake called " + player.level().isClientSide);
            super.onTake(player, stack);
        }

        protected void onQuickCraft(ItemStack stack, int amount) {
            System.out.println("result slot onQuickCraft called");
            super.onQuickCraft(stack, amount);
        }

        protected void onSwapCraft(int numItemsCrafted) {
            System.out.println("result slot onSwapCraft called");
            super.onSwapCraft(numItemsCrafted);
        }
    }
}
