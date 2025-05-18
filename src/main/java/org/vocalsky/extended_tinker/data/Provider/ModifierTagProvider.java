package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModModifiers;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.*;

public class ModifierTagProvider extends AbstractModifierTagProvider {
    public ModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(RANGED_UPGRADES).replace(false).add(ModModifiers.FLIGHT.getId());
        this.tag(INTERACTION_ABILITIES).replace(false).add(ModModifiers.STAR.getId());
        this.tag(CHESTPLATE_ABILITIES).replace(false).add(ModModifiers.PAINLESS.getId(), ModModifiers.ASONE.getId());
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Modifier Tag Provider";
    }
}