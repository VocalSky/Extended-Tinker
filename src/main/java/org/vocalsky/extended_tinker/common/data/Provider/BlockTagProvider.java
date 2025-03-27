package org.vocalsky.extended_tinker.common.data.Provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.vocalsky.extended_tinker.Extended_tinker;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Extended_tinker.MODID, existingFileHelper);
    }

    @Override
    public void addTags() {}
}
