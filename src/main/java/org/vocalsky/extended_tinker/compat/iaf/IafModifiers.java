package org.vocalsky.extended_tinker.compat.iaf;

import com.csdy.tcondiadema.modifier.CommonDiademaModifier;
import com.csdy.tcondiadema.modifier.DiademaModifier;
import org.vocalsky.extended_tinker.Extended_tinker;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class IafModifiers {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Extended_tinker.MODID);
    public static final StaticModifier<DiademaModifier> magnetic_storm_surge = MODIFIERS.register("magnetic_storm_surge", CommonDiademaModifier.Create(IafDiadema.MagneticStormSurge));
}
