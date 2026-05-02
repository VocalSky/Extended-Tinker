package org.vocalsky.extended_tinker.mixin.iaf;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.IPassabilityNavigator;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.ICustomSizeNavigator;
import com.google.common.collect.Multimap;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.compat.iaf.tool.DragonArmorItem;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Objects;

@Mixin(EntityDragonBase.class)
public abstract class EntityDragonBaseMixin extends TamableAnimal implements IPassabilityNavigator, ISyncMount, IFlyingMount, IMultipartEntity, IAnimatedEntity, IDragonFlute, IDeadMob, IVillagerFear, IAnimalFear, IDropArmor, IHasCustomizableAttributes, ICustomSizeNavigator, ICustomMoveController, ContainerListener {
    @Shadow
    public abstract @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slotIn);

    @Shadow
    public abstract int getArmorOrdinal(ItemStack stack);

    protected EntityDragonBaseMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
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
                    if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof DragonArmorItem) {
                        itemstack.hurtAndBreak((int)damage, this, (entity) -> entity.broadcastBreakEvent(slot));
                    }
                }
            }
        }
    }
}
