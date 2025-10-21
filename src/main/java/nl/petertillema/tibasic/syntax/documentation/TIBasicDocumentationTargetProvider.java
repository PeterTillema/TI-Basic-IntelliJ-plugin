package nl.petertillema.tibasic.syntax.documentation;

import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.DocumentationTargetProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TIBasicDocumentationTargetProvider implements DocumentationTargetProvider {

    private static final List<IElementType> ALLOWED_DOCUMENTATION_TYPES = List.of(
            TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS,
            TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS,
            TIBasicTypes.COMMAND_WITH_PARENS,
            TIBasicTypes.COMMAND_NO_PARENS
    );

    @Override
    public @NotNull List<? extends @NotNull DocumentationTarget> documentationTargets(@NotNull PsiFile file, int offset) {
        if (!(file instanceof TIBasicFile)) return List.of();

        if (offset == file.getTextLength()) {
            offset = Math.max(0, offset - 1);
        }
        var element = file.findElementAt(offset);
        if (element == null) return List.of();

        if (!ALLOWED_DOCUMENTATION_TYPES.contains(element.getNode().getElementType())) return List.of();

        return List.of(new TIBasicDocumentationTarget(element));
    }
}
