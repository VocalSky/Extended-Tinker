package org.vocalsky.extended_tinker.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.modifier.Firecrack.FireworkStarModifier;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.entity.ModifiableArrow;

public class FireworkRocketEntity extends ModifiableArrow {
    public FireworkRocketEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public FireworkRocketEntity(Level level, double pX, double pY, double pZ) {
        this(ModCore.Entities.fireworkRocketEntity.get(), level);
        this.setPos(pX, pY, pZ);
    }

    public FireworkRocketEntity(Level level, LivingEntity shooter) {
        this(level, shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
        this.setOwner(shooter);
    }

    @Override
    public void handleEntityEvent(byte eid) {
        if (eid == 17 && this.level().isClientSide) {
            ItemStack itemstack = this.getDisplayTool();
            CompoundTag compoundtag = FireworkStarModifier.getStar(ToolStack.from(itemstack));
            Vec3 vec3 = this.getDeltaMovement();
            this.level().createFireworks(this.getX(), this.getY(), this.getZ(), vec3.x, vec3.y, vec3.z, compoundtag);
        }
        super.handleEntityEvent(eid);
    }
}