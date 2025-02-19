package org.vocalsky.extended_tinker.content.modifiers.HorseArmor;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.vocalsky.extended_tinker.access.LivingHurtEventMixinInterface;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
        if (((LivingHurtEventMixinInterface)event).extended_tinker$getPassedAsOne()) return;
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
                                System.out.print("ALL AMOUNT ");
                                System.out.println(amount);
                                for(Entity entity : entities) {
                                    float nowAmount = amount * ((LivingEntity) entity).getHealth() / allHealth + restAmount;
                                    if (nowAmount > ((LivingEntity) entity).getHealth()) {
                                        restAmount += nowAmount - (((LivingEntity) entity).getHealth() - 1);
                                        nowAmount = ((LivingEntity) entity).getHealth() - 1;
                                    }
                                    LivingHurtEvent nowEvent = new LivingHurtEvent((LivingEntity) entity, damageSource, nowAmount);
                                    ((LivingHurtEventMixinInterface)nowEvent).extended_tinker$setPassedAsOne(true);
                                    System.out.print("GETHURT ");
                                    System.out.print(nowEvent.getEntity());
                                    System.out.print(" with(base) ");
                                    System.out.print(nowEvent.getAmount());
                                    boolean hurt = MinecraftForge.EVENT_BUS.post(nowEvent);
                                    System.out.print(" + ");
                                    System.out.print(hurt);
                                    System.out.print(" - ");
                                    System.out.print(nowEvent.getAmount());
                                    System.out.print(" : ");
                                    System.out.println(hurt ? 0.0f : nowEvent.getAmount());
                                    entity.hurt(damageSource, hurt ? 0.0f : nowEvent.getAmount());
                                }
                                if (restAmount != 0) {
                                    LivingHurtEvent nowEvent = new LivingHurtEvent(living, damageSource, restAmount);
                                    ((LivingHurtEventMixinInterface)nowEvent).extended_tinker$setPassedAsOne(true);
                                    System.out.print("GETHURT ");
                                    System.out.print(nowEvent.getEntity());
                                    System.out.print(" with(rest) ");
                                    System.out.println(nowEvent.getAmount());
                                    living.hurt(damageSource, MinecraftForge.EVENT_BUS.post(nowEvent) ? 0.0f : nowEvent.getAmount());
                                }
                                event.setCanceled(true);
                            }
                        }
                    });
                }
            }
        }
    }
}