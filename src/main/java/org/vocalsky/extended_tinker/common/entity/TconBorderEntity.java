package org.vocalsky.extended_tinker.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.common.ModEntity;


public class TconBorderEntity extends Mob {
    public TconBorderEntity(EntityType<? extends TconBorderEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1024.00)
                .add(Attributes.MOVEMENT_SPEED, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    public TconBorderEntity(Level level, double xo, double yo, double zo) {
        this(ModEntity.tconBoatEntity.get(), level);
        this.xo = xo;
        this.yo = yo;
        this.zo = zo;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    private void doPlayerRide(@NotNull Player player) {
        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
//        System.out.println("interacting by " + player);
        boolean flag = player.isSecondaryUseActive();
//        System.out.println("interact type  " + this.isVehicle() + " " + player.isSecondaryUseActive());
        if (!this.isVehicle() && !flag) {
            doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.getControllingPassenger() != null) {
            LivingEntity rider = this.getControllingPassenger();
            if (rider == null) {
                super.travel(pTravelVector);
            } else {
                if (!this.isInWater() && !this.isInLava()) {
                    double forward = (double)rider.zza;
                    double strafing = (double)(rider.xxa * 0.5F);
                    double vertical = pTravelVector.y;
                    float speed = (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    forward *= (double)speed;
                    forward *= rider.isSprinting() ? (double)1.2F : (double)1.0F;
                    forward *= rider.zza > 0.0F ? (double)1.0F : (double)0.2F;
                    if (this.isControlledByLocalInstance()) {
                        this.setSpeed(speed);
                        super.travel(new Vec3(strafing, vertical, forward));
                    } else {
                        this.setDeltaMovement(Vec3.ZERO);
                    }

                    this.tryCheckInsideBlocks();
                } else {
                    double forward = (double)rider.zza;
                    double strafing = (double)rider.xxa;
                    double vertical = (double)0.0F;
                    float speed = (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);

                    this.setSpeed(speed);
                    this.setZza((float)forward);
                    super.travel(pTravelVector.add(strafing, vertical, forward));
                }
            }
        } else {
            super.travel(pTravelVector);
        }
    }
}
