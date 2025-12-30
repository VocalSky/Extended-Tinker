package org.vocalsky.extended_tinker.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import pyre.tinkerslevellingaddon.ImprovableModifier;
import pyre.tinkerslevellingaddon.config.Config;
import pyre.tinkerslevellingaddon.util.ModUtil;
import pyre.tinkerslevellingaddon.util.ToolLevellingUtil;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExpTransferOrb extends Item {
    public static final ResourceLocation IS_BOARD_KEY = Extended_tinker.getResource("is_board");

    public ExpTransferOrb(Properties properties) {
        super(properties);
    }

    private static TextColor getLevelColor(int level) {
        float hue = 0.277777F * (float)level;
        hue -= (float)((int)hue);
        return TextColor.fromRgb(Color.HSBtoRGB(hue, 0.75F, 0.8F));
    }

    private static MutableComponent getLevelName(int level) {
        TextColor levelColor = getLevelColor(level);
        if (ModUtil.canTranslate("tooltip", "level." + level)) {
            return ModUtil.makeTranslation("tooltip", "level." + level, levelColor);
        } else {
            int i;
            for(i = 1; ModUtil.canTranslate("tooltip", "level." + i); ++i) ;
            int tier = level / i;
            String suffix = (Boolean) Config.squashLevelPluses.get() && level > 0 ? "+" + tier : "+".repeat(tier);
            return ModUtil.makeTranslation("tooltip", "level." + level % i, levelColor).append(ModUtil.makeText(suffix, levelColor));
        }
    }

    private static List<Component> prepareGeneralInfo(ItemStack transfer) {
        List<Component> infoEntries = new ArrayList<>();
        CompoundTag tag = transfer.getOrCreateTag();
        int level = tag.getInt(ImprovableModifier.LEVEL_KEY.toString());
        MutableComponent fullLevelName = ModUtil.makeTranslation("tooltip", "level.name", ChatFormatting.GRAY, new Object[]{getLevelName(level), ModUtil.makeText(level, ChatFormatting.GRAY)});
        infoEntries.add(ModUtil.makeTranslation("tooltip", "level", new Object[]{fullLevelName}));
        if (ToolLevellingUtil.canLevelUp(level)) {
            MutableComponent xp = ModUtil.makeText(tag.getInt(ImprovableModifier.EXPERIENCE_KEY.toString()), ChatFormatting.GOLD);
            infoEntries.add(ModUtil.makeTranslation("tooltip", "xp", xp));
        }

        return infoEntries;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> componentList, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, componentList, tooltipFlag);
        if (!ModCore.LevellingAddonLoaded()) return;
        List<Component> infoEntries = prepareGeneralInfo(itemStack);
        componentList.addAll(infoEntries);
    }
}
