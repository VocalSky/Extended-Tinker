package org.vocalsky.extended_tinker.common.data.Provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static slimeknights.tconstruct.common.TinkerTags.Items.*;

public class ItemTagProvider extends ItemTagsProvider {

    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.addTools();
        this.addSmeltry();
    }

    private void addTools() {
        addToolTags(ModItems.Tools.HORSE_ARMOR, CHESTPLATES, BONUS_SLOTS, DURABILITY, LOOT_CAPABLE_TOOL, MULTIPART_TOOL, BOOK_ARMOR);
        addToolTags(ModItems.Tools.FIRECRACK, BONUS_SLOTS, DURABILITY, MULTIPART_TOOL, SMALL_TOOLS, AOE, INTERACTABLE_LEFT, INTERACTABLE_RIGHT, SPECIAL_TOOLS);

        this.tag(TOOL_PARTS).replace(false).add(ModItems.Parts.BRIDLE.get());

//        this.tag(BOOK_ARMOR).replace(false).add(ModItems.Tools.HORSE_ARMOR.get());
    }

    private void addSmeltry() {
        // tag each type of cast
        IntrinsicTagAppender<Item> goldCasts = this.tag(TinkerTags.Items.GOLD_CASTS);
        IntrinsicTagAppender<Item> sandCasts = this.tag(TinkerTags.Items.SAND_CASTS);
        IntrinsicTagAppender<Item> redSandCasts = this.tag(TinkerTags.Items.RED_SAND_CASTS);
        IntrinsicTagAppender<Item> singleUseCasts = this.tag(TinkerTags.Items.SINGLE_USE_CASTS);
        IntrinsicTagAppender<Item> multiUseCasts = this.tag(TinkerTags.Items.MULTI_USE_CASTS);
        Consumer<CastItemObject> addCast = cast -> {
            // tag based on material
            goldCasts.add(cast.get());
            sandCasts.add(cast.getSand());
            redSandCasts.add(cast.getRedSand());
            // tag based on usage
            singleUseCasts.addTag(cast.getSingleUseTag());
            this.tag(cast.getSingleUseTag()).add(cast.getSand(), cast.getRedSand());
            multiUseCasts.addTag(cast.getMultiUseTag());
            this.tag(cast.getMultiUseTag()).add(cast.get());
        };
        addCast.accept(ModItems.Casts.BRIDLE_CAST);
    }

    @SafeVarargs
    private void addToolTags(ItemLike tool, TagKey<Item>... tags) {
        Item item = tool.asItem();
        for (TagKey<Item> tag : tags) {
            this.tag(tag).replace(false).add(item);
        }
    }
}
