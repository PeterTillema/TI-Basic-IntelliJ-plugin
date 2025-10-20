package nl.petertillema.tibasic.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to provide documentation for elements which should display the documentation
 * which is not done automatically for all kinds of elements. The reference doesn't resolve to
 * anything, nor is it used anywhere.
 */
public class TIBasicEmptyReference extends PsiPolyVariantReferenceBase<PsiElement> {

    public TIBasicEmptyReference(@NotNull PsiElement element, TextRange rangeInElement) {
        super(element, rangeInElement);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ResolveResult.EMPTY_ARRAY;
    }

    @Override
    public Object @NotNull [] getVariants() {
        return new Object[0];
    }

}
