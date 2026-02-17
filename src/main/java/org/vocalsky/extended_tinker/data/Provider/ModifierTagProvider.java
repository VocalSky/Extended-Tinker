package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.*;

public class ModifierTagProvider extends AbstractModifierTagProvider {
    public ModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(RANGED_UPGRADES).replace(false).addOptional(ModCore.Modifiers.FIREWORK_FLIGHT.getId());
        this.tag(INTERACTION_ABILITIES).replace(false).addOptional(ModCore.Modifiers.FIREWORK_STAR.getId());
        this.tag(CHESTPLATE_ABILITIES).replace(false).addOptional(ModCore.Modifiers.PAINLESS.getId(), ModCore.Modifiers.AS_ONE.getId());
        this.tag(RANGED_ABILITIES).replace(false).add(ModCore.Modifiers.shoot_firework);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Modifier Tag Provider";
    }
}