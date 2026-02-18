package org.vocalsky.extended_tinker.mixin.golem;

import dev.xkmc.modulargolems.content.entity.common.SweepGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.weapon.GolemWeaponRegistry;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemPartType;
import dev.xkmc.modulargolems.content.entity.mode.GolemMode;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vocalsky.extended_tinker.compat.golem.GolemCore;
import org.vocalsky.extended_tinker.compat.golem.tool.GolemArmorItem;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.tools.Tool;

@Mixin(MetalGolemEntity.class)
public abstract class MetalGolemEntityMixin extends SweepGolemEntity<MetalGolemEntity, MetalGolemPartType> {
    protected MetalGolemEntityMixin(GolemWeaponRegistry<MetalGolemEntity> reg, EntityType<MetalGolemEntity> type, Level level) {
        super(reg, type, level);
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damage) {
        super.hurtArmor(damageSource, damage);
        if (!(damage <= 0.0F)) {
            damage /= 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }
            for(EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack stack = this.getItemBySlot(slot);
                    if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !stack.getItem().isFireResistant()) && stack.getItem() instanceof GolemArmorItem) {
                        ToolStack tool = ToolStack.from(stack);
                        ToolDamageUtil.damage(tool, (int)damage, this, stack);
                        tool.updateStack(stack);
                    }
                }
            }
        }
    }

    @Inject(method = "mobInteractImpl", at = @At("HEAD"), cancellable = true, remap = false)
    public void mobInteractImplMixin(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof ModifiableItem)) return;
        ToolStack tool = ToolStack.from(stack);
        if (tool.getModifierLevel(GolemCore.Modifiers.Ids.golem_weapon) == 0) return;
        if (!this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return;
        this.setItemSlot(EquipmentSlot.MAINHAND, stack.split(1));
        cir.setReturnValue(InteractionResult.CONSUME);
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"), remap = false)
    public void doHurtTargetMixin(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return;
        ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!(stack.getItem() instanceof ModifiableItem)) return;
        ToolStack tool = ToolStack.from(stack);
        ToolDamageUtil.damage(tool, 1, this, stack);
        tool.updateStack(stack);
    }
}
