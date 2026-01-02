package org.vocalsky.extended_tinker.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.entity.FirecrackEntity;
import org.vocalsky.extended_tinker.common.entity.FireworkRocket;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.tconstruct.tools.entity.ModifiableArrow;


@Mod.EventBusSubscriber(
        modid = Extended_tinker.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModEntity {
    public static final EntityTypeDeferredRegister ENTITYS;
    public static RegistryObject<EntityType<FirecrackEntity>> firecrackEntity;
    public static RegistryObject<EntityType<FireworkRocket>> fireworkRocketEntity;

    static {
        ENTITYS = new EntityTypeDeferredRegister(Extended_tinker.MODID);
        firecrackEntity = ENTITYS.register("firecrack", () -> EntityType.Builder.<FirecrackEntity>of((entityType, level) -> new FirecrackEntity(entityType, level, new ItemStack(ModCore.Tools.FIRECRACK.get())), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new FirecrackEntity(firecrackEntity.get(), world, new ItemStack(ModCore.Tools.FIRECRACK.get()))).setShouldReceiveVelocityUpdates(true));
        fireworkRocketEntity = ENTITYS.register("firework_rocket", () -> EntityType.Builder.<FireworkRocket>of(FireworkRocket::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
//        fireworkRocketEntity = ENTITY_TYPES.register("firework_rocket", () -> EntityType.Builder.<FireworkRocket>of((entityType, level) -> new FireworkRocket(entityType, level, new ItemStack(ModCore.Tools.FIREWORK_ROCKET.get())), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new FireworkRocket(fireworkRocketEntity.get(), world, new ItemStack(ModCore.Tools.FIREWORK_ROCKET.get()))).setShouldReceiveVelocityUpdates(true).build(Extended_tinker.getResource("firework_rocket").toString()));
    }
//    public static final RegistryObject<EntityType<ModifiableArrow>> materialArrow = ENTITYS.register("arrow", () -> EntityType.Builder.<ModifiableArrow>of(ModifiableArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));


    public static void registers(IEventBus eventBus)  {
        ENTITYS.register(eventBus);
    }

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent creationEvent) {
    }
}
