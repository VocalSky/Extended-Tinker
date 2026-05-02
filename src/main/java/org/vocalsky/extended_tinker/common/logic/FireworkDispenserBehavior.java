package org.vocalsky.extended_tinker.common.logic;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.entity.FireworkRocketEntity;
import org.vocalsky.extended_tinker.common.tool.FireworkRocketItem;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class FireworkDispenserBehavior extends AbstractProjectileDispenseBehavior {
    public static FireworkDispenserBehavior INSTANCE = new FireworkDispenserBehavior();

    @Override
    public @NotNull ItemStack execute(BlockSource blockSource, @NotNull ItemStack itemStack) {
        Level level = blockSource.getLevel();
        Position position = DispenserBlock.getDispensePosition(blockSource);
        Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);
        Projectile projectile = this.getProjectile(level, position, itemStack);
        projectile.shoot(direction.getStepX(), (float)direction.getStepY() + 0.1F, direction.getStepZ(), this.getPower(), this.getUncertainty());
        level.addFreshEntity(projectile);
        ToolStack tool = ToolStack.from(itemStack);
        ToolDamageUtil.damage(tool, 1, null, itemStack);
        tool.updateStack(itemStack);
        return itemStack;
    }

    @Override
    protected @NotNull Projectile getProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack itemStack) {
        FireworkRocketEntity rocket = new FireworkRocketEntity(level, position.x(), position.y(), position.z());
        rocket.onCreate(FireworkRocketItem.createFireworkRocket(ToolStack.from(itemStack)), null);
        return rocket;
    }
}
