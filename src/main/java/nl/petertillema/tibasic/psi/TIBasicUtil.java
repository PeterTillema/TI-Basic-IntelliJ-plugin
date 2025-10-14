package nl.petertillema.tibasic.psi;

import com.intellij.psi.util.PsiTreeUtil;
import nl.petertillema.tibasic.language.TIBasicFile;

import java.util.ArrayList;
import java.util.List;

public class TIBasicUtil {

    public static List<TIBasicLblStatement> findLabels(TIBasicFile file, String key) {
        var labels = PsiTreeUtil.findChildrenOfType(file, TIBasicLblStatement.class);

        return labels.stream()
                .filter(label -> key.equals(label.getLblName().getText()))
                .toList();
    }

    public static List<TIBasicLblStatement> findLabels(TIBasicFile file) {
        var labels = PsiTreeUtil.findChildrenOfType(file, TIBasicLblStatement.class);
        return new ArrayList<>(labels);
    }

    public static List<TIBasicAssignmentStatement> findAssignments(TIBasicFile file, String key) {
        var assignments = PsiTreeUtil.findChildrenOfType(file, TIBasicAssignmentStatement.class);

        return assignments.stream()
                .filter(assignment -> assignment.getAssignmentTarget().getText().equals(key))
                .toList();
    }

    public static List<TIBasicAssignmentStatement> findAssignments(TIBasicFile file) {
        var assignments = PsiTreeUtil.findChildrenOfType(file, TIBasicAssignmentStatement.class);

        return new ArrayList<>(assignments);
    }

    public static List<TIBasicForStatement> findForLoops(TIBasicFile file, String key) {
        var forLoops = PsiTreeUtil.findChildrenOfType(file, TIBasicForStatement.class);

        return forLoops.stream()
                .filter(forLoop -> forLoop.getNode().getChildren(null)[1].getText().equals(key))
                .toList();
    }

    public static List<TIBasicForStatement> findForLoops(TIBasicFile file) {
        var forLoops = PsiTreeUtil.findChildrenOfType(file, TIBasicForStatement.class);

        return new ArrayList<>(forLoops);
    }

}
