package org.vocalsky.extended_tinker.common.recipe;

import org.vocalsky.extended_tinker.util.ReflectionUtil;
import slimeknights.tconstruct.library.recipe.partbuilder.*;

public class UltraPartRecipe extends PartRecipe {
    public UltraPartRecipe(PartRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getPattern(), ReflectionUtil.getReflection("patternItem", recipe), recipe.getCost(), true, ReflectionUtil.getReflection("output", recipe), ReflectionUtil.getReflection("outputCount", recipe));
    }
}