package org.vocalsky.extended_tinker.compat.iaf.diadema.Permafrost;

import com.csdy.tcondiadema.diadema.api.ranges.SphereDiademaRange;
import com.csdy.tcondiadema.frames.diadema.Diadema;
import com.csdy.tcondiadema.frames.diadema.DiademaType;
import com.csdy.tcondiadema.frames.diadema.movement.DiademaMovement;
import com.csdy.tcondiadema.frames.diadema.range.DiademaRange;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import lombok.NonNull;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PermaforstDiadema extends Diadema {
    private static final double radius = 10;
    private final SphereDiademaRange range = new SphereDiademaRange(this, radius);

    public PermaforstDiadema(DiademaType type, DiademaMovement movement) {
        super(type, movement);
    }

    @Override
    public @NonNull DiademaRange getRange() {
        return range;
    }

    @Override
    protected void perTick() {
        var self = getCoreEntity();
        if (!(self.tickCount % (10 * 20) == 0)) return;

        Vec3 pos = getPosition();
        ServerLevel level = getLevel();

        if (level.isClientSide()) return;
        if (!(self instanceof LivingEntity)) return;

        for (Monster monster : listOfMonster(level, pos)) {
            if (!self.level().isClientSide) {
                EntityDataProvider.getCapability(monster).ifPresent((data) -> data.frozenData.setFrozen(monster, 100));
                monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                monster.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
            }
        }
    }

    private List<Monster> listOfMonster(Level level, Vec3 pos) {
        AABB area = new AABB(
                pos.x - radius, pos.y - radius, pos.z - radius,
                pos.x + radius, pos.y + radius, pos.z + radius
        );
        return level.getEntitiesOfClass(
                Monster.class,
                area,
                e -> e.isAlive() && !e.isRemoved()
        );
    }
}
