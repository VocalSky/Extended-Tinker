package org.vocalsky.extended_tinker.common.modifier.Firecrack;

import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class FirecrackFlightModifier extends Modifier {
    private static final TinkerDataCapability.TinkerDataKey<Integer> FLIGHT = TConstruct.createKey("flight_firecrack");

    public FirecrackFlightModifier() {
        super();
    }
}
