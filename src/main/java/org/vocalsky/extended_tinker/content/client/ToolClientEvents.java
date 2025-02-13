package org.vocalsky.extended_tinker.content.client;

import net.minecraft.server.packs.PackType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.content.Items;
import slimeknights.mantle.data.listener.ISafeManagerReloadListener;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.client.model.DynamicTextureLoader;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;
import slimeknights.tconstruct.library.client.modifiers.ModifierModelManager;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

@Mod.EventBusSubscriber(modid = Extended_tinker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ToolClientEvents {
    private static final ISafeManagerReloadListener MODIFIER_RELOAD_LISTENER = manager -> ModifierManager.INSTANCE.getAllValues().forEach(modifier -> modifier.clearCache(PackType.CLIENT_RESOURCES));
    public ToolClientEvents() {
    }
    @SubscribeEvent
    static void addResourceListener(RegisterClientReloadListenersEvent manager) {
        ModifierModelManager.init(manager);
        MaterialTooltipCache.init(manager);
        DynamicTextureLoader.init(manager);
        manager.registerReloadListener(MODIFIER_RELOAD_LISTENER);
    }
    @SubscribeEvent
    static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TinkerItemProperties.registerToolProperties(Items.HORSE_ARMOR.get().asItem());
        });
    }
}