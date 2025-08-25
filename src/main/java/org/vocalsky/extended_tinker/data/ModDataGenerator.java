package org.vocalsky.extended_tinker.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.data.Provider.*;
import org.vocalsky.extended_tinker.data.Provider.LangData.ILang;
import org.vocalsky.extended_tinker.data.Provider.LangData.en_us;
import org.vocalsky.extended_tinker.data.Provider.LangData.zh_cn;
import org.vocalsky.extended_tinker.data.Provider.LangData.zh_tw;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Extended_tinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        boolean server = event.includeServer();
        boolean client = event.includeClient();
        BlockTagProvider blockTags = new BlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new ItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(server, new ToolRecipeProvider(packOutput));
        generator.addProvider(server, new ToolDefinitionDataProvider(packOutput));
        generator.addProvider(server, new StationSlotLayoutProvider(packOutput));
        generator.addProvider(server, new ModifierProvider(packOutput));
        generator.addProvider(server, new ModifierRecipeProvider(packOutput));
        generator.addProvider(server, new ModifierTagProvider(packOutput, existingFileHelper));
        generator.addProvider(client, new ModItemModelProvider(packOutput, existingFileHelper));
        MaterialDataProvider materials = new MaterialDataProvider(packOutput);
        generator.addProvider(server, materials);
        generator.addProvider(server, new MaterialStatsDataProvider(packOutput, materials));
        generator.addProvider(server, new MaterialTraitsDataProvider(packOutput, materials));
        generator.addProvider(client, new LangProvider(packOutput, new en_us()));
        generator.addProvider(client, new LangProvider(packOutput, new zh_cn()));
        generator.addProvider(client, new LangProvider(packOutput, new zh_tw()));
    }
}