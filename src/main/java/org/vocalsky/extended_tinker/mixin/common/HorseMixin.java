package org.vocalsky.extended_tinker.mixin.common;

import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.tool.HorseArmor;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.vocalsky.extended_tinker.common.tool.HorseArmor.HorseArmorUUID;

@Mixin(Horse.class)
public abstract class HorseMixin extends AbstractHorse {
    @Shadow @Final private static UUID ARMOR_MODIFIER_UUID;

    @Shadow public abstract ItemStack getArmor();

    protected HorseMixin(EntityType<? extends AbstractHorse> p_30531_, Level p_30532_) {
        super(p_30531_, p_30532_);
    }

    @Inject(method = "setArmorEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Horse;isArmor(Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BY, by = 1))
    public void setArmorEquipmentMixin(@NotNull ItemStack itemStack, CallbackInfo ci) {
        if (this.level().isClientSide()) return;
        if (itemStack.getItem() instanceof HorseArmor) {
            Multimap<Attribute, AttributeModifier> builder = itemStack.getItem().getAttributeModifiers(EquipmentSlot.CHEST, itemStack);
            builder.forEach(((attribute, attributeModifier) -> {
                Objects.requireNonNull(getAttribute(attribute)).removeModifier(HorseArmorUUID);
                Objects.requireNonNull(getAttribute(attribute)).addTransientModifier(attributeModifier);
            }));
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
            if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof HorseArmor) {
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
