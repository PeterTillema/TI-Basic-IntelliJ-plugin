package nl.petertillema.tibasic.syntax.documentation;

import com.intellij.model.Pointer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SmartPsiElementPointer;
import nl.petertillema.tibasic.language.TIBasicIcons;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.SmartPointersKt.createSmartPointer;

public record TIBasicDocumentationTarget(PsiElement element) implements DocumentationTarget {

    @Override
    public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
        SmartPsiElementPointer<PsiElement> elementPointer = createSmartPointer(this.element);

        return () -> {
            PsiElement element = elementPointer.dereference();
            if (element == null) {
                return null;
            }
            return new TIBasicDocumentationTarget(element);
        };
    }

    @Override
    public @NotNull TargetPresentation computePresentation() {
        return TargetPresentation
                .builder(this.element.getText())
                .icon(TIBasicIcons.FILE)
                .presentation();
    }

    @Override
    public @Nullable DocumentationResult computeDocumentation() {
        String token = this.element.getText();
        if (this.element.getNode().getElementType() == TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS || this.element.getNode().getElementType() == TIBasicTypes.COMMAND_WITH_PARENS) {
            token = token + "(";
        }

        TIBasicDocumentationService service = ApplicationManager.getApplication().getService(TIBasicDocumentationService.class);
        String html = service.getToken(token);

        return html == null ? null : DocumentationResult.documentation(html);
    }
}