package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModCore;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.item.ItemPredicate;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.json.IntRange;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.capability.inventory.InventoryModule;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class FireworkInventoryModule extends InventoryModule {
    /** Loader instance */
    public static final RecordLoadable<FireworkInventoryModule> LOADER = RecordLoadable.create(KEY_FIELD, SLOTS_FIELD, LIMIT_FIELD, PATTERN_FIELD, ModifierCondition.CONTEXT_FIELD, VALIDATION_FIELD, FireworkInventoryModule::new);

    private FireworkInventoryModule(@Nullable ResourceLocation key, LevelingInt slots, LevelingInt slotLimit, @Nullable Pattern pattern, ModifierCondition<IToolContext> condition, IntRange validationLevel) {
        super(key, slots, slotLimit, ItemPredicate.ANY, pattern, condition, validationLevel);
    }

    @Override
    public @NotNull RecordLoadable<FireworkInventoryModule> getLoader() {
        return LOADER;
    }

    @Override
    public boolean isItemValid(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, int slot, @NotNull ItemStack stack) {
        if (condition().matches(tool, modifier)) {
            return stack.is(ModCore.Tools.FIRECRACK.get());
        }
        return false;
    }


    /* Builder */

    /** Creates a new builder instance */
    public static FireworkInventoryModule.Builder builder() {
        return new FireworkInventoryModule.Builder();
    }

    public static class Builder extends InventoryModule.Builder {
        private Builder() {}

        @Deprecated(forRemoval = true)
        @Override
        public InventoryModule.@NotNull Builder filter(@NotNull IJsonPredicate<Item> filter) {
            throw new IllegalStateException("Cannot set filter on FireworkInventoryModule");
        }

        @Override
        public @NotNull InventoryModule slots(int base, int perLevel) {
            return new FireworkInventoryModule(key, new LevelingInt(base, perLevel), slotLimit, pattern, condition, validationLevel);
        }
    }
}

