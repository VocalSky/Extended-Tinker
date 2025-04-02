package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class FirecrackStarModifier extends Modifier implements ModifierRemovalHook {
    @Getter
    @Setter
    CompoundTag tag;
//    public static ResourceLocation fireworks = new ResourceLocation("Fireworks");
//    public static void setStar(ToolStack tool, CompoundTag tag) {
//        tool.getPersistentData().put(fireworks, tag);
//    }
//    public static CompoundTag getStar(ToolStack tool) {
//        if (tool.getPersistentData().contains(fireworks, Tag.TAG_COMPOUND))
//            return tool.getPersistentData().getCompound(fireworks);
//        return new CompoundTag();
//    }

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
