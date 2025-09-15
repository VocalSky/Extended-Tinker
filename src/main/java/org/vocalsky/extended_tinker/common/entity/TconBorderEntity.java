package org.vocalsky.extended_tinker.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
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

//    private void doPlayerRide(@NotNull Player player) {
//        if (!this.level().isClientSide) {
//            player.setYRot(this.getYRot());
//            player.setXRot(this.getXRot());
//            player.startRiding(this);
////            this.addPassenger(player);
//        }
//    }

//    @Override
//    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
//        return super.interact(player, hand);
//////        System.out.println("interacting by " + player);
////        boolean flag = player.isSecondaryUseActive();
//////        System.out.println("interact type  " + this.isVehicle() + " " + player.isSecondaryUseActive());
////        if (!this.isVehicle() && !flag) {
////            doPlayerRide(player);
////            return InteractionResult.sidedSuccess(this.level().isClientSide);
////        } else {
////            return super.interact(player, hand);
////        }
//    }
}
