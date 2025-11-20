package nl.petertillema.tibasic.findUsages;

import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.Processor;
import nl.petertillema.tibasic.psi.TIBasicLblName;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.syntax.TIBasicLexerAdapter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.TokenType.WHITE_SPACE;

public final class TIBasicFindLabelUsagesProvider implements FindUsagesProvider {

    @Override
    public @NotNull WordsScanner getWordsScanner() {
        return new GotoLabelWordScanner();
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof TIBasicLblName;
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        return "Label";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        return "";
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return "";
    }

    private static class GotoLabelWordScanner implements WordsScanner {

        @Override
        public void processWords(@NotNull CharSequence fileText, @NotNull Processor<? super WordOccurrence> processor) {
            TIBasicLexerAdapter lexer = new TIBasicLexerAdapter();
            WordOccurrence occurrence = new WordOccurrence(fileText, 0, 0, null);

            lexer.start(fileText);

            IElementType type;
            while ((type = lexer.getTokenType()) != null) {
                if (type == TIBasicTypes.GOTO) {
                    // Skip whitespace
                    do {
                        lexer.advance();
                        type = lexer.getTokenType();
                    } while (type != null && type == WHITE_SPACE);

                    int beginLabelIndex = lexer.getTokenStart();

                    // Get everything up to the newline or end of the file
                    do {
                        lexer.advance();
                        type = lexer.getTokenType();
                    } while (type != null && type != TIBasicTypes.CRLF && type != TIBasicTypes.COLON);

                    // Eventually process the occurrence
                    int endLabelIndex = lexer.getTokenStart();
                    if (endLabelIndex > beginLabelIndex) {
                        occurrence.init(fileText, beginLabelIndex, endLabelIndex, null);
                        if (!processor.process(occurrence)) {
                            break;
                        }
                    }
                }
                lexer.advance();
            }
        }

    }

}
