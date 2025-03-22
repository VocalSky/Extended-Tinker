package org.vocalsky.extended_tinker.common.entity.damageSources;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class firecrackDamageSource extends DamageSource {
    private final Entity entity;
    private final Entity owner;

    public firecrackDamageSource(Entity entity, Entity owner) {
        super("firecrack");
        this.entity = entity;
        this.owner = owner;
    }
}
