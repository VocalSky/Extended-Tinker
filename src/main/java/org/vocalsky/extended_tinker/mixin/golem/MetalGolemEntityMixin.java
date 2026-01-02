package org.vocalsky.extended_tinker.mixin.golem;

import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.weapon.GolemWeaponRegistry;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.vocalsky.extended_tinker.compat.golem.tool.GolemArmorItem;

@Mixin(MetalGolemEntity.class)
public abstract class MetalGolemEntityMixin extends SweepGolemEntity<MetalGolemEntity, MetalGolemPartType> {
    protected MetalGolemEntityMixin(GolemWeaponRegistry<MetalGolemEntity> reg, EntityType<MetalGolemEntity> type, Level level) {
        super(reg, type, level);
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damage) {
        if (!(damage <= 0.0F)) {
            damage /= 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }

            for(EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(slot);
                    if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof GolemArmorItem) {
                        itemstack.hurtAndBreak((int)damage, this, (entity) -> entity.broadcastBreakEvent(slot));
                    }
                }
            }
        }
    }
}
