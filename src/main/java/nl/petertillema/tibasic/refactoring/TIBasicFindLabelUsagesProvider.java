package nl.petertillema.tibasic.refactoring;

import com.intellij.lang.cacheBuilder.VersionedWordsScanner;
import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.Processor;
import nl.petertillema.tibasic.psi.TIBasicNamedElement;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import nl.petertillema.tibasic.syntax.TIBasicLexerAdapter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TIBasicFindLabelUsagesProvider implements FindUsagesProvider {

    @Override
    public @NotNull WordsScanner getWordsScanner() {
        return new GotoLabelWordScanner();
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof TIBasicNamedElement;
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

    private static class GotoLabelWordScanner extends VersionedWordsScanner {

        @Override
        public void processWords(@NotNull CharSequence fileText, @NotNull Processor<? super WordOccurrence> processor) {
            var lexer = new TIBasicLexerAdapter();
            var occurrence = new WordOccurrence(fileText, 0, 0, null);

            lexer.start(fileText);

            IElementType type;
            while ((type = lexer.getTokenType()) != null) {
                if (type == TIBasicTypes.GOTO) {
                    var beginLabelIndex = lexer.getTokenEnd();

                    // Get everything up to the newline or end of the file
                    do {
                        lexer.advance();
                        type = lexer.getTokenType();
                    } while (type != null && type != TIBasicTypes.CRLF);

                    // Eventually process the occurrence
                    var endLabelIndex = lexer.getTokenStart();
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
