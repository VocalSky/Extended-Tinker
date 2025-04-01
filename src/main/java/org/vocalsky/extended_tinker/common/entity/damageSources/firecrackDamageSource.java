package org.vocalsky.extended_tinker.common.entity.damageSources;

import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

public class firecrackDamageSource extends IndirectEntityDamageSource {
    public firecrackDamageSource(Entity entity, Entity owner) {
        super("firecrack", entity, owner);
        this.setExplosion();
    }
}
