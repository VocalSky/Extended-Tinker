package org.vocalsky.extended_tinker.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.data.Provider.*;

@Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        boolean server = event.includeServer();
        boolean client = event.includeClient();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//        generator.addProvider(server, blockTags);
//        generator.addProvider(server, new ItemTagProvider(generator, blockTags, existingFileHelper));
//        generator.addProvider(server, new ToolRecipeProvider(generator));
//        generator.addProvider(server, new ToolDefinitionDataProvider(generator));
//        generator.addProvider(server, new StationSlotLayoutProvider(generator));
//        generator.addProvider(server, new ModifierProvider(generator));
//        generator.addProvider(server, new ModifierRecipeProvider(generator));
//        generator.addProvider(server, new ModifierTagProvider(generator, existingFileHelper));
//        generator.addProvider(client, new ModItemModelProvider(generator, existingFileHelper));
    }
}