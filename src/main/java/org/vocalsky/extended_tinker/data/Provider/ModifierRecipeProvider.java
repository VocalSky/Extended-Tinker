package org.vocalsky.extended_tinker.data.Provider;

import com.csdy.tcondiadema.DiademaSlots;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.common.ModModifiers;
import org.vocalsky.extended_tinker.common.recipe.Builder.ToolExpExportRecipeBuilder;
import org.vocalsky.extended_tinker.common.recipe.FirecrackStarModifierRecipe;
import org.vocalsky.extended_tinker.compat.iaf.IafModifiers;
import org.vocalsky.extended_tinker.util.ETTagsUtil;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.mantle.recipe.helper.SimpleFinishedRecipe;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.function.Consumer;

public class ModifierRecipeProvider extends RecipeProvider  implements IRecipeHelper{
    public ModifierRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        addModifierRecipes(consumer);
    }

    private void addModifierRecipes(Consumer<FinishedRecipe> consumer) {
        String upgradeFolder = "tools/modifiers/upgrade/";
        String abilityFolder = "tools/modifiers/ability/";
        String slotlessFolder = "tools/modifiers/slotless/";
        String upgradeSalvage = "tools/modifiers/salvage/upgrade/";
        String abilitySalvage = "tools/modifiers/salvage/ability/";
        String defenseFolder = "tools/modifiers/defense/";
        String defenseSalvage = "tools/modifiers/salvage/defense/";
        String compatFolder = "tools/modifiers/compat/";
        String compatSalvage = "tools/modifiers/salvage/compat/";
        String worktableFolder = "tools/modifiers/worktable/";
        String diademaFolder = "tools/modifiers/diadema/";
        String diademaSalvage = "tools/modifiers/salvage/diadema/";

        ModifierRecipeBuilder.modifier(ModModifiers.ASONE.getId())
            .addInput(Items.GOLDEN_APPLE)
            .addInput(Items.ARMOR_STAND)
            .addInput(Items.GOLDEN_APPLE)
            .setSlots(SlotType.ABILITY, 1)
            .setTools(Ingredient.of(ModCore.Tools.HORSE_ARMOR))
            .setMaxLevel(1)
            .disallowCrystal()
            .saveSalvage(consumer, prefix(ModModifiers.ASONE.getId(), abilitySalvage))
            .save(consumer, prefix(ModModifiers.ASONE.getId(), abilityFolder));
        ModifierRecipeBuilder.modifier(ModModifiers.PAINLESS.getId())
            .addInput(TinkerTools.travelersShield)
            .addInput(Items.HAY_BLOCK)
            .addInput(TinkerTools.plateShield)
            .addInput(Items.LEAD)
            .addInput(ItemTags.MUSIC_DISCS)
            .setSlots(SlotType.ABILITY, 1)
            .setTools(Ingredient.of(ModCore.Tools.HORSE_ARMOR))
            .setMaxLevel(1)
            .disallowCrystal()
            .saveSalvage(consumer, prefix(ModModifiers.PAINLESS.getId(), abilitySalvage))
            .save(consumer, prefix(ModModifiers.PAINLESS.getId(), abilityFolder));
        ModifierRecipeBuilder.modifier(ModModifiers.FLIGHT.getId())
            .addInput(Tags.Items.GUNPOWDER, 8)
            .addInput(Tags.Items.GUNPOWDER, 8)
            .addInput(Tags.Items.GUNPOWDER, 8)
            .addInput(Items.PAPER, 4)
            .addInput(Items.PAPER, 4)
            .setMaxLevel(5)
            .setSlots(SlotType.UPGRADE, 1)
            .setTools(Ingredient.of(ModCore.Tools.FIRECRACK))
            .disallowCrystal()
            .saveSalvage(consumer, prefix(ModModifiers.FLIGHT.getId(), upgradeSalvage))
            .save(consumer, prefix(ModModifiers.FLIGHT.getId(), upgradeFolder));
        consumer.accept(new FirecrackStarModifierRecipe.Finished(Extended_tinker.getResource(abilityFolder + "firecrack_star"), Ingredient.of(ModCore.Tools.FIRECRACK), new SlotType.SlotCount(SlotType.ABILITY, 1)));
        consumer.accept(new FirecrackStarModifierRecipe.FinishedSalvage(Extended_tinker.getResource(abilitySalvage + "firecrack_star"), Ingredient.of(ModCore.Tools.FIRECRACK), new SlotType.SlotCount(SlotType.ABILITY, 1)));
        ModifierRecipeBuilder.modifier(IafModifiers.MagneticStormSurge.getId())
            .addInput(IafItemRegistry.DRAGON_SKULL_LIGHTNING.get(), 1)
            .addInput(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get(), 1)
            .addInput(IafItemRegistry.LIGHTNING_DRAGON_HEART.get(), 1)
            .disallowCrystal()
            .setSlots(DiademaSlots.DIADEMA, 1)
            .setMaxLevel(1)
            .setTools(ETTagsUtil.DRAGON_ARMOR)
            .saveSalvage(consumer, prefix(IafModifiers.MagneticStormSurge.getId(), diademaSalvage))
            .save(consumer, prefix(IafModifiers.MagneticStormSurge.getId(), diademaFolder));
        ModifierRecipeBuilder.modifier(IafModifiers.BurnstheSky.getId())
                .addInput(IafItemRegistry.DRAGON_SKULL_FIRE.get(), 1)
                .addInput(IafItemRegistry.FIRE_DRAGON_BLOOD.get(), 1)
                .addInput(IafItemRegistry.FIRE_DRAGON_HEART.get(), 1)
                .disallowCrystal()
                .setSlots(DiademaSlots.DIADEMA, 1)
                .setMaxLevel(1)
                .setTools(ETTagsUtil.DRAGON_ARMOR)
                .saveSalvage(consumer, prefix(IafModifiers.BurnstheSky.getId(), diademaSalvage))
                .save(consumer, prefix(IafModifiers.BurnstheSky.getId(), diademaFolder));
        ModifierRecipeBuilder.modifier(IafModifiers.Permafrost.getId())
                .addInput(IafItemRegistry.DRAGON_SKULL_ICE.get(), 1)
                .addInput(IafItemRegistry.ICE_DRAGON_BLOOD.get(), 1)
                .addInput(IafItemRegistry.ICE_DRAGON_HEART.get(), 1)
                .disallowCrystal()
                .setSlots(DiademaSlots.DIADEMA, 1)
                .setMaxLevel(1)
                .setTools(ETTagsUtil.DRAGON_ARMOR)
                .saveSalvage(consumer, prefix(IafModifiers.Permafrost.getId(), diademaSalvage))
                .save(consumer, prefix(IafModifiers.Permafrost.getId(), diademaFolder));
        ToolExpExportRecipeBuilder.export()
            .addInput(ModCore.ExpTransferOrb)
            .save(consumer, location(worktableFolder + "exp_export"));
        consumer.accept(new SimpleFinishedRecipe(location(slotlessFolder + "exp_import"), ModModifiers.TOOL_EXP_IMPORT_SERIALIZER.get()));
    }

    @Override
    public @NotNull String getModId() {
        return Extended_tinker.MODID;
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Modifier Recipe Provider";
    }
}
