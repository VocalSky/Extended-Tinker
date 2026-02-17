package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.tool.FireworkRocketItem;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.modules.InventorySelectionModule;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public enum ShootFireworkModule implements ModifierModule, BowAmmoModifierHook, GeneralInteractionModifierHook, ModifierRemovalHook, InventorySelectionModule {
    INSTANCE;

    public static final SingletonLoader<ShootFireworkModule> LOADER = new SingletonLoader<>(INSTANCE);
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.defaultHooks(ModifierHooks.BOW_AMMO, ModifierHooks.GENERAL_INTERACT, ModifierHooks.REMOVE);
    private static final ResourceLocation SELECTED_SLOT = Extended_tinker.getResource("shoot_firework_selected");
    private static final Component DISABLED = Extended_tinker.makeTranslation("modifier", "shoot_firework.disabled");
    private static final String SELECTED = Extended_tinker.makeTranslationKey("modifier", "shoot_firework.selected");

    public @NotNull RecordLoadable<ShootFireworkModule> getLoader() {
        return LOADER;
    }

    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    private ItemStack consumeTool = null;

    public @NotNull ItemStack findAmmo(@NotNull IToolStackView tool, ModifierEntry modifier, @NotNull LivingEntity shooter, @NotNull ItemStack standardAmmo, @NotNull Predicate<ItemStack> ammoPredicate) {
        ItemStack itemStack = modifier.getHook(ToolInventoryCapability.HOOK).getStack(tool, modifier, tool.getPersistentData().getInt(SELECTED_SLOT));
        consumeTool = itemStack.copy();
        return FireworkRocketItem.createFireworkRocket(ToolStack.from(itemStack));
    }

    public void shrinkAmmo(@NotNull IToolStackView tool, ModifierEntry modifier, @NotNull LivingEntity shooter, @NotNull ItemStack ammo, int needed) {
        ToolStack toolStack = ToolStack.from(consumeTool);
        ToolDamageUtil.damageAnimated(toolStack, needed, shooter);
        toolStack.updateStack(consumeTool);
        modifier.getHook(ToolInventoryCapability.HOOK).setStack(tool, modifier, tool.getPersistentData().getInt(SELECTED_SLOT), consumeTool);
        consumeTool = null;
    }

    public void onDisableSelection(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, Player player) {
        player.displayClientMessage(DISABLED, true);
    }

    public void onInventorySelect(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, Player player, int newIndex, ItemStack stack) {
        player.displayClientMessage(Component.translatable(SELECTED, stack.getHoverName(), newIndex + 1), true);
    }

    public @NotNull InteractionResult onToolUse(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, Player player, @NotNull InteractionHand hand, @NotNull InteractionSource source) {
        if (!player.isCrouching()) {
            return this.selectNext(tool, modifier, player, SELECTED_SLOT) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Nullable
    public Component onRemoved(IToolStackView tool, @NotNull Modifier modifier) {
        tool.getPersistentData().remove(SELECTED_SLOT);
        return null;
    }
}