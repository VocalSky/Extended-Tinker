package org.vocalsky.extended_tinker.data.Provider;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.data.Provider.LangData.ILang;

public class LangProvider extends LanguageProvider {
    private final ILang lang;

    public LangProvider(PackOutput output, ILang lang) {
        super(output, Extended_tinker.MODID, lang.getLocale());
        this.lang = lang;
    }

    @Override
    protected void addTranslations() {
        lang.allTranslation((translate) -> {
            this.add(translate.getKey(), translate.getValue());
            return true;
        });
    }
}
