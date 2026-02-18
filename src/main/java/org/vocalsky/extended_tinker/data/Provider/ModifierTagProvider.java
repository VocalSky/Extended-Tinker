package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModCore;
import org.vocalsky.extended_tinker.compat.golem.GolemCore;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

import static slimeknights.tconstruct.common.TinkerTags.Modifiers.*;

public class ModifierTagProvider extends AbstractModifierTagProvider {
    public ModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(UPGRADES).replace(false).addOptional(ModCore.Modifiers.Ids.firework_flight);
        this.tag(INTERACTION_ABILITIES).replace(false).addOptional(ModCore.Modifiers.Ids.firework_star);
        this.tag(CHESTPLATE_ABILITIES).replace(false).addOptional(ModCore.Modifiers.Ids.painless, ModCore.Modifiers.Ids.asone);
        this.tag(RANGED_ABILITIES).replace(false).add(ModCore.Modifiers.Ids.shoot_firework);
        this.tag(BOOT_ABILITIES).replace(false).addOptional(GolemCore.Modifiers.Ids.golem_beacon);
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Modifier Tag Provider";
    }
}