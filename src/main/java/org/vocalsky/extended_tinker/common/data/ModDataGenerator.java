package org.vocalsky.extended_tinker.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.data.Provider.ModifierTagProvider;
import org.vocalsky.extended_tinker.common.data.Provider.StationSlotLayoutProvider;
import org.vocalsky.extended_tinker.common.data.Provider.ToolDefinitionDataProvider;
import org.vocalsky.extended_tinker.common.data.Provider.ToolRecipeProvider;

@Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        boolean server = event.includeServer();
        generator.addProvider(server, new ToolRecipeProvider(generator));
        generator.addProvider(server, new ToolDefinitionDataProvider(generator));
        generator.addProvider(server, new StationSlotLayoutProvider(generator));
        generator.addProvider(server, new ModifierTagProvider(generator, event.getExistingFileHelper()));
    }
}