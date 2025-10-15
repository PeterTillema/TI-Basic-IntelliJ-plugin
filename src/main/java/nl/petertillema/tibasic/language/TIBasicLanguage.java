package nl.petertillema.tibasic.language;

import com.intellij.lang.Language;

public final class TIBasicLanguage extends Language {

    public static final TIBasicLanguage INSTANCE = new TIBasicLanguage();

    private TIBasicLanguage() {
        super("TIBasic");
    }

}
