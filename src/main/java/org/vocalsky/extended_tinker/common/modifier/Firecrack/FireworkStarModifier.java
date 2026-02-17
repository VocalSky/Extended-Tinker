package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkStarItem;
import net.minecraft.world.item.TooltipFlag;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class FireworkStarModifier extends Modifier implements ModifierRemovalHook, TooltipModifierHook {
    public static ResourceLocation fireworks = Extended_tinker.getResource("fireworks");
    public static void setStar(IToolStackView tool, CompoundTag tag) {
        ListTag combined = getStar(tool).getList("Explosions", 10).copy();
        combined.addAll(tag.getList("Explosions", 10));
        CompoundTag newTag = new CompoundTag();
        newTag.put("Explosions", combined);
        tool.getPersistentData().put(fireworks, newTag);
    }
    public static CompoundTag getStar(IToolStackView tool) {
        if (tool.getPersistentData().contains(fireworks, Tag.TAG_COMPOUND))
            return tool.getPersistentData().getCompound(fireworks);
        return new CompoundTag();
    }

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.REMOVE);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP);
    }

    @Override
    public @Nullable Component onRemoved(@NotNull IToolStackView tool, @NotNull Modifier modifier) {
        tool.getPersistentData().remove(fireworks);
        return null;
    }

    @Override
    public void addTooltip(@NotNull IToolStackView iToolStackView, @NotNull ModifierEntry modifierEntry, @Nullable Player player, @NotNull List<Component> list, @NotNull TooltipKey tooltipKey, @NotNull TooltipFlag tooltipFlag) {
        CompoundTag compoundTag = FireworkStarModifier.getStar(iToolStackView);
        ListTag listTag = compoundTag.getList("Explosions", Tag.TAG_COMPOUND);
        if (!listTag.isEmpty()) {
            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag tag = listTag.getCompound(i);
                List<Component> description = Lists.newArrayList();
                FireworkStarItem.appendHoverText(tag, description);
                if (!description.isEmpty()) {
                    for (int j = 1; j < description.size(); ++j)
                        description.set(j, Component.literal("  ").append(description.get(j)).withStyle(ChatFormatting.GRAY));
                    list.addAll(description);
                }
            }
        }
    }
}