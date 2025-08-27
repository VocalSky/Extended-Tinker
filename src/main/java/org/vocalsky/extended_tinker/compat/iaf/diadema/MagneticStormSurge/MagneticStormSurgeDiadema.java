package org.vocalsky.extended_tinker.compat.iaf.diadema.MagneticStormSurge;

import com.csdy.tcondiadema.diadema.api.ranges.SphereDiademaRange;
import com.csdy.tcondiadema.frames.diadema.Diadema;
import com.csdy.tcondiadema.frames.diadema.DiademaType;
import com.csdy.tcondiadema.frames.diadema.movement.DiademaMovement;
import com.csdy.tcondiadema.frames.diadema.range.DiademaRange;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import lombok.NonNull;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MagneticStormSurgeDiadema extends Diadema {
    private static final double radius = 6;
    private final SphereDiademaRange range = new SphereDiademaRange(this, radius);

    public MagneticStormSurgeDiadema(DiademaType type, DiademaMovement movement) {
        super(type, movement);
    }

    @Override
    public @NonNull DiademaRange getRange() {
        return range;
    }

    @Override
    protected void perTick() {
        var self = getCoreEntity();
        if (!(self.tickCount % (6.6 * 20) == 0)) return;

        Vec3 pos = getPosition();
        ServerLevel level = getLevel();

        if (level.isClientSide()) return;
        if (!(self instanceof LivingEntity)) return;

        for (Monster monster : listOfMonster(level, pos)) {
            if (!self.level().isClientSide) {
                LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(monster.level());
                lightningboltentity.getTags().add(ServerEvents.BOLT_DONT_DESTROY_LOOT);
                lightningboltentity.getTags().add(self.getStringUUID());
                lightningboltentity.moveTo(monster.position());
                if (!monster.level().isClientSide) {
                    monster.level().addFreshEntity(lightningboltentity);
                }
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
