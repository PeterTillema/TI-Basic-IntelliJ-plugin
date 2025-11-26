package nl.petertillema.tibasic.psi.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicAssignmentStatement;
import nl.petertillema.tibasic.psi.TIBasicForStatement;
import nl.petertillema.tibasic.psi.TIBasicUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class TIBasicVariableReference extends PsiPolyVariantReferenceBase<PsiElement> {

    public TIBasicVariableReference(@NotNull PsiElement element, TextRange range) {
        super(element, range);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        TIBasicFile file = (TIBasicFile) myElement.getContainingFile();
        List<TIBasicAssignmentStatement> assignments = TIBasicUtil.findAssignments(file, getValue());
        List<TIBasicForStatement> forLoops = TIBasicUtil.findForLoops(file, getValue());
        ArrayList<ResolveResult> results = new ArrayList<>();

        for (TIBasicAssignmentStatement assignment : assignments) {
            results.add(new PsiElementResolveResult(assignment));
        }
        for (TIBasicForStatement forLoop : forLoops) {
            results.add(new PsiElementResolveResult(forLoop));
        }

        return results.toArray(new ResolveResult[0]);
    }

    @Override
    public Object @NotNull [] getVariants() {
        TIBasicFile file = (TIBasicFile) myElement.getContainingFile();
        List<TIBasicAssignmentStatement> assignments = TIBasicUtil.findAssignments(file);
        List<TIBasicForStatement> forLoops = TIBasicUtil.findForLoops(file);
        ArrayList<LookupElement> variants = new ArrayList<>();

        for (TIBasicAssignmentStatement assignment : assignments) {
            if (assignment.getAssignmentTarget() != null) {
                variants.add(LookupElementBuilder.create(assignment.getAssignmentTarget()).withTypeText(assignment.getText()));
            }
        }
        for (TIBasicForStatement forLoop : forLoops) {
            if (forLoop.getForIdentifier() == null) continue;
            String text = forLoop.getPresentation().getPresentableText();
            variants.add(LookupElementBuilder.create(forLoop.getForIdentifier()).withTypeText(text));
        }

        return variants.toArray();
    }

}
