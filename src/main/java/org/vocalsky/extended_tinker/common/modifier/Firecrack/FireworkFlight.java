package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.entity.FireworkEntity;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.List;

public class FireworkFlight extends Modifier implements GeneralInteractionModifierHook, TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP);
    }

    @Override
    public int getPriority() {
        return 220;
    }

    @Override
    public @NotNull InteractionResult onToolUse(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull Player player, @NotNull InteractionHand hand, @NotNull InteractionSource source) {
        if (player.isFallFlying()) {
            ItemStack stack = player.getItemInHand(hand);
            ItemStack other = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            Level level = player.level();
            if (!level.isClientSide && !tool.isBroken() && (other.is(ModCore.Tags.FIREWORK_FLINT) || (other.getItem() instanceof IModifiable && !ToolStack.from(other).isBroken() && ToolStack.from(other).getModifierLevel(TinkerModifiers.firestarter.get()) > 0))) {
                FireworkEntity fireworkEntity = new FireworkEntity(level, stack, player);
                level.addFreshEntity(fireworkEntity);
                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
                    other.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND));
                }
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void addTooltip(@NotNull IToolStackView iToolStackView, @NotNull ModifierEntry modifierEntry, @Nullable Player player, @NotNull List<Component> list, @NotNull TooltipKey tooltipKey, @NotNull TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.minecraft.firework_rocket.flight").append(" ").append(String.valueOf(modifierEntry.getLevel())).withStyle(ChatFormatting.GRAY));
    }
}