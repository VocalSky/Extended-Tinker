package org.vocalsky.extended_tinker.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.entity.FirecrackEntity;
import org.vocalsky.extended_tinker.common.entity.TconBorderEntity;

public class ModEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static RegistryObject<EntityType<FirecrackEntity>> firecrackEntity;
    public static RegistryObject<EntityType<TconBorderEntity>> tconBoatEntity;

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Extended_tinker.MODID);
        firecrackEntity = ENTITY_TYPES.register("firecrack", () -> EntityType.Builder.<FirecrackEntity>of((entityType, level) -> new FirecrackEntity(entityType, level, new ItemStack(ModCore.Tools.FIRECRACK.get())), MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).setCustomClientFactory((spawnEntity, world) -> new FirecrackEntity(firecrackEntity.get(), world, new ItemStack(ModCore.Tools.FIRECRACK.get()))).setShouldReceiveVelocityUpdates(true).build(Extended_tinker.getResource("firecrack").toString()));
        tconBoatEntity =
                ENTITY_TYPES.register("tcon_border",
                        () -> EntityType.Builder.<TconBorderEntity>of(
                                TconBorderEntity::new,
                                MobCategory.MISC)
                                .sized(3, 2)
                                .build(Extended_tinker.getResource("tcon_border").toString()));
    }

    public static void registers(IEventBus eventBus)  {
        ENTITY_TYPES.register(eventBus);
    }
}
