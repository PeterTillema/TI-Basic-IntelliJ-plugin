package nl.petertillema.tibasic.completion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.patterns.PlatformPatterns.elementType;
import static com.intellij.patterns.PlatformPatterns.psiElement;

public class TIBasicPatterns {

    public static final PsiElementPattern.Capture<PsiElement> START_OF_FILE = psiElement().atStartOf(psiElement(TIBasicFile.class));

    public static final PsiElementPattern.Capture<PsiElement> START_OF_LINE =
            psiElement().afterLeaf(
                    psiElement().withElementType(elementType().or(TIBasicTypes.CRLF, TIBasicTypes.COLON))
            );

    public static final PsiElementPattern.Capture<PsiElement> CONTROL_FLOW_INITIALIZER =
            psiElement().with(
                    afterConsecutiveTypesSkipping(
                            psiElement().whitespaceCommentEmptyOrError(),
                            List.of(
                                    psiElement().withElementType(elementType().or(TIBasicTypes.FOR, TIBasicTypes.IS, TIBasicTypes.DS)),
                                    psiElement().withElementType(TIBasicTypes.LPAREN)
                            )
                    )
            );

    public static final PsiElementPattern.Capture<PsiElement> WITHIN_EXPRESSION =
            psiElement().withAncestor(Integer.MAX_VALUE - 1, psiElement(TIBasicExpr.class));

    public static final PsiElementPattern.Capture<PsiElement> AFTER_STO_DIM =
            psiElement().with(
                    afterConsecutiveTypesSkipping(
                            psiElement().whitespaceCommentEmptyOrError(),
                            List.of(
                                    psiElement().withElementType(TIBasicTypes.STO),
                                    psiElement().withElementType(TIBasicTypes.DIM),
                                    psiElement().withElementType(TIBasicTypes.LPAREN)
                            )
                    )
            );

    public static PsiElementPattern.Capture<PsiElement> afterType(IElementType type) {
        return psiElement().afterLeaf(psiElement().withElementType(type));
    }

    private static PatternCondition<PsiElement> afterConsecutiveTypesSkipping(ElementPattern<PsiElement> skip, List<PsiElementPattern.Capture<PsiElement>> patterns) {
        return new PatternCondition<>("afterConsecutiveTypesSkipping") {
            @Override
            public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                for (int i = patterns.size() - 1; i >= 0; i--) {
                    do {
                        element = PsiTreeUtil.prevLeaf(element);
                    } while (element != null && skip.accepts(element, context));

                    if (element == null) return false;
                    if (!patterns.get(i).accepts(element, context)) return false;

                }
                return true;
            }
        };
    }

}
