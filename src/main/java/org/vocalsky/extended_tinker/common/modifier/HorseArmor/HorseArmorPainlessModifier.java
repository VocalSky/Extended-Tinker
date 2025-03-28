package org.vocalsky.extended_tinker.common.modifier.HorseArmor;

import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class HorseArmorPainlessModifier extends NoLevelsModifier {
    private static final TinkerDataCapability.TinkerDataKey<Integer> PAINLESS = TConstruct.createKey("painless_horsearmor");

    public HorseArmorPainlessModifier() {
        super();
    }

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(PAINLESS, false, null));
    }
}