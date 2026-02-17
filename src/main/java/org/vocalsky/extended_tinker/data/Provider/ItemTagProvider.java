package org.vocalsky.extended_tinker.data.Provider;

import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.compat.golem.GolemCore;
import org.vocalsky.extended_tinker.compat.iaf.IafCore;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static slimeknights.tconstruct.common.TinkerTags.Items.*;
import static org.vocalsky.extended_tinker.util.ETTagsUtil.*;

public class ItemTagProvider extends ItemTagsProvider {
    private final Function<Item, ResourceLocation> LocExtractor = (item) -> item.builtInRegistryHolder().key().location();

    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        this.addTools();
        this.addSmeltry();
    }

    private void addTools() {
        TagKey<Item>[] armorTags = new TagKey[]{BONUS_SLOTS, DURABILITY, LOOT_CAPABLE_TOOL, MULTIPART_TOOL, BOOK_ARMOR};

        // common
        addToolTags(ModCore.Tools.HORSE_ARMOR, CHESTPLATES, BONUS_SLOTS, DURABILITY, LOOT_CAPABLE_TOOL, MULTIPART_TOOL, BOOK_ARMOR);
        addToolTags(ModCore.Tools.FIRECRACK, BONUS_SLOTS, DURABILITY, MULTIPART_TOOL, SMALL_TOOLS, AOE, INTERACTABLE_LEFT, INTERACTABLE_RIGHT, SPECIAL_TOOLS);
        addToolTags(ModCore.Tools.FIREWORK_ROCKET, BONUS_SLOTS, DURABILITY, MULTIPART_TOOL, SMALL_TOOLS, AOE, INTERACTABLE_RIGHT, SPECIAL_TOOLS);
        this.tag(TOOL_PARTS).replace(false).addOptional(LocExtractor.apply(ModCore.Parts.BRIDLE.get()));

        this.tag(ModCore.Tags.FIREWORK_FLINT).replace(false).add(TinkerTools.flintAndBrick.get(), Items.FLINT_AND_STEEL);

        // golems
        addArmorTags(GolemCore.Tools.GOLEM_ARMOR, BONUS_SLOTS, DURABILITY, LOOT_CAPABLE_TOOL, MULTIPART_TOOL, BOOK_ARMOR);
        GolemCore.Parts.GOLEM_PLATING.forEach((slot, item) -> {
            this.tag(TOOL_PARTS).replace(false).addOptional(LocExtractor.apply(item));
        });

        // iaf
        IafCore.Tools.DRAGON_ARMOR.forEach((type, item) -> {
            for (TagKey<Item> tag : armorTags) this.tag(tag).addOptional(LocExtractor.apply(item));
            this.tag(getArmorTag(type.ArmorType())).addOptional(LocExtractor.apply(item));
            this.tag(getForgeArmorTag(type.ArmorType())).addOptional(LocExtractor.apply(item));
            this.tag(DRAGON_ARMOR).addOptional(LocExtractor.apply(item));
        });
        IafCore.Parts.DRAGON_ARMOR_CORE.forEach((type, item) -> {
            this.tag(TOOL_PARTS).replace(false).addOptional(LocExtractor.apply(item));
        });
    }

    private void addSmeltry() {
        // tag each type of cast
        IntrinsicTagAppender<Item> goldCasts = this.tag(TinkerTags.Items.GOLD_CASTS);
        IntrinsicTagAppender<Item> sandCasts = this.tag(TinkerTags.Items.SAND_CASTS);
        IntrinsicTagAppender<Item> redSandCasts = this.tag(TinkerTags.Items.RED_SAND_CASTS);
        IntrinsicTagAppender<Item> singleUseCasts = this.tag(TinkerTags.Items.SINGLE_USE_CASTS);
        IntrinsicTagAppender<Item> multiUseCasts = this.tag(TinkerTags.Items.MULTI_USE_CASTS);
        Consumer<CastItemObject> addCast = cast -> {
            goldCasts.addOptional(LocExtractor.apply(cast.get()));
            sandCasts.addOptional(LocExtractor.apply(cast.getSand()));
            redSandCasts.addOptional(LocExtractor.apply(cast.getRedSand()));
            singleUseCasts.addOptionalTag(cast.getSingleUseTag().location());
            this.tag(cast.getSingleUseTag()).addOptional(LocExtractor.apply(cast.getSand()));
            this.tag(cast.getSingleUseTag()).addOptional(LocExtractor.apply(cast.getRedSand()));
            multiUseCasts.addOptionalTag(cast.getMultiUseTag().location());
            this.tag(cast.getMultiUseTag()).addOptional(LocExtractor.apply(cast.get()));
        };
        addCast.accept(ModCore.Casts.BRIDLE_CAST);

        GolemCore.Casts.GOLEM_PLATING_CAST.forEach((slot, item) -> {
            if (slot == ArmorItem.Type.BOOTS) return;
            addCast.accept(item);
        });
        this.tag(TinkerTags.Items.CHEST_PARTS).addOptionalTag(TinkerTags.Items.TOOL_PARTS.location());
        for (Item item : GolemCore.Parts.DUMMY_GOLEM_PLATING.values().toArray(new Item[0]))
            this.tag(TinkerTags.Items.CHEST_PARTS).addOptional(LocExtractor.apply(item));
    }

    @SafeVarargs
    private void addToolTags(ItemLike tool, TagKey<Item>... tags) {
        Item item = tool.asItem();
        for (TagKey<Item> tag : tags) {
            this.tag(tag).replace(false).addOptional(LocExtractor.apply(item));
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
                this.tag(tag).addOptional(LocExtractor.apply(item));
            }
            this.tag(getArmorTag(type)).addOptional(LocExtractor.apply(item));
            this.tag(getForgeArmorTag(type)).addOptional(LocExtractor.apply(item));
        });
    }
}
