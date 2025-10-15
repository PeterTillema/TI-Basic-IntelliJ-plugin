package nl.petertillema.tibasic.psi.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class TIBasicVariableReference extends PsiPolyVariantReferenceBase<PsiElement> {

    public TIBasicVariableReference(@NotNull PsiElement element, TextRange range) {
        super(element, range);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        var file = (TIBasicFile) myElement.getContainingFile();
        var assignments = TIBasicUtil.findAssignments(file, getValue());
        var forLoops = TIBasicUtil.findForLoops(file, getValue());
        var results = new ArrayList<ResolveResult>();

        for (var assignment : assignments) {
            results.add(new PsiElementResolveResult(assignment));
        }
        for (var forLoop : forLoops) {
            results.add(new PsiElementResolveResult(forLoop));
        }

        return results.toArray(new ResolveResult[0]);
    }

    @Override
    public Object @NotNull [] getVariants() {
        var file = (TIBasicFile) myElement.getContainingFile();
        var assignments = TIBasicUtil.findAssignments(file);
        var forLoops = TIBasicUtil.findForLoops(file);
        var variants = new ArrayList<LookupElement>();

        for (var assignment : assignments) {
            variants.add(LookupElementBuilder.create(assignment.getAssignmentTarget()).withTypeText(assignment.getText()));
        }
        for (var forLoop : forLoops) {
            var endOffset = forLoop.getExprList().getLast().getTextRangeInParent().getEndOffset();
            var text = forLoop.getText().substring(0, endOffset);
            variants.add(LookupElementBuilder.create(forLoop.getForIdentifier()).withTypeText(text));
        }

        return variants.toArray();
    }

}
