package nl.petertillema.tibasic.annotator;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.util.IncorrectOperationException;
import nl.petertillema.tibasic.TIBasicMessageBundle;
import nl.petertillema.tibasic.analysis.commands.TIBasicCommand;
import nl.petertillema.tibasic.analysis.functions.TIBasicFunction;
import nl.petertillema.tibasic.psi.TIBasicCommandStatement;
import nl.petertillema.tibasic.psi.TIBasicExpr;
import nl.petertillema.tibasic.psi.TIBasicFuncExpr;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.getElementsOfRange;
import static nl.petertillema.tibasic.analysis.TIBasicCommandFunctionMap.COMMAND_MAP;
import static nl.petertillema.tibasic.analysis.TIBasicCommandFunctionMap.FUNCTION_MAP;

public class TIBasicNrArgumentsAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TIBasicFuncExpr funcExpr) checkFunction(holder, element.getFirstChild(), funcExpr.getExprList());
        if (element instanceof TIBasicCommandStatement commandStatement) checkCommand(holder, element.getFirstChild(), commandStatement.getExprList());
    }

    private void checkFunction(AnnotationHolder holder, PsiElement mainElement, List<TIBasicExpr> exprList) {
        TIBasicFunction functionImpl = FUNCTION_MAP.get(mainElement.getText());
        if (functionImpl == null) return;

        if (exprList.size() < functionImpl.getMinNrArguments()) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Not enough arguments, at least " + functionImpl.getMinNrArguments() + " required")
                    .range(mainElement)
                    .create();
        }
        if (exprList.size() > functionImpl.getMaxNrArguments()) {
            for (int i = functionImpl.getMaxNrArguments(); i < exprList.size(); i++) {
                PsiElement startElement = exprList.get(functionImpl.getMaxNrArguments() - 1).getNextSibling();
                TIBasicExpr endElement = exprList.getLast();

                holder.newAnnotation(HighlightSeverity.ERROR, TIBasicMessageBundle.message("annotator.too.many.arguments.description"))
                        .range(exprList.get(i))
                        .withFix(new RemoveArgumentsQuickFix(startElement, endElement))
                        .create();
            }
        }
    }

    private void checkCommand(AnnotationHolder holder, PsiElement mainElement, List<TIBasicExpr> exprList) {
        TIBasicCommand commandImpl = COMMAND_MAP.get(mainElement.getText());
        if (commandImpl == null) return;

        if (exprList.size() < commandImpl.getMinNrArguments()) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Not enough arguments, at least " + commandImpl.getMinNrArguments() + " required")
                    .range(mainElement)
                    .create();
        }
        if (exprList.size() > commandImpl.getMaxNrArguments()) {
            PsiElement startElement = commandImpl.getMaxNrArguments() == 0 ?
                    exprList.getFirst() :
                    exprList.get(commandImpl.getMaxNrArguments() - 1).getNextSibling();
            TIBasicExpr endElement = exprList.getLast();

            for (int i = commandImpl.getMaxNrArguments(); i < exprList.size(); i++) {
                holder.newAnnotation(HighlightSeverity.ERROR, TIBasicMessageBundle.message("annotator.too.many.arguments.description"))
                        .range(exprList.get(i))
                        .withFix(new RemoveArgumentsQuickFix(startElement, endElement))
                        .create();
            }
        }
    }

    private static class RemoveArgumentsQuickFix extends BaseIntentionAction {

        private final @NotNull PsiElement firstToDelete;
        private final @NotNull PsiElement lastToDelete;

        private RemoveArgumentsQuickFix(@NotNull PsiElement firstToDelete, @NotNull PsiElement lastToDelete) {
            this.firstToDelete = firstToDelete;
            this.lastToDelete = lastToDelete;
        }

        @Override
        public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
            return true;
        }

        @Override
        public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
            getElementsOfRange(firstToDelete, lastToDelete).forEach(el -> {
                if (el instanceof PsiWhiteSpace) return;
                el.delete();
            });
        }

        @Override
        public @NotNull @IntentionName String getText() {
            return TIBasicMessageBundle.message("annotator.too.many.arguments.fix.text");
        }

        @Override
        public @NotNull @IntentionFamilyName String getFamilyName() {
            return TIBasicMessageBundle.message("annotator.too.many.arguments.fix.family.name");
        }
    }

}
