package nl.petertillema.tibasic.editor.formatting;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TIBasicEndEnterHandler implements EnterHandlerDelegate {

    @Override
    public Result preprocessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull Ref<Integer> caretOffset, @NotNull Ref<Integer> caretAdvance, @NotNull DataContext dataContext, @Nullable EditorActionHandler originalHandler) {
        return Result.Continue;
    }

    @Override
    public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
        var project = file.getProject();
        var doc = editor.getDocument();
        var caretOffset = editor.getCaretModel().getOffset();
        var line = doc.getLineNumber(caretOffset);
        if (line == 0) {
            return Result.Continue;
        }

        // Commit the document before performing an action on it
        var documentManager = PsiDocumentManager.getInstance(project);
        documentManager.commitDocument(editor.getDocument());

        // Check the text of the previous line
        var previousLineStart = doc.getLineStartOffset(line - 1);
        var previousLineEnd = doc.getLineEndOffset(line - 1);
        var previousLineText = doc.getText(new TextRange(previousLineStart, previousLineEnd)).trim();

        if (previousLineText.matches("End|Else")) {
            Runnable action = () -> CodeStyleManager.getInstance(project).adjustLineIndent(file, previousLineStart);

            WriteCommandAction.runWriteCommandAction(project, action);
        }

        return Result.Continue;
    }

}
