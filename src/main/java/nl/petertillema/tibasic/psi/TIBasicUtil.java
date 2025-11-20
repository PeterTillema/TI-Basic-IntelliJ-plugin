package nl.petertillema.tibasic.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import nl.petertillema.tibasic.language.TIBasicFile;
import nl.petertillema.tibasic.language.TIBasicFileType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TIBasicUtil {

    public static TIBasicFile createFromText(Project project, String text) {
        return (TIBasicFile) PsiFileFactory.getInstance(project).createFileFromText("dummy.basic", TIBasicFileType.INSTANCE, text);
    }

    public static List<TIBasicLblStatement> findLabels(TIBasicFile file, String key) {
        Collection<TIBasicLblStatement> labels = PsiTreeUtil.findChildrenOfType(file, TIBasicLblStatement.class);

        return labels.stream()
                .filter(label -> label.getLblName() != null)
                .filter(label -> key.equals(label.getLblName().getText()))
                .toList();
    }

    public static List<TIBasicLblStatement> findLabels(TIBasicFile file) {
        Collection<TIBasicLblStatement> labels = PsiTreeUtil.findChildrenOfType(file, TIBasicLblStatement.class);
        return new ArrayList<>(labels);
    }

    public static List<TIBasicAssignmentStatement> findAssignments(TIBasicFile file, String key) {
        Collection<TIBasicAssignmentStatement> assignments = PsiTreeUtil.findChildrenOfType(file, TIBasicAssignmentStatement.class);

        return assignments.stream()
                .filter(assignment -> assignment.getAssignmentTarget() != null)
                .filter(assignment -> assignment.getAssignmentTarget().getText().equals(key))
                .toList();
    }

    public static List<TIBasicAssignmentStatement> findAssignments(TIBasicFile file) {
        Collection<TIBasicAssignmentStatement> assignments = PsiTreeUtil.findChildrenOfType(file, TIBasicAssignmentStatement.class);

        return new ArrayList<>(assignments);
    }

    public static List<TIBasicForStatement> findForLoops(TIBasicFile file, String key) {
        Collection<TIBasicForStatement> forLoops = PsiTreeUtil.findChildrenOfType(file, TIBasicForStatement.class);

        return forLoops.stream()
                .filter(forLoop -> forLoop.getNode().getChildren(null)[1].getText().equals(key))
                .toList();
    }

    public static List<TIBasicForStatement> findForLoops(TIBasicFile file) {
        Collection<TIBasicForStatement> forLoops = PsiTreeUtil.findChildrenOfType(file, TIBasicForStatement.class);

        return new ArrayList<>(forLoops);
    }

    public static String getTextUntilNewline(PsiElement element) {
        StringBuilder sb = new StringBuilder();
        ASTNode node = element.getNode();
        if (node == null) return "";

        ASTNode current = node.getFirstChildNode();
        while (current != null) {
            IElementType type = current.getElementType();
            if (type.equals(TIBasicTypes.COLON) || type.equals(TIBasicTypes.CRLF)) break;
            sb.append(current.getText());
            current = current.getTreeNext();
        }

        return sb.toString();
    }

}
