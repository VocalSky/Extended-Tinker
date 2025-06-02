package org.vocalsky.extended_tinker.mixin.golem;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemArmorItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsMenu;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vocalsky.extended_tinker.compat.golem.tool.GolemArmor;

import java.util.function.Function;

@Mixin(EquipmentsMenu.class)
public class EquipmentsMenuMixin extends BaseContainerMenu<EquipmentsMenu>  {
    @Shadow @Final public AbstractGolemEntity<?, ?> golem;

    protected EquipmentsMenuMixin(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, Function<EquipmentsMenu, SimpleContainer> factory, boolean isVirtual) {
        super(type, wid, plInv, manager, factory, isVirtual);
    }

    @Inject(method = "getSlotForItem", at = @At(value = "TAIL"), cancellable = true, remap = false)
    public void getSlotForItemMixin(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if (golem instanceof MetalGolemEntity) {
            if (stack.getItem() instanceof GolemArmor armor) {
                cir.setReturnValue(armor.getSlot());
            }
        }
    }
}
