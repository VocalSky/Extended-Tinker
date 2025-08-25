package org.vocalsky.extended_tinker.data.Provider.LangData;

import lombok.Getter;
import org.vocalsky.extended_tinker.data.Provider.LangData.zh_cn;

import java.util.function.Function;

public interface ILang {
    String getLocale();
    void allTranslation(Function<ILangData, Boolean> translation);
}
