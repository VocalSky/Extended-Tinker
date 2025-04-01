package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FirecrackStarModifier extends Modifier implements ModifierRemovalHook {
    private static final TinkerDataCapability.TinkerDataKey<Integer> STAR = TConstruct.createKey("star_firecrack");

    @Getter
    @Setter
    private CompoundTag tag = new CompoundTag();

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.REMOVE);
    }

    @Override
    public @Nullable Component onRemoved(@NotNull IToolStackView tool, @NotNull Modifier modifier) {
        tool.getPersistentData().remove(getId());
        return null;
    }
}
