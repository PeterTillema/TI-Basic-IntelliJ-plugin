package nl.petertillema.tibasic.psi.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicLblStatement;
import nl.petertillema.tibasic.psi.TIBasicUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TIBasicLabelReference extends PsiReferenceBase<PsiElement> {

    public TIBasicLabelReference(@NotNull PsiElement element, TextRange range) {
        super(element, range);
    }

    @Override
    public @Nullable PsiElement resolve() {
        TIBasicFile file = (TIBasicFile) myElement.getContainingFile();
        List<TIBasicLblStatement> labels = TIBasicUtil.findLabels(file, getValue());

        return !labels.isEmpty() ? labels.getFirst().getLblName() : null;
    }

    @Override
    public Object @NotNull [] getVariants() {
        TIBasicFile file = (TIBasicFile) myElement.getContainingFile();
        List<TIBasicLblStatement> labels = TIBasicUtil.findLabels(file);
        ArrayList<LookupElement> variants = new ArrayList<>();

        for (TIBasicLblStatement label : labels) {
            if (label.getLblName() != null) {
                variants.add(LookupElementBuilder.create(label.getLblName()).withPresentableText(label.getText()));
            }
        }

        return variants.toArray();
    }
}
