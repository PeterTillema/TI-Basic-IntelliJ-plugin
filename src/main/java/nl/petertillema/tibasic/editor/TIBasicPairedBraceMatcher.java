package nl.petertillema.tibasic.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TIBasicPairedBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] BRACE_PAIRS = new BracePair[]{
            new BracePair(TIBasicTypes.LPAREN, TIBasicTypes.RPAREN, false),
            new BracePair(TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS, TIBasicTypes.RPAREN, false),
            new BracePair(TIBasicTypes.COMMAND_WITH_PARENS, TIBasicTypes.RPAREN, false),
            new BracePair(TIBasicTypes.LBRACKET, TIBasicTypes.RBRACKET, false),
            new BracePair(TIBasicTypes.LCURLY, TIBasicTypes.RCURLY, false),
    };

    @Override
    public BracePair @NotNull [] getPairs() {
        return BRACE_PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }

}
