package org.vocalsky.extended_tinker.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vocalsky.extended_tinker.content.tools.HorseArmor;

import java.util.UUID;
import java.util.function.Consumer;

@Mixin(Horse.class)
public abstract class ForHorseArmor extends AbstractHorse {
    @Shadow @Final private static UUID ARMOR_MODIFIER_UUID;

    @Shadow public abstract ItemStack getArmor();

    protected ForHorseArmor(EntityType<? extends AbstractHorse> p_30531_, Level p_30532_) {
        super(p_30531_, p_30532_);
    }

    @Inject(method = "setArmorEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Horse;isArmor(Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BY, by = 1))
    public void setArmorEquipmentMixin(@NotNull ItemStack itemStack, CallbackInfo ci) {
        if (itemStack.getItem() instanceof HorseArmor) {
            float armor_toughness = ((HorseArmor)itemStack.getItem()).getToughness();
            if (armor_toughness != 0) getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)armor_toughness, AttributeModifier.Operation.ADDITION));
            float knockback_resistance = ((HorseArmor)itemStack.getItem()).getKnockbackResistance();
            if (knockback_resistance != 0) getAttribute(Attributes.KNOCKBACK_RESISTANCE).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)armor_toughness, AttributeModifier.Operation.ADDITION));
        }
    }

    protected void hurtArmor(DamageSource p_21122_, float p_21123_) {
        if (!(p_21123_ <= 0.0F)) {
            p_21123_ /= 4.0F;
            if (p_21123_ < 1.0F) {
                p_21123_ = 1.0F;
            }

            ItemStack itemstack = this.getArmor();
            if ((!p_21122_.isFire() || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof HorseArmor) {
                itemstack.hurtAndBreak((int)p_21123_, (AbstractHorse)this, (p_35997_) -> p_35997_.broadcastBreakEvent(EquipmentSlot.CHEST));
            }
        }
    }

}
