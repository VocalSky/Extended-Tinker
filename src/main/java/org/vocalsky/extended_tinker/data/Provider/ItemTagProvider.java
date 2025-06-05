package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import org.vocalsky.extended_tinker.compat.golem.GolemItems;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

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

    private static ResourceLocation id(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private void addTools() {
        // common
        addToolTags(ModItems.Tools.HORSE_ARMOR, CHESTPLATES, BONUS_SLOTS, DURABILITY, LOOT_CAPABLE_TOOL, MULTIPART_TOOL, BOOK_ARMOR);
        addToolTags(ModItems.Tools.FIRECRACK, BONUS_SLOTS, DURABILITY, MULTIPART_TOOL, SMALL_TOOLS, AOE, INTERACTABLE_LEFT, INTERACTABLE_RIGHT, SPECIAL_TOOLS);
        this.tag(TOOL_PARTS).replace(false).addOptionalTag(ModItems.Parts.BRIDLE.getId());
        GolemItems.Parts.GOLEM_PLATING.forEach((slot, item) -> {
            this.tag(TOOL_PARTS).replace(false).addOptionalTag(id(item));
        });

        // golems
        addArmorTags(GolemItems.Tools.GOLEM_ARMOR, BONUS_SLOTS, DURABILITY, LOOT_CAPABLE_TOOL, MULTIPART_TOOL, BOOK_ARMOR);
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
            goldCasts.addOptionalTag(cast.getId());
            sandCasts.addOptionalTag(id(cast.getSand()));
            redSandCasts.addOptionalTag(id(cast.getRedSand()));
            // tag based on usage
            singleUseCasts.addOptionalTag(cast.getSingleUseTag().location());
            this.tag(cast.getSingleUseTag()).addOptionalTag(id(cast.getSand())).addOptionalTag(id(cast.getRedSand()));
            multiUseCasts.addOptionalTag(cast.getMultiUseTag().location());
            this.tag(cast.getMultiUseTag()).addOptionalTag(id(cast.get()));
        };
        addCast.accept(ModItems.Casts.BRIDLE_CAST);

        GolemItems.Casts.GOLEM_PLATING_CAST.forEach((slot, item) -> {
            if (slot == ArmorItem.Type.BOOTS) return;
            addCast.accept(item);
        });
        for (Item item : GolemItems.Parts.DUMMY_GOLEM_PLATING.values().toArray(new Item[0]))
            this.tag(TinkerTags.Items.CHEST_PARTS).addOptionalTag(id(item));
    }

    @SafeVarargs
    private void addToolTags(ItemLike tool, TagKey<Item>... tags) {
        Item item = tool.asItem();
        for (TagKey<Item> tag : tags) {
            this.tag(tag).replace(false).addOptionalTag(id(item));
        }
    }

    private TagKey<Item> getArmorTag(ArmorItem.Type slotType) {
        return switch (slotType) {
            case BOOTS -> BOOTS;
            case LEGGINGS -> LEGGINGS;
            case CHESTPLATE -> CHESTPLATES;
            case HELMET -> HELMETS;
        };
    }

    private TagKey<Item> getForgeArmorTag(ArmorItem.Type slotType) {
        return switch (slotType) {
            case BOOTS -> Tags.Items.ARMORS_BOOTS;
            case LEGGINGS -> Tags.Items.ARMORS_LEGGINGS;
            case CHESTPLATE -> Tags.Items.ARMORS_CHESTPLATES;
            case HELMET -> Tags.Items.ARMORS_HELMETS;
        };
    }

    @SafeVarargs
    private void addArmorTags(EnumObject<ArmorItem.Type,? extends Item> armor, TagKey<Item>... tags) {
        armor.forEach((type, item) -> {
            for (TagKey<Item> tag : tags) {
                this.tag(tag).addOptionalTag(id(item));
            }
            this.tag(getArmorTag(type)).addOptionalTag(id(item));
            this.tag(getForgeArmorTag(type)).addOptionalTag(id(item));
        });
    }
}
