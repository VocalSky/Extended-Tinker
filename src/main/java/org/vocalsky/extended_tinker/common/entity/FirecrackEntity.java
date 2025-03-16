package org.vocalsky.extended_tinker.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FirecrackEntity extends FireworkRocketEntity {
    public FirecrackEntity(EntityType<? extends FireworkRocketEntity> p_37027_, Level p_37028_) {
        super(p_37027_, p_37028_);
    }

    public FirecrackEntity(Level p_37030_, double p_37031_, double p_37032_, double p_37033_, ItemStack p_37034_) {
        super(p_37030_, p_37031_, p_37032_, p_37033_, p_37034_);
    }

    public FirecrackEntity(Level p_37036_, @Nullable Entity p_37037_, double p_37038_, double p_37039_, double p_37040_, ItemStack p_37041_) {
        super(p_37036_, p_37037_, p_37038_, p_37039_, p_37040_, p_37041_);
    }

    public FirecrackEntity(Level p_37058_, ItemStack p_37059_, LivingEntity p_37060_) {
        super(p_37058_, p_37059_, p_37060_);
    }

    public FirecrackEntity(Level p_37043_, ItemStack p_37044_, double p_37045_, double p_37046_, double p_37047_, boolean p_37048_) {
        super(p_37043_, p_37044_, p_37045_, p_37046_, p_37047_, p_37048_);
    }

    public FirecrackEntity(Level p_37050_, ItemStack p_37051_, Entity p_37052_, double p_37053_, double p_37054_, double p_37055_, boolean p_37056_) {
        super(p_37050_, p_37051_, p_37052_, p_37053_, p_37054_, p_37055_, p_37056_);
    }
}
