package org.vocalsky.extended_tinker.mixin.iaf;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vocalsky.extended_tinker.common.tool.HorseArmorItem;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import static com.github.alexthe666.iceandfire.entity.EntityHippocampus.INV_SLOT_ARMOR;

@Mixin(EntityHippocampus.class)
public abstract class EntityHippocampusMixin extends TamableAnimal {
    @Shadow(remap = false) public SimpleContainer inventory;
    @Final @Shadow(remap = false) private static EntityDataAccessor<Integer> ARMOR;

    protected EntityHippocampusMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getIntFromArmor", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getIntFromArmorMixin(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!stack.isEmpty() && stack.getItem() instanceof HorseArmorItem) cir.setReturnValue(4);
    }

    @Inject(method = "setArmor", at = @At("HEAD"), cancellable = true, remap = false)
    private void setArmorMixin(int armorType, CallbackInfo cir) {
        if (armorType == 4) {
            this.entityData.set(ARMOR, armorType);
            ItemStack itemStack = this.inventory.getItem(INV_SLOT_ARMOR);
            this.setItemSlot(EquipmentSlot.CHEST, itemStack);
            cir.cancel();
        }
    }

    @Override
    protected void hurtArmor(@NotNull DamageSource damageSource, float damage) {
        if (!(damage <= 0.0F)) {
            damage /= 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }
            ItemStack stack = this.inventory.getItem(INV_SLOT_ARMOR);
            if ((!damageSource.is(DamageTypeTags.IS_FIRE) || !stack.getItem().isFireResistant()) && stack.getItem() instanceof HorseArmorItem) {
                ToolStack tool = ToolStack.from(stack);
                ToolDamageUtil.damage(tool, (int)damage, this, stack);
                tool.updateStack(stack);
            }
        }
    }
}