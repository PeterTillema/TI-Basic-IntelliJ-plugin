package nl.petertillema.tibasic.syntax;

import com.intellij.psi.tree.TokenSet;
import nl.petertillema.tibasic.psi.TIBasicTypes;

public interface TIBasicTokenSets {
    TokenSet COMMENTS = TokenSet.create(TIBasicTypes.COMMENT);
    TokenSet STRINGS = TokenSet.create(TIBasicTypes.STRING);
}
