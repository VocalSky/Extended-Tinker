package org.vocalsky.extended_tinker.common;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;

public class ModTab extends CreativeModeTab {
    public static ModTab INSTANCE = new ModTab();

    public ModTab() {
        super(Extended_tinker.MODID);
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return ModTools.HORSE_ARMOR.get().getRenderTool();
    }
}
