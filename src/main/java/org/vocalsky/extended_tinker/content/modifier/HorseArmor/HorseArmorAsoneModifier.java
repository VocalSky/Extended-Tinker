package org.vocalsky.extended_tinker.content.modifier.HorseArmor;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;

import java.util.stream.Stream;

public class HorseArmorAsoneModifier extends Modifier {
    private static final TinkerDataCapability.TinkerDataKey<Integer> ASONE = TConstruct.createKey("asone_horsearmor");

    public HorseArmorAsoneModifier() {
        super();
        MinecraftForge.EVENT_BUS.addListener(HorseArmorAsoneModifier::onHurtSelf);
        MinecraftForge.EVENT_BUS.addListener(HorseArmorAsoneModifier::onHurtPassenger);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addModule(new ArmorLevelModule(ASONE, false, null));
    }

    private static void onHurtSelf(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.isSpectator()) {
            EquipmentContext context = new EquipmentContext(living);
            if (context.hasModifiableArmor()) {
                if (!living.level.isClientSide && living.isAlive()) {
                    living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                        if (holder.get(ASONE, 0) > 0) {
                            if (living.isVehicle()) {
                                DamageSource damageSource = event.getSource();
                                float amount = event.getAmount();
                                Stream<Entity> selfAndPassengers = living.getSelfAndPassengers();
                                Entity[] entities = selfAndPassengers.toArray(Entity[]::new);
                                float allHealth = 0;
                                for(Entity entity : entities) allHealth += ((LivingEntity)entity).getHealth();
                                float restAmount = 0;
                                for(Entity entity : entities) {
                                    float nowAmount = amount * ((LivingEntity) entity).getHealth() / allHealth + restAmount;
                                    if (nowAmount > ((LivingEntity) entity).getHealth()) {
                                        restAmount += nowAmount - (((LivingEntity) entity).getHealth() - 1);
                                        nowAmount = ((LivingEntity) entity).getHealth() - 1;
                                    }
                                    if (entity == living) {
                                        restAmount += nowAmount;
                                        continue;
                                    }
                                    entity.hurt(damageSource, nowAmount);
                                }
                                if (restAmount != 0) {
                                    living.invulnerableTime = 0;
                                    event.setAmount(restAmount);
                                } else event.setCanceled(true);
                            }
                        }
                    });
                }
            }
        }
    }

    private static void onHurtPassenger(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (!living.isSpectator()) {
            LivingEntity vehicle = (LivingEntity) living.getVehicle();
            if (vehicle == null) return;
            EquipmentContext context = new EquipmentContext(vehicle);
            if (context.hasModifiableArmor()) {
                if (!vehicle.level.isClientSide && vehicle.isAlive()) {
                    vehicle.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                        if (holder.get(ASONE, 0) > 0) {
                            event.setCanceled(true);
                            vehicle.hurt(event.getSource(), event.getAmount());
                        }
                    });
                }
            }
        }
    }
}