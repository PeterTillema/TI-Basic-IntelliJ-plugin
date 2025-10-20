package nl.petertillema.tibasic.syntax.documentation;

import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.language.TIBasicFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TIBasicDocumentationProvider implements PsiDocumentationTargetProvider {

    @Override
    public @Nullable DocumentationTarget documentationTarget(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        if (!(element.getContainingFile() instanceof TIBasicFile)) return null;

        return new TIBasicDocumentationTarget(element, originalElement);
    }
}
