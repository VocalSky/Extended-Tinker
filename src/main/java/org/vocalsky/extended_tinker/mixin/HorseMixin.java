package org.vocalsky.extended_tinker.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Objects;
import java.util.UUID;

@Mixin(Horse.class)
public abstract class HorseMixin extends AbstractHorse {
    @Shadow @Final private static UUID ARMOR_MODIFIER_UUID;

    @Shadow public abstract ItemStack getArmor();

    protected HorseMixin(EntityType<? extends AbstractHorse> p_30531_, Level p_30532_) {
        super(p_30531_, p_30532_);
    }

    @Inject(method = "setArmorEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Horse;isArmor(Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BY, by = 1))
    public void setArmorEquipmentMixin(@NotNull ItemStack itemStack, CallbackInfo ci) {
        if (itemStack.getItem() instanceof HorseArmor) {
            float armor_toughness = ((HorseArmor)itemStack.getItem()).getToughness();
            if (armor_toughness != 0) Objects.requireNonNull(getAttribute(Attributes.ARMOR_TOUGHNESS)).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", armor_toughness, AttributeModifier.Operation.ADDITION));
            float knockback_resistance = ((HorseArmor)itemStack.getItem()).getKnockbackResistance();
            if (knockback_resistance != 0) Objects.requireNonNull(getAttribute(Attributes.KNOCKBACK_RESISTANCE)).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", armor_toughness, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damage) {
        if (!(damage <= 0.0F)) {
            damage /= 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }

            ItemStack itemstack = this.getArmor();
            if ((!damageSource.isFire() || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof HorseArmor) {
                itemstack.hurtAndBreak((int)damage, (AbstractHorse)this, (abstractHorse) -> abstractHorse.broadcastBreakEvent(EquipmentSlot.CHEST));
            }
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    public void getHurtSoundMixin(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (getArmor().getItem() instanceof HorseArmor)
            if(isVehicle() && ToolStack.from(getArmor()).getModifierLevel(ModModifiers.PAINLESS.get()) > 0)
                cir.setReturnValue(SoundEvents.HORSE_HURT);
    }
}
