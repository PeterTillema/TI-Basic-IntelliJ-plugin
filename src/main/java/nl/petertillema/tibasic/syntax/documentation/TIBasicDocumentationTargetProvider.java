package nl.petertillema.tibasic.syntax.documentation;

import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.DocumentationTargetProvider;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.language.TIBasicFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TIBasicDocumentationTargetProvider implements DocumentationTargetProvider {

    @Override
    public @NotNull List<? extends @NotNull DocumentationTarget> documentationTargets(@NotNull PsiFile file, int offset) {
        if (!(file instanceof TIBasicFile)) return List.of();

        if (offset == file.getTextLength()) {
            offset = Math.max(0, offset - 1);
        }
        var element = file.findElementAt(offset);
        if (element == null) return List.of();

        return List.of(new TIBasicDocumentationTarget(element));
    }
}
