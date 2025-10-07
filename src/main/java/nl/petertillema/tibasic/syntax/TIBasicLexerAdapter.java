package nl.petertillema.tibasic.syntax;

import com.intellij.lexer.FlexAdapter;

public class TIBasicLexerAdapter extends FlexAdapter {

    public TIBasicLexerAdapter() {
        super(new TIBasicLexer(null));
    }

}
