package nl.petertillema.tibasic.editor.formatting;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.NotNull;

public final class TIBasicEndEnterHandler implements EnterHandlerDelegate {

    @Override
    public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
        Project project = file.getProject();
        Document doc = editor.getDocument();
        int caretOffset = editor.getCaretModel().getOffset();
        int line = doc.getLineNumber(caretOffset);
        if (line == 0) {
            return Result.Continue;
        }

        // Commit the document before performing an action on it
        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
        documentManager.commitDocument(editor.getDocument());

        // Check the text of the previous line
        int previousLineStart = doc.getLineStartOffset(line - 1);
        int previousLineEnd = doc.getLineEndOffset(line - 1);
        String previousLineText = doc.getText(new TextRange(previousLineStart, previousLineEnd)).trim();

        if (previousLineText.matches("End|Else")) {
            Runnable action = () -> {
                CodeStyleManager.getInstance(project).adjustLineIndent(file, previousLineStart);
                CodeStyleManager.getInstance(project).adjustLineIndent(file, doc.getLineStartOffset(line));
            };

            WriteCommandAction.runWriteCommandAction(project, action);
        }

        return Result.Continue;
    }

}
