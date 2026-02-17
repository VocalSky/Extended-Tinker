package org.vocalsky.extended_tinker.common.tool;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.entity.FireworkRocketEntity;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FireworkStarModifier;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.*;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class FireworkRocketItem extends ModifiableArrowItem {
    public FireworkRocketItem(Item.Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        FireworkRocketEntity arrow = new FireworkRocketEntity(level, shooter);
        arrow.onCreate(stack, shooter);
        return arrow;
    }

    public static ItemStack createFireworkRocket(ToolStack toolStack) {
        if (toolStack.isBroken()) return ItemStack.EMPTY;
        MaterialNBT materialNBT = toolStack.getMaterials();
        ToolStack ammo = ToolStack.createTool(ModCore.Tools.FIREWORK_ROCKET.asItem(), ModCore.Tools.Definitions.FIREWORK_ROCKET, materialNBT);
        ammo.setUpgrades(toolStack.getUpgrades());
        ammo.addModifier(ModCore.Modifiers.FIREWORK_FLIGHT.getId(), toolStack.getModifierLevel(ModCore.Modifiers.FIREWORK_FLIGHT.getId()));
        ammo.addModifier(ModCore.Modifiers.firework_rocket, 1);
        if (toolStack.getModifierLevel(ModCore.Modifiers.FIREWORK_STAR.getId()) > 0)
            FireworkStarModifier.setStar(ammo, FireworkStarModifier.getStar(toolStack));
        return ammo.createStack(1);
    }
}