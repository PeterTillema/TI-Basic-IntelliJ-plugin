package nl.petertillema.tibasic.psi.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TIBasicLabelReference extends PsiReferenceBase<PsiElement> {

    public TIBasicLabelReference(@NotNull PsiElement element, TextRange range) {
        super(element, range);
    }

    @Override
    public @Nullable PsiElement resolve() {
        var file = (TIBasicFile) myElement.getContainingFile();
        var labels = TIBasicUtil.findLabels(file, getValue());

        return !labels.isEmpty() ? labels.getFirst().getLblName() : null;
    }

    @Override
    public Object @NotNull [] getVariants() {
        var file = (TIBasicFile) myElement.getContainingFile();
        var labels = TIBasicUtil.findLabels(file);
        var variants = new ArrayList<LookupElement>();

        for (var label : labels) {
            variants.add(LookupElementBuilder.create(label.getLblName()).withPresentableText(label.getText()));
        }

        return variants.toArray();
    }
}
