package org.vocalsky.extended_tinker.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.vocalsky.extended_tinker.access.LivingHurtEventMixinInterface;

@Mixin(LivingHurtEvent.class)
public abstract class LivingHurtEventMixin extends LivingEvent implements LivingHurtEventMixinInterface {
    public LivingHurtEventMixin(LivingEntity entity) {
        super(entity);
    }

    @Unique
    private boolean extended_tinker$passedAsOne = false;

    @Override
    public void extended_tinker$setPassedAsOne(boolean passedAsOne) {
        this.extended_tinker$passedAsOne = passedAsOne;
    }

    @Override
    public boolean extended_tinker$getPassedAsOne() {
        return this.extended_tinker$passedAsOne;
    }
}
