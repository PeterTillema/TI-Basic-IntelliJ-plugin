package nl.petertillema.tibasic.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import nl.petertillema.tibasic.syntax.TIBasicTokenSets;

public final class TIBasicQuoteHandler extends SimpleTokenSetQuoteHandler {

    public TIBasicQuoteHandler() {
        super(TIBasicTokenSets.STRINGS);
    }

}
