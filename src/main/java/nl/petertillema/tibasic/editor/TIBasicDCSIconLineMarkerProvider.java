package nl.petertillema.tibasic.editor;

import com.intellij.codeInsight.daemon.GutterName;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.FunctionUtil;
import nl.petertillema.tibasic.editor.dcs.AbstractDCSIcon;
import nl.petertillema.tibasic.editor.dcs.DCSIconEditorService;
import nl.petertillema.tibasic.editor.dcs.impl.DCSColoredIcon;
import nl.petertillema.tibasic.editor.dcs.impl.DCSMonochrome16Icon;
import nl.petertillema.tibasic.editor.dcs.impl.DCSMonochrome8Icon;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.psi.TIBasicExprStatement;
import nl.petertillema.tibasic.psi.TIBasicLiteralExpr;
import nl.petertillema.tibasic.psi.TIBasicTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

public final class TIBasicDCSIconLineMarkerProvider extends LineMarkerProviderDescriptor {

    @Override
    public @GutterName @NotNull String getName() {
        return "TI-BASIC icon editor";
    }

    @Override
    public @Nullable LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        // Check if the file starts with ":DCS"
        if (!element.getContainingFile().getText().startsWith(":DCS")) {
            return null;
        }

        // Element should be a string, with the proper parents
        if (element.getNode().getElementType() != TIBasicTypes.STRING) return null;
        if (!(element.getParent() instanceof TIBasicLiteralExpr literalExpr)) return null;
        if (!(literalExpr.getParent() instanceof TIBasicExprStatement statement)) return null;
        if (!(statement.getParent() instanceof TIBasicFile file)) return null;

        // Statement should be the second child of the main file
        PsiElement[] children = file.getChildren();
        if (children.length < 4) return null;
        if (children[2].getNode().getElementType() != TIBasicTypes.CRLF || children[3] != statement) return null;

        // Icon is valid, let's check the length
        String iconData = element.getText().replace("\"", "");
        if (iconData.length() > 256) return null;

        AbstractDCSIcon icon = switch (iconData.length()) {
            case 16 -> new DCSMonochrome8Icon(iconData);
            case 64 -> new DCSMonochrome16Icon(iconData);
            default -> new DCSColoredIcon(iconData);
        };

        return new DCSLineMarkerInfo(element, icon, iconData);
    }

    private static class DCSLineMarkerInfo extends LineMarkerInfo<PsiElement> {

        private DCSLineMarkerInfo(final @NotNull PsiElement element, AbstractDCSIcon icon, final @NotNull String data) {
            super(element,
                    element.getTextRange(),
                    icon,
                    FunctionUtil.<Object, String>nullConstant(),
                    (e, elt) -> onIconClick(e, elt, data),
                    GutterIconRenderer.Alignment.LEFT,
                    () -> "DCS Icon");
        }

        private static void onIconClick(MouseEvent e, PsiElement element, String data) {
            if (!element.isWritable()) return;

            RelativePoint relativePoint = new RelativePoint(e.getComponent(), e.getPoint());
            Project project = element.getProject();
            PsiFile file = element.getContainingFile();
            Document document = PsiDocumentManager.getInstance(project).getDocument(file);

            DCSIconEditorService.getInstance().showPopup(relativePoint, data, newData -> {
                if (document == null) return;
                String quoted = '"' + (newData == null ? "" : newData) + '"';
                TextRange range = element.getTextRange();
                Runnable action = () -> {
                    document.replaceString(range.getStartOffset(), range.getEndOffset(), quoted);
                    PsiDocumentManager.getInstance(project).commitDocument(document);
                };
                WriteCommandAction.runWriteCommandAction(project, action);
            });
        }
    }

}
