package nl.petertillema.tibasic.language;

import com.intellij.lang.Language;

public class TIBasicLanguage extends Language {

    public static final TIBasicLanguage INSTANCE = new TIBasicLanguage();

    protected TIBasicLanguage() {
        super("TIBasic");
    }

}
