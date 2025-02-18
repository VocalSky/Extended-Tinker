package org.vocalsky.extended_tinker.content.modifiers.HorseArmor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.vocalsky.extended_tinker.content.tools.HorseArmor;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class HorseArmorAsoneModifier extends Modifier {
    private static final TinkerDataCapability.TinkerDataKey<Integer> ASONE = TConstruct.createKey("asone_horsearmor");

    public HorseArmorAsoneModifier() {
        super();
        MinecraftForge.EVENT_BUS.addListener(HorseArmorAsoneModifier::getHurt);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addModule(new ArmorLevelModule(ASONE, false, null));
    }

    private static void getHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.isSpectator()) {
            EquipmentContext context = new EquipmentContext(living);
            if (context.hasModifiableArmor()) {
                if (!living.level.isClientSide && living.isAlive() && living.tickCount % 8 == 0) {
                    living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                        if (holder.get(ASONE, 0) > 0)
                            if (living.isVehicle()) {
                                for(Entity entity : living.getIndirectPassengers())
                                    MinecraftForge.EVENT_BUS.post(new LivingHurtEvent((LivingEntity) entity, event.getSource(), event.getAmount()));
                                event.setCanceled(true);
                            }
                    });
                }

            }
        }
//        if (event.getEntity() instanceof Horse horse) {
//            if (horse.isWearingArmor() && horse.getArmor().getItem() instanceof HorseArmor) {
//                ToolStack armor = ToolStack.from(horse.getArmor());
//            }
//        }
    }
}