package org.vocalsky.extended_tinker;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.vocalsky.extended_tinker.common.*;
import org.vocalsky.extended_tinker.network.PacketHandler;

@Mod(Extended_tinker.MODID)
public class Extended_tinker {

    public static final String MODID = "extended_tinker";

    public Extended_tinker() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModModifiers.registers(modEventBus);
        ModItems.registers(modEventBus);
        ModEntity.registers(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.Init();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    public static ResourceLocation getResource(String id) {
        return new ResourceLocation(MODID, id);
    }
}