package org.vocalsky.extended_tinker.common.data.Provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.common.ModItems;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.tools.TinkerToolParts;

public class StationSlotLayoutProvider extends AbstractStationSlotLayoutProvider {
    public StationSlotLayoutProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addLayouts() {
        defineModifiable(ModItems.Tools.HORSE_ARMOR)
            .sortIndex(514)
            .addInputItem(TinkerToolParts.maille, 3, 50)
            .addInputItem(TinkerToolParts.shieldCore, 23, 50)
            .addInputItem(TinkerToolParts.maille, 43, 50)
            .addInputItem(TinkerToolParts.shieldCore, 63, 50)
            .addInputItem(ModItems.Parts.BRIDLE, 78, 30)
            .build();

        defineModifiable(ModItems.Tools.FIRECRACK)
            .sortIndex(1145)
            .addInputItem(TinkerToolParts.toolHandle, 30, 50)
            .addInputItem(TinkerToolParts.bowLimb, 10, 50)
            .addInputItem(new Pattern(Extended_tinker.MODID, "gunpowder"), net.minecraft.world.item.Items.GUNPOWDER, 30, 30)
            .addInputItem(new Pattern(Extended_tinker.MODID, "paper"), net.minecraft.world.item.Items.PAPER, 50, 50)
            .build();
    }

    @Override
    public @NotNull String getName() {
        return "Extended Tinker Station Slot Layout Data Provider";
    }
}
