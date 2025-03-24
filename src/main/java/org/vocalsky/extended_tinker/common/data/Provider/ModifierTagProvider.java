package org.vocalsky.extended_tinker.common.data.Provider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModModifiers;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.*;

public class ModifierTagProvider extends AbstractModifierTagProvider {
    public ModifierTagProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(UPGRADES).add(ModModifiers.FLIGHT.getId());
        this.tag(CHESTPLATE_ABILITIES).add(ModModifiers.PAINLESS.getId(), ModModifiers.ASONE.getId());
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Modifier Provider";
    }
}