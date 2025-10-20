package nl.petertillema.tibasic.syntax.documentation;

import com.intellij.model.Pointer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.PsiElement;
import nl.petertillema.tibasic.language.TIBasicIcons;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.SmartPointersKt.createSmartPointer;

public record TIBasicDocumentationTarget(PsiElement element,
                                         PsiElement originalElement) implements DocumentationTarget {

    @Override
    public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
        var elementPointer = createSmartPointer(this.element);
        var originalElementPointer = this.originalElement == null ? null : createSmartPointer(this.originalElement);

        return () -> {
            var element = elementPointer.dereference();
            if (element == null) {
                return null;
            }
            return new TIBasicDocumentationTarget(element, originalElementPointer == null ? null : originalElementPointer.dereference());
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
        var token = this.element.getText();
        if (this.element.getNode().getElementType() == TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS || this.element.getNode().getElementType() == TIBasicTypes.COMMAND_WITH_PARENS) {
            token = token + "(";
        }

        var service = ApplicationManager.getApplication().getService(TIBasicDocumentationService.class);
        var html = service.getToken(token);

        return html == null ? null : DocumentationResult.documentation(html);
    }
}